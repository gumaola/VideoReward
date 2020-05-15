package cn.nano.umshare;

import android.content.Context;

import com.umeng.commonsdk.UMConfigure;

public class UMSdk {

    public static void initSdk(Context context) {
        //基础组件初始化
        UMConfigure.init(context, BuildConfig.UM_APP_KEY, "cn.nano.videoreward", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        //
    }
}
