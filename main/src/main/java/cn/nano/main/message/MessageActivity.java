package cn.nano.main.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.main.R;
import cn.nano.main.login.ForgetPasswordActivity;

public class MessageActivity extends CommonActivity {

    //统一跳转
    public static void forward(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }
}
