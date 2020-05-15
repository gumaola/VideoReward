package cn.nano.common.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import cn.nano.common.utils.CountryUtil;
import cn.nano.common.utils.LanguageUtil;
import cn.nano.common.utils.MD5Util;
import cn.nano.common.utils.SystemUtil;

/**
 * 伴随整个app生命周期，包含app全局信息
 */
public class GlobalConfig {
    private static GlobalConfig sConfigInstance;

    //当前app实例
    private Application application;

    //语言
    private String language;
    //设置中的国家代码
    private String localCountryCode;
    //网络商国家代码
    private String netCountryCode;

    //设备id
    private String deviceId;
    //设备系统版本
    private String osVersion;
    //设备屏幕信息
    private int screenWidth;
    private int screenHeight;


    private GlobalConfig() {

    }

    public static GlobalConfig getInstance() {
        if (sConfigInstance == null) {
            synchronized (GlobalConfig.class) {
                if (sConfigInstance == null) {
                    sConfigInstance = new GlobalConfig();
                }
            }
        }
        return sConfigInstance;
    }


    public void init(Application app) {
        application = app;
        app.registerActivityLifecycleCallbacks(LifeCyclerObserver.getObserver());

        language = LanguageUtil.getLocaleLanguage();
        localCountryCode = CountryUtil.getCountryFromLocale();
        netCountryCode = CountryUtil.getCountryFromNetwork(app);
        deviceId = MD5Util.getMD5String(SystemUtil.getDeviceId(app));
        osVersion = android.os.Build.VERSION.RELEASE;

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        screenWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
        screenHeight = Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    public Application currentApp() {
        return application;
    }


    public String getLanguage() {
        return language;
    }

    public String getLocalCountryCode() {
        return localCountryCode;
    }

    public String getNetCountryCode() {
        return netCountryCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
