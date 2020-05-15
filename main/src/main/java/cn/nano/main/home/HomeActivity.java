package cn.nano.main.home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.app.GlobalConfig;
import cn.nano.common.glide.ImgLoader;
import cn.nano.common.utils.FileUtil;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.common.utils.ToastUtil;
import cn.nano.getui.GetuiSDK;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.AppPrefs;
import cn.nano.main.message.HomeMessageFragment;
import cn.nano.main.person.HomeMeFragment;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.MemberInfoResult;
import cn.nano.main.tribe.HomeTribeFragment;
import okhttp3.Call;
import okhttp3.Response;

public class HomeActivity extends CommonActivity implements View.OnClickListener {

    private Fragment mMainFragment;
    private Fragment mTribeFragment;
    private Fragment mMeFragment;
    private Fragment mMessageFragment;

    private Fragment mCurrentFragment;

    private FragmentManager mFragmentManager;

    private ImageView mMainImage;
    private TextView mMainTitle;
    private ImageView mTribeImage;
    private TextView mTribeTitle;
    private ImageView mMeImage;
    private TextView mMeTitle;
    private ImageView mMessageImage;
    private TextView mMessageTitle;

    private int mCurrentTabId = R.id.home_main;


    private static final int[] ENABLE_TAB_IMAGE_RESID = new int[]{R.mipmap.ico_home_enable, R.mipmap.ico_tribe_enable, R.mipmap.ico_message_enable, R.mipmap.ico_me_enable};
    private static final int[] DISABLE_TAB_IMAGE_RESID = new int[]{R.mipmap.ico_home_disable, R.mipmap.ico_tribe_disable, R.mipmap.ico_message_disable, R.mipmap.ico_me_disable};


    //统一跳转
    public static void forward() {
        Intent intent = new Intent(GlobalConfig.getInstance().currentApp(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalConfig.getInstance().currentApp().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GetuiSDK.initSDK(this);
        initView();
        initData();
    }


    private void initView() {
        findViewById(R.id.home_main).setOnClickListener(this);
        findViewById(R.id.home_me).setOnClickListener(this);
        findViewById(R.id.home_message).setOnClickListener(this);
        findViewById(R.id.home_tribe).setOnClickListener(this);
        findViewById(R.id.home_take_photo).setOnClickListener(this);

        mMainImage = findViewById(R.id.home_main_image);
        mTribeImage = findViewById(R.id.home_tribe_image);
        mMessageImage = findViewById(R.id.home_message_image);
        mMeImage = findViewById(R.id.home_me_image);

        mMainTitle = findViewById(R.id.home_main_title);
        mTribeTitle = findViewById(R.id.home_tribe_title);
        mMessageTitle = findViewById(R.id.home_message_title);
        mMeTitle = findViewById(R.id.home_me_title);
    }

    private void initData() {
        mMainFragment = new HomeMainFragment();
        mTribeFragment = new HomeTribeFragment();
        mMeFragment = new HomeMeFragment();
        mMessageFragment = new HomeMessageFragment();

        //添加第一个需要显示的fragment
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.home_content, mMainFragment)
                .commit();
        mCurrentFragment = mMainFragment;
        switchBottomTag(mCurrentTabId);

        //提前获取用户信息保存本地
        ServerApi.getSelfInfo(AccountManager.INSTANCE.getUserToken(), new AutoLoginOutCallback<MemberInfoResult>() {
            @Override
            public MemberInfoResult parseNetworkResponse(Response response, int id) throws Exception {
                MemberInfoResult result = super.parseNetworkResponse(response, id);
                if (result != null && result.getCode() == BaseResult.SUCCESS_CODE) {
                    AccountManager.INSTANCE.syncUserInfo(result.getData().getMInfo());
                }
                return result;
            }
        });
    }

    //如果当前是首页，则开启或者停止视频
    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentFragment instanceof HomeMainFragment) {
            ((HomeMainFragment) mCurrentFragment).resumeVideoPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCurrentFragment instanceof HomeMainFragment) {
            ((HomeMainFragment) mCurrentFragment).pauseVideoPlayer();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.home_take_photo) {
            startTakePhoto();
        }

        if (mCurrentTabId == v.getId()) {//防止重复加载数据
            return;
        }
        mCurrentTabId = v.getId();
        switchBottomTag(mCurrentTabId);
        if (mCurrentTabId == R.id.home_main) {
            switchContent(mCurrentFragment, mMainFragment);
            return;
        }

        if (mCurrentTabId == R.id.home_tribe) {
            switchContent(mCurrentFragment, mTribeFragment);
            return;
        }


        if (mCurrentTabId == R.id.home_message) {
            switchContent(mCurrentFragment, mMessageFragment);
            return;
        }

        if (mCurrentTabId == R.id.home_me) {
            mMeFragment.onResume();
            switchContent(mCurrentFragment, mMeFragment);
        }
    }


    private void startTakePhoto() {
        //todo
        boolean isCanComment = PreferenceUtil.getBoolean(this, AppPrefs.FILE_CONFIG, AppPrefs.KEY_CAN_UPLOAD_VIDEO, false);
        if (isCanComment) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofVideo())
                    .compress(true)
                    .recordVideoSecond(30)
                    .videoMaxSecond(30)
                    .maxSelectNum(1)
                    .isGif(false)
                    .forResult(1001);
        } else {
            ToastUtil.show(getString(R.string.no_permission));
        }
    }

    private void uploadVideo(String path) {
        if (TextUtils.isEmpty(path)) {
            ToastUtil.show("上传失败，请重新尝试");
            return;
        }
        showloading();
        ServerApi.uploadVideo(AccountManager.INSTANCE.getUserToken(), new File(path), "未命名",
                new AutoLoginOutCallback<BaseResult>() {
                    @Override
                    public void onResponse(BaseResult response, int id) {
                        super.onResponse(response, id);
                        dissLoading();
                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            ToastUtil.show(R.string.video_upload_success);
                        } else {
                            ToastUtil.show(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        dissLoading();
                        ToastUtil.show("上传失败，请重新尝试");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String video = selectList.get(0).getPath();
            if (!TextUtils.isEmpty(video) && video.startsWith("content")) {
                uploadVideo(FileUtil.getPath(this, Uri.parse(video)));
            } else {
                uploadVideo(video);
            }

        }

        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            if (mCurrentFragment instanceof HomeMainFragment) {
                ((HomeMainFragment) mCurrentFragment).pauseVideoPlayer();
            }

            if (to instanceof HomeMainFragment) {
                ((HomeMainFragment) to).resumeVideoPlayer();
            }

            mCurrentFragment = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (from.isAdded()) {
                transaction.hide(from);
            }

            if (!to.isAdded()) {
                transaction.add(R.id.home_content, to).commit();
            } else {
                transaction.show(to).commit();
            }
        }
    }


    private void switchBottomTag(int tabId) {
        if (tabId == R.id.home_main) {
            mMainImage.setImageResource(ENABLE_TAB_IMAGE_RESID[0]);
            mTribeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[1]);
            mMessageImage.setImageResource(DISABLE_TAB_IMAGE_RESID[2]);
            mMeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[3]);

            mMainTitle.setTextColor(getResources().getColor(R.color.color_app_main));
            mTribeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMessageTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));

            return;
        }


        if (tabId == R.id.home_tribe) {
            mMainImage.setImageResource(DISABLE_TAB_IMAGE_RESID[0]);
            mTribeImage.setImageResource(ENABLE_TAB_IMAGE_RESID[1]);
            mMessageImage.setImageResource(DISABLE_TAB_IMAGE_RESID[2]);
            mMeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[3]);

            mMainTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mTribeTitle.setTextColor(getResources().getColor(R.color.color_app_main));
            mMessageTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));

            return;
        }

        if (tabId == R.id.home_message) {
            mMainImage.setImageResource(DISABLE_TAB_IMAGE_RESID[0]);
            mTribeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[1]);
            mMessageImage.setImageResource(ENABLE_TAB_IMAGE_RESID[2]);
            mMeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[3]);

            mMainTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mTribeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMessageTitle.setTextColor(getResources().getColor(R.color.color_app_main));
            mMeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));

            return;
        }


        if (tabId == R.id.home_me) {
            mMainImage.setImageResource(DISABLE_TAB_IMAGE_RESID[0]);
            mTribeImage.setImageResource(DISABLE_TAB_IMAGE_RESID[1]);
            mMessageImage.setImageResource(DISABLE_TAB_IMAGE_RESID[2]);
            mMeImage.setImageResource(ENABLE_TAB_IMAGE_RESID[3]);

            mMainTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMessageTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mTribeTitle.setTextColor(getResources().getColor(R.color.color_text_disable));
            mMeTitle.setTextColor(getResources().getColor(R.color.color_app_main));
        }

    }
}
