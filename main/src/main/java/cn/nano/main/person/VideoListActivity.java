package cn.nano.main.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.okhttp.utils.L;
import cn.nano.common.snappager.OnPagerListener;
import cn.nano.common.snappager.PagerLayoutManager;
import cn.nano.common.utils.GsonUtil;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.AppPrefs;
import cn.nano.main.constant.IntentConstant;
import cn.nano.main.home.adapter.VideoHoldClicker;
import cn.nano.main.home.adapter.VideoPlayerAdapter;
import cn.nano.main.home.adapter.VideoPlayerViewHold;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.VideoInfo;
import cn.nano.main.server.result.VideoListResult;
import okhttp3.Call;

public class VideoListActivity extends CommonActivity implements VideoHoldClicker {

    private static final String BUNDLE_EXTRA = "bundle_extra";
    public static final String VIDEO_INFO = "video_info";

    private RecyclerView videoRecycler;
    private VideoPlayerAdapter videoPlayerAdapter;

    //统一跳转
    public static void forward(Context context, Bundle bundle) {
        Intent intent = new Intent(context, VideoListActivity.class);
        intent.putExtra(BUNDLE_EXTRA, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        videoRecycler = findViewById(R.id.video_list_recycler);
        initRecycler();
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra(BUNDLE_EXTRA);
        if (bundle != null) {
            String videoInfo = bundle.getString(VIDEO_INFO);
            if (!TextUtils.isEmpty(videoInfo)) {
                VideoInfo info = null;
                try {
                    info = GsonUtil.createGson().fromJson(videoInfo, VideoInfo.class);
                } catch (Exception e) {
                    info = null;
                }

                if (info != null) {
                    VideoListResult.DataBean.VlistBean bean = new VideoListResult.DataBean.VlistBean();
                    bean.setVInfo(info);

                    List<VideoListResult.DataBean.VlistBean> list = new ArrayList<>();
                    list.add(bean);
                    videoPlayerAdapter.bindVideoList(list);
                }
            }
        }
    }

    public void goback(View v) {
        finish();
    }


    private void initRecycler() {
        PagerLayoutManager viewPagerLayoutManager = new PagerLayoutManager(this, OrientationHelper.VERTICAL);

        videoPlayerAdapter = new VideoPlayerAdapter();
        videoPlayerAdapter.setVideoClicker(this);
        videoRecycler.setAdapter(videoPlayerAdapter);
        videoRecycler.setLayoutManager(viewPagerLayoutManager);

        viewPagerLayoutManager.setOnViewPagerListener(new OnPagerListener() {
            @Override
            public void onInitComplete() {
                L.e("comm");
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                L.e("release");
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                L.e("pos");
            }
        });

        videoRecycler.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                VideoPlayer videoPlayer = ((VideoPlayerViewHold) holder).mVideoPlayer;
                if (videoPlayer == VideoPlayerManager.instance().getCurrentVideoPlayer()) {
                    VideoPlayerManager.instance().releaseVideoPlayer();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.instance().resumeVideoPlayer();
    }


    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.instance().suspendVideoPlayer();
    }

    @Override
    public void onClick(View v, VideoListResult.DataBean.VlistBean videoBean, VideoPlayerViewHold viewHold) {
        if (videoBean == null) {
            return;
        }

        int id = v.getId();

        //跳转到个人信息页面
        if (id == R.id.video_user_thumb) {
            VideoListResult.DataBean.VlistBean.MInfoBean member = videoBean.getMInfo();
            Bundle bundle = new Bundle();
            bundle.putInt(IntentConstant.MEMBER_ID, member.getMember_id());
            PersonInfoActivity.forward(this, bundle);
            return;
        }

        //开始关注用户
        if (id == R.id.video_follow_user) {
            followUserOrVideo(videoBean.getMInfo().getMember_id(), false, videoBean, viewHold);
            return;
        }

        //开始收藏video
        if (id == R.id.video_fav_icon) {
            followUserOrVideo(videoBean.getVInfo().getVideo_id(), true, videoBean, viewHold);
            return;
        }


        //评论video
        if (id == R.id.video_comment_icon) {
            boolean isCanComment = PreferenceUtil.getBoolean(this, AppPrefs.FILE_CONFIG, AppPrefs.KEY_CANCOMMENT, false);
            if (isCanComment) {
                ToastUtil.show("评论功能尚在开发");
            } else {
                ToastUtil.show(getString(R.string.no_permission));
            }

            return;
        }

        //分享
        if (id == R.id.video_share_icon) {
            //todo
            ToastUtil.show("分享功能尚在开发");
        }
    }


    private void followUserOrVideo(int id, final boolean isVideo,
                                   final VideoListResult.DataBean.VlistBean bean, final VideoPlayerViewHold viewHold) {
        showloading();
        String token = AccountManager.INSTANCE.getUserToken();
        ServerApi.follow(token, isVideo ? 0 : 1, id, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                dissLoading();
                ToastUtil.show(R.string.common_error);
            }

            @Override
            public void onResponse(BaseResult response, int id) {
                dissLoading();
                if (response.getCode() != BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(response.getMsg());
                    return;
                }


                if (isVideo) {
                    boolean isFollow = bean.getVInfo().getIs_follow() == 1;

                    if (!isFollow) {
                        bean.getVInfo().setIs_follow(1);
                        int count = bean.getVInfo().getSupport_num();
                        bean.getVInfo().setSupport_num(count + 1);
                        viewHold.getFavorIcon().setImageResource(R.mipmap.ico_favor_enable);
                        viewHold.getFavorCount().setText(String.valueOf(count + 1));
                    } else {
                        bean.getVInfo().setIs_follow(2);
                        int count = bean.getVInfo().getSupport_num();
                        bean.getVInfo().setSupport_num(count - 1);
                        viewHold.getFavorCount().setText(String.valueOf(count - 1));
                        viewHold.getFavorIcon().setImageResource(R.mipmap.ico_favor_disable);
                    }


                } else {
                    boolean isFollow = bean.getMInfo().getIs_follow() == 1;
                    bean.getMInfo().setIs_follow(isFollow ? 2 : 1);
                    viewHold.getFollowIcon().setText(isFollow ? "+" : "√");
                }
            }
        });
    }
}
