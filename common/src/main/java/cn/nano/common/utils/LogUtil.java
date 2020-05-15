package cn.nano.common.utils;

import android.text.TextUtils;
import android.util.Log;

import cn.nano.common.BuildConfig;

public class LogUtil {
    /**
     * 后期Release的时候，一定要将sIsSaveLog 值复制为false;否则用户手机上就会有关于购买记录的日志信息;
     */
    private static boolean sIsDebug = BuildConfig.DEBUG;
    private static final String NANO_TAG = "nano_";

    public static void setEnableDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    public static boolean isDebug() {
        return sIsDebug;
    }

    public static void logE(String TAG, String log) {
        log(TAG, log, Log.ERROR);
    }

    public static void logD(String TAG, String log) {
        log(TAG, log, Log.DEBUG);
    }

    public static void logW(String TAG, String log) {
        log(TAG, log, Log.WARN);
    }

    public static void logV(String TAG, String log) {
        log(TAG, log, Log.VERBOSE);
    }

    public static void logI(String TAG, String log) {
        log(TAG, log, Log.INFO);
    }

    public static void log(String tag, String content) {
        log(tag, content, Log.DEBUG);
    }


    public static void log(String tag, String... contents) {
        if (contents == null || contents.length <= 0) {
            return;
        }
        if (contents.length == 1) {
            log(tag, contents[0], Log.DEBUG);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < contents.length; i++) {
            stringBuilder.append(contents[i]).append(" ");
        }
        log(tag, stringBuilder.toString(), Log.DEBUG);
    }

    private static void log(String tag, String content, int level) {
        if (!sIsDebug || TextUtils.isEmpty(tag)) return;
        if (content == null) {
            content = "Log value  is null";
        }
        Log.println(level, tag, content);
    }


}
