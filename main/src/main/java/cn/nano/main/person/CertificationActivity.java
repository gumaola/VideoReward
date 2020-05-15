package cn.nano.main.person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.regionselector.AddressSelectorActivity;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import okhttp3.Call;

public class CertificationActivity extends CommonActivity {

    private EditText nameV;
    private EditText IdCodeV;
    private TextView addressV;

    public static final int CERTIFICATION_REQUEST_CODE = 0x2000;


    //统一跳转
    public static void forward(Activity activity) {
        Intent intent = new Intent(activity, CertificationActivity.class);
        activity.startActivityForResult(intent, CERTIFICATION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        nameV = findViewById(R.id.cert_name_editor);
        IdCodeV = findViewById(R.id.cert_id_editor);
        addressV = findViewById(R.id.cert_adress_editor);
    }

    public void goback(View v) {
        finish();
    }

    public void goToAddressSelector(View v) {
        AddressSelectorActivity.forward(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddressSelectorActivity.REQUEST_ADDRESS_CODE && resultCode == RESULT_OK) {
            String address = data.getStringExtra(AddressSelectorActivity.KEY_ADDRESS_RESULT);
            addressV.setText(address);
        }
    }

    public void cert(View v) {
        String name = nameV.getText().toString();
        String idCode = IdCodeV.getText().toString();
        String address = addressV.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idCode) || TextUtils.isEmpty(address)) {
            ToastUtil.show("请输入正确实名信息！");
            return;
        }

        showloading();
        ServerApi.certificate(AccountManager.INSTANCE.getUserToken(), name, idCode, address, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                dissLoading();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    AccountManager.INSTANCE.syncUserCertState(true);
                    ToastUtil.show(R.string.certificate_success);
                    setResult(RESULT_OK);
                    finish();
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
}
