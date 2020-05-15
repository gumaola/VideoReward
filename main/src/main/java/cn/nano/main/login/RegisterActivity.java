package cn.nano.main.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.home.HomeActivity;
import cn.nano.main.person.CertificationActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.view.EditCustomView;
import okhttp3.Call;

public class RegisterActivity extends CommonActivity implements View.OnClickListener {

    private EditText phoneEditor;
    private EditText pswEditor;
    private EditText confirmPswEditor;
    private EditText inviteCodeEditor;

    //统一跳转
    public static void forward(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        findViewById(R.id.register_back).setOnClickListener(this);
        findViewById(R.id.register_now).setOnClickListener(this);

        phoneEditor = ((EditCustomView) findViewById(R.id.register_phone)).getEditor();
        pswEditor = ((EditCustomView) findViewById(R.id.register_password)).getEditor();
        confirmPswEditor = ((EditCustomView) findViewById(R.id.register_confirm_password)).getEditor();
        inviteCodeEditor = ((EditCustomView) findViewById(R.id.register_invite_code)).getEditor();

        phoneEditor.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pswEditor.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPswEditor.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
    }


    private void register() {
        String phone = phoneEditor.getText().toString();
        String psw = pswEditor.getText().toString();
        String confirmPsw = confirmPswEditor.getText().toString();
        String inviteCode = inviteCodeEditor.getText().toString();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(psw) || TextUtils.isEmpty(confirmPsw) || TextUtils.isEmpty(inviteCode)) {
            ToastUtil.show(R.string.common_info_error);
            return;
        }

        showloading();
        ServerApi.register(phone.trim(), psw, confirmPsw, inviteCode.trim(), new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                dissLoading();
                if (response != null) {
                    if (response.getCode() == BaseResult.SUCCESS_CODE) {
                        onRegisterSuccess();
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
                dissLoading();
                ToastUtil.show(R.string.common_error);
            }
        });
    }


    private void onRegisterSuccess() {
        ToastUtil.show(R.string.register_successful);
        CertificationActivity.forward(this);
        finish();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CertificationActivity.CERTIFICATION_REQUEST_CODE && resultCode == RESULT_OK) {
//            ToastUtil.show(R.string.register_successful);
//            finish();
//        }
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (R.id.register_now == id) {
            register();
            return;
        }

        if (R.id.register_back == id) {
            finish();
        }
    }
}
