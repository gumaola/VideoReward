package cn.nano.main.person;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.glide.ImgLoader;
import cn.nano.common.okhttp.utils.L;
import cn.nano.common.regionselector.AddressSelectorActivity;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.login.LoginActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.MemberInfo;
import cn.nano.main.view.GenderSelectorDialog;
import okhttp3.Call;

public class PersonInfoEditActivity extends CommonActivity implements View.OnClickListener, OnDateSetListener {
    //统一跳转
    public static void forward(Context context) {
        Intent intent = new Intent(context, PersonInfoEditActivity.class);
        context.startActivity(intent);
    }

    private ImageView avatarV;
    private TextView nameV;
    private TextView idV;
    private TextView descV;
    private TextView genderV;
    private TextView birthDayV;
    private TextView locationV;

    //各个数据更改后的值
    private String avatarPath;
    private String modifyName;
    private String modifyDesc;
    private String modifyGender;
    private String modifyBrithDay;
    private String modifyAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        findViewById(R.id.edit_user_camera).setOnClickListener(this);
        findViewById(R.id.edit_user_name_root).setOnClickListener(this);
        findViewById(R.id.edit_user_desc_root).setOnClickListener(this);
        findViewById(R.id.edit_user_gender_root).setOnClickListener(this);
        findViewById(R.id.edit_user_birth_root).setOnClickListener(this);
        findViewById(R.id.edit_user_location_root).setOnClickListener(this);
        findViewById(R.id.edit_back).setOnClickListener(this);

        avatarV = findViewById(R.id.edit_user_avatar);
        nameV = findViewById(R.id.edit_user_name);
        idV = findViewById(R.id.edit_user_id);
        descV = findViewById(R.id.edit_user_desc);
        genderV = findViewById(R.id.edit_user_gender);
        locationV = findViewById(R.id.edit_user_location);
        birthDayV = findViewById(R.id.edit_user_birthr);

        MemberInfo info = AccountManager.INSTANCE.loadLocalUserInfo();
        if (info != null) {
            ImgLoader.displayWithError(this, info.getMember_avatar(), avatarV, R.mipmap.icon_avatar_placeholder);
            nameV.setText(info.getNick_name());
            idV.setText(String.valueOf(info.getMember_id()));
            descV.setText(info.getMember_autograph());

            int genderIndex = info.getMember_sex();
            String gender = "保密";
            if (genderIndex == 1) {
                gender = getString(R.string.male);
            } else if (genderIndex == 2) {
                gender = getString(R.string.female);
            }
            genderV.setText(gender);
            locationV.setText(info.getMember_city());
            birthDayV.setText(info.getMember_birthday());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.edit_back) {
            postAndFinish();
            return;
        }

        if (id == R.id.edit_user_camera) {
            takePhoto();
            return;
        }

        if (id == R.id.edit_user_name_root) {
            Bundle bundle = new Bundle();
            bundle.putString(ModifyInfoActivity.MODIFY_TYPE, "name");

            ModifyInfoActivity.forward(this, 1000, bundle);
            return;
        }

        if (id == R.id.edit_user_desc_root) {
            Bundle bundle = new Bundle();
            bundle.putString(ModifyInfoActivity.MODIFY_TYPE, "desc");

            ModifyInfoActivity.forward(this, 1000, bundle);
            return;
        }

        if (id == R.id.edit_user_gender_root) {
            showGenderSelectDialog();
            return;
        }

        if (id == R.id.edit_user_birth_root) {
            showBrithdaySelect();
            return;
        }

        if (id == R.id.edit_user_location_root) {
            AddressSelectorActivity.forward(this);
            return;
        }
    }


    private Dialog genderDialog;

    private void showGenderSelectDialog() {
        if (genderDialog == null) {
            genderDialog = GenderSelectorDialog.create(this, new GenderSelectorDialog.GenderSelectorWatch() {
                @Override
                public void onSelect(String gender) {
                    genderDialog.dismiss();
                    genderV.setText(gender);
                    modifyGender = gender;
                }
            });
        }

        genderDialog.show();
    }

    private void showBrithdaySelect() {
        long currentTime = System.currentTimeMillis();
        long minTime = currentTime - (365L * 24 * 60 * 60 * 50000);

        TimePickerDialog dialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setMinMillseconds(minTime)
                .setMaxMillseconds(currentTime)
                .setCurrentMillseconds(757432444000L)//1994-01-01
                .setThemeColor(getResources().getColor(R.color.color_dark))
                .setCyclic(false)
                .setTitleStringId("出生日期")
                .setCallBack(this)
                .build();
        dialogYearMonthDay.show(getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//精确到分钟

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date date = new Date(millseconds);
        String time = format.format(date);
        birthDayV.setText(time);
        modifyBrithDay = time;
    }

    @Override
    public void onBackPressed() {
        postAndFinish();
    }

    private void postAndFinish() {

        if (TextUtils.isEmpty(avatarPath)
                && TextUtils.isEmpty(modifyName)
                && TextUtils.isEmpty(modifyDesc)
                && TextUtils.isEmpty(modifyGender)
                && TextUtils.isEmpty(modifyBrithDay)
                && TextUtils.isEmpty(modifyAddress)) {
            finish();
            return;
        }

        String gender = null;
        if (!TextUtils.isEmpty(modifyGender)) {
            if (modifyGender.equalsIgnoreCase(getString(R.string.male))) {
                gender = "1";
            } else if (modifyGender.equalsIgnoreCase(getString(R.string.female))) {
                gender = "2";
            } else {
                gender = "0";
            }
        }

        File avatarFile = null;
        if (!TextUtils.isEmpty(avatarPath)) {
            avatarFile = new File(avatarPath);
        }

        showloading();
        ServerApi.modifyUserInfo(AccountManager.INSTANCE.getUserToken(), modifyName, modifyBrithDay, modifyDesc, gender, avatarFile, modifyAddress, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dissLoading();
                ToastUtil.show("更新信息失败");
                finish();
            }

            @Override
            public void onResponse(BaseResult response, int id) {
                dissLoading();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    finish();
                    return;
                }

                ToastUtil.show(response.getMsg());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            if (data != null) {
                String type = data.getStringExtra(ModifyInfoActivity.MODIFY_TYPE);
                String result = data.getStringExtra(ModifyInfoActivity.RESULT_DATA);

                if ("name".equalsIgnoreCase(type)) {
                    modifyName = result;
                    nameV.setText(result);
                } else if ("desc".equalsIgnoreCase(type)) {
                    modifyDesc = result;
                    descV.setText(result);
                }
            }

            return;
        }


        if (requestCode == AddressSelectorActivity.REQUEST_ADDRESS_CODE && resultCode == RESULT_OK) {
            String address = data.getStringExtra(AddressSelectorActivity.KEY_ADDRESS_RESULT);
            modifyAddress = address;
            locationV.setText(address);
            return;
        }


        if (requestCode == 1001 && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String photo = selectList.get(0).getCompressPath();
            avatarPath = photo;
            ImgLoader.display(this, new File(photo), avatarV);
        }
    }

    private void takePhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .maxSelectNum(1)
                .isGif(false)
                .forResult(1001);
    }

}
