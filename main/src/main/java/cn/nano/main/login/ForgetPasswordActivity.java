package cn.nano.main.login;

import android.content.Context;
import android.content.Intent;

import cn.nano.common.app.CommonActivity;

public class ForgetPasswordActivity extends CommonActivity {


    //统一跳转
    public static void forward(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }
}
