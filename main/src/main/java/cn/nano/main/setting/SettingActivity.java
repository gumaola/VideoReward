package cn.nano.main.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.BuildConfig;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.IntentConstant;
import cn.nano.main.login.LoginActivity;
import cn.nano.main.person.MemberListActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.MemberInfo;
import cn.nano.main.view.WithdrawModifyPswDialog;
import okhttp3.Call;

public class SettingActivity extends CommonActivity {

    private Dialog withdrawModifyDialog;

    //统一跳转
    public static void forward(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView version = findViewById(R.id.setting_version);
        version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
    }

    public void goback(View v) {
        finish();
    }

    public void goToInvite(View v) {
        MemberInfo info = AccountManager.INSTANCE.loadLocalUserInfo();
        Bundle bundle = new Bundle();
        if (info != null) {
            String url = info.getPromo_img();
            bundle.putString(IntentConstant.URL, url);
        }


        InviteActivity.forward(this, bundle);
    }

    public void goToMyInvite(View v) {
        MemberListActivity.forward(this, MemberListActivity.inviters);
    }

    public void modifyPsw(View v) {
        ToastUtil.show("修改密码尚在开发");
    }

    public void modifyBillingPsw(View v) {
        if (withdrawModifyDialog == null) {
            withdrawModifyDialog = WithdrawModifyPswDialog.create(this, new WithdrawModifyPswDialog.WithdrawModifyCallback() {
                @Override
                public void onYesClick(String psw, String confirm) {
                    withdrawModifyDialog.dismiss();
                    if (TextUtils.isEmpty(psw) || psw.length() < 6) {
                        ToastUtil.show("请输入正确6位密码");
                        return;
                    }

                    if (TextUtils.isEmpty(confirm) || confirm.length() < 6) {
                        ToastUtil.show("确认密码格式不正确");
                        return;
                    }

                    if (!psw.equalsIgnoreCase(confirm)) {
                        ToastUtil.show("两次密码不匹配");
                        return;
                    }

                    modifyWithdrawPsw(psw, confirm);
                }
            });
        } else {
            WithdrawModifyPswDialog.reset(withdrawModifyDialog);
        }

        withdrawModifyDialog.show();
    }

    private void modifyWithdrawPsw(String psw, String confirm) {
        showloading();

        ServerApi.modifyWithdrawPsw(AccountManager.INSTANCE.getUserToken(), psw, confirm, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                dissLoading();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(R.string.modify_psw_success);
                } else {
                    ToastUtil.show(response.getMsg());
                }
            }


            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                dissLoading();
                ToastUtil.show(R.string.common_error);
            }
        });
    }


    public void loginOut(View v) {
        AccountManager.INSTANCE.clear(this);
        LoginActivity.forward();
        finish();
    }
}
