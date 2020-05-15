package cn.nano.common.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Created by wjj3771 on 2016/8/30.
 */
public class PackageUtil {
    public static void componentEnabledSetting(Context context, Class<?>[] cls, int newState, int flags) {
        PackageManager pm = context.getPackageManager();
        for (int i = 0; i < cls.length; i++) {
            ComponentName name = new ComponentName(context, cls[i]);
            pm.setComponentEnabledSetting(name, newState, flags);
        }
    }

    public static void componentEnabledSettingWithNoKill(Context context, Class<?>[] cls, int newState) {
        if (cls == null || cls.length <= 0) return;
        PackageManager pm = context.getPackageManager();
        int lastTimeState = pm.getComponentEnabledSetting(new ComponentName(context, cls[0]));
        if (lastTimeState == newState) return;
        componentEnabledSetting(context, cls, newState, PackageManager.DONT_KILL_APP);
    }

    public static String getCurrentProcessName(Context context) {
        String processName = null;
        int curPid = android.os.Process.myPid();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + curPid + "/cmdline"));
            processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
        } catch (Exception e) {
            processName = null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(processName)) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                List<ActivityManager.RunningAppProcessInfo> runningApps;
                runningApps = activityManager.getRunningAppProcesses();
                if (runningApps != null) {
                    for (ActivityManager.RunningAppProcessInfo appProcess : runningApps) {
                        if (appProcess.pid == curPid) {
                            processName = appProcess.processName;
                            break;
                        }
                    }
                }
            }
        }


        return processName;
    }
}
