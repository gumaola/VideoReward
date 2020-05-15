package cn.nano.app;

import androidx.multidex.MultiDexApplication;

import cn.nano.common.app.GlobalConfig;
import cn.nano.common.utils.SystemUtil;
import cn.nano.getui.GetuiSDK;

public class VideoRewardApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = SystemUtil.getCurrentProcessName(this);
        if (processName != null && !processName.equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
            return;
        }

        //初始化config
        GlobalConfig.getInstance().init(this);

        //初始化push
        GetuiSDK.initSDK(this);
    }
}
