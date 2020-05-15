package cn.nano.common.utils;

import android.app.Application;


public class ToolsManager {
    public static void initTools(Application application, boolean bIsDebug) {
        LogUtil.setEnableDebug(bIsDebug);
        DeviceUtil.checkScreenInfo(application.getApplicationContext());
    }
}
