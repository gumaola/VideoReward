package cn.nano.app.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import cn.nano.app.R;
import cn.nano.common.app.CommonActivity;
import cn.nano.common.app.GlobalConfig;
import cn.nano.main.account.AccountManager;
import cn.nano.main.home.HomeActivity;
import cn.nano.main.login.LoginActivity;


public class LaunchActivity extends CommonActivity {

    private IntentHandler mHandle;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    //统一跳转
    public static void forward() {
        Intent intent = new Intent(GlobalConfig.getInstance().currentApp(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalConfig.getInstance().currentApp().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mHandle = new IntentHandler();

        //先申请权限
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_STORAGE, 1000);
    }

    @Override
    protected void onPermissionGranted() {
        if (AccountManager.INSTANCE.isLogin()) {
            mHandle.sendEmptyMessageDelayed(IntentHandler.GO_TO_MAIN, 2000);
        } else {
            mHandle.sendEmptyMessageDelayed(IntentHandler.GO_TO_LOGIN, 2000);
        }
    }

    private class IntentHandler extends Handler {

        private static final int GO_TO_LOGIN = 0x1000;
        private static final int GO_TO_MAIN = 0x2000;
        private static final int SHOW_SPLASH = 0x3000;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_TO_LOGIN:
                    LoginActivity.forward();
                    finish();
                    break;
                case GO_TO_MAIN:
                    HomeActivity.forward();
                    finish();
                    break;
                case SHOW_SPLASH:

                    break;
                default:
                    break;
            }
        }
    }
}
