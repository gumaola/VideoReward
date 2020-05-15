package cn.nano.main.server;

import cn.nano.common.okhttp.callback.GsonCallback;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.login.LoginActivity;
import cn.nano.main.server.result.BaseResult;

public class AutoLoginOutCallback<T extends BaseResult> extends GsonCallback<T> {

    private static final int AUTO_LOGIN_OUT_CODE = 4;

    @Override
    public void onResponse(T response, int id) {
        if (response != null && response.getCode() == AUTO_LOGIN_OUT_CODE) {
            //todo login oout
            LoginActivity.forward();
        }
    }
}
