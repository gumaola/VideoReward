package cn.nano.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.Nullable;


import cn.nano.common.app.CommonActivity;
import cn.nano.common.app.GlobalConfig;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.home.HomeActivity;
import cn.nano.main.person.CertificationActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.LoginResult;
import cn.nano.main.view.EditCustomView;
import okhttp3.Call;

public class LoginActivity extends CommonActivity implements View.OnClickListener {

    private EditText phoneEditor;
    private EditText pswEditor;

    //统一跳转
    public static void forward() {
        Intent intent = new Intent(GlobalConfig.getInstance().currentApp(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalConfig.getInstance().currentApp().startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {
        findViewById(R.id.login_forget_password).setOnClickListener(this);
        findViewById(R.id.login_login_now).setOnClickListener(this);
        findViewById(R.id.login_register).setOnClickListener(this);

        phoneEditor = ((EditCustomView) findViewById(R.id.login_phone)).getEditor();
        pswEditor = ((EditCustomView) findViewById(R.id.login_password)).getEditor();

        phoneEditor.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pswEditor.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);

    }


    private void forgetPassword() {
        ForgetPasswordActivity.forward(this);
    }

    private void loginNow() {
        String phone = phoneEditor.getText().toString();
        String psw = pswEditor.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(R.string.register_phone_error);
            return;
        }

        if (TextUtils.isEmpty(psw)) {
            ToastUtil.show(R.string.register_psw_error);
            return;
        }
        showloading();
        ServerApi.login(phone.trim(), psw.trim(), new AutoLoginOutCallback<LoginResult>() {
            @Override
            public void onResponse(LoginResult response, int id) {
                super.onResponse(response, id);
                dissLoading();
                if (response != null) {
                    if (response.getCode() == BaseResult.SUCCESS_CODE) {
                        boolean isCerted = response.getData().getIsIdentity() == 1;
                        String token = response.getData().getMtoken();
                        AccountManager.INSTANCE.syncUserToken(token);
                        AccountManager.INSTANCE.syncUserCertState(isCerted);

                        if (isCerted) {
                            HomeActivity.forward();
                            finish();
                        } else {
                            ToastUtil.show(R.string.certificate_tip);
                            CertificationActivity.forward(LoginActivity.this);
                        }
                    } else {
                        String msg = response.getMsg();
                        ToastUtil.show(msg);
                    }
                } else {
                    ToastUtil.show(R.string.common_error);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CertificationActivity.CERTIFICATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                HomeActivity.forward();
                finish();
            } else {
                ToastUtil.show("必须实名认证才可登陆");
            }
        }
    }

    private void registerNow() {
        RegisterActivity.forward(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.login_forget_password == v.getId()) {
            forgetPassword();
            return;
        }


        if (R.id.login_login_now == v.getId()) {
            loginNow();
            return;
        }

        if (R.id.login_register == v.getId()) {
            registerNow();
        }
    }
}
