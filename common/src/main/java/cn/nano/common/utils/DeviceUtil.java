package cn.nano.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by sxl4287 on 16/2/25.
 * Email : sxl4287@arcsoft.com
 */
public class DeviceUtil {
    //当前设备的等级，P365自定义的；
    private static String sDeviceType = null;//当前设备的等级，高，中，低端，根据CPU核数，以及最大频数
    //屏幕宽、高、dpi 相关
    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;
    private static float sScreenDensityScale = 1.0f;
    private static float sScreenDensityDpi = 240f;
    private static int sScreenRealHeight = -1;

    public static void checkScreenInfo(Context context) {
        if (sScreenWidth != -1 || context == null)
            return;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        sScreenWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
        sScreenHeight = Math.max(metrics.widthPixels, metrics.heightPixels);
        sScreenDensityScale = metrics.scaledDensity;
        sScreenDensityDpi = metrics.densityDpi;
        sScreenRealHeight = getScreenRealHeight(context);
    }

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static float getScreenDensityScale() {
        return sScreenDensityScale;
    }

    public static float getScreenDensityDpi() {
        return sScreenDensityDpi;
    }

    public static int getScreenRealHeight() {
        return sScreenRealHeight;
    }

    private static int getScreenRealHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 字符之间的间隔，与一般设备不一样，特别适配
     *
     * @return
     */
    public static boolean isSharp() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "sh8298u");
    }

    /**
     * 为了邮件没有账号登陆，直接跳转到欢迎界面（登陆界面）
     *
     * @return
     */
    public static boolean isCoolPad() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "coolpad");
    }

    public static boolean isMtolorM778() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith("mt788");
    }

    /**
     * 为了无人脸的邮件发送后，不显示无人脸提示框
     *
     * @return
     */
    public static boolean isMtolorM525() {
        return Build.MODEL.toLowerCase(Locale.getDefault())
                .startsWith("me525+");
    }

    /**
     * 为了无人脸的邮件发送后，不显示无人脸提示框
     *
     * @return
     */
    public static boolean isZTEu985() {// 特殊设备，图片的exif信息中thumbnail有些角度旋转问题,导致显示不全
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "zte u985");
    }

    public static boolean isZTEu950() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "zte u950");
    }

    public static boolean isHTC_X920() {// 特殊设备，camera
        // takepicture后直接restartPreview
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc x920e");
    }

    public static boolean isHTC_ONE_801E() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc one 801e");
    }

    // HTC Sensation XE with Beats Audio Z715e
    public static boolean isHTC_Sensation() {// G18特殊设备，camera拍照完，exif的旋转信息，在前置摄像头竖屏模式下，颠倒了
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc sensation");
    }

    // HUAWEI D2-2010 //HUAWEI MT1-U06
    public static boolean isHUAWEI() {// 特殊设备，camera拍照完，exif的旋转信息，在前置摄像头竖屏模式下，颠倒了
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "huawei d2-2010");
    }

    // LG optiums G Pro
    public static boolean isLG_optiums() {// 特殊设备，图片的exif信息中thumbnail有些角度旋转问题,导致显示不全
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "lg-f240l");
    }

    // GT-I9000
    public static boolean isGTI9000() {// cameraFixed preview 显示有问题
        // MakeupApp.MeirenLog("sxl",
        // "Build.MODEL.toLowerCase==" + Build.MODEL.toLowerCase());
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "gt-i9000");
    }

    // HTC_X515
    public static boolean isHTC_X515() {// G17 有邮件发送程序，单不是官方email
        // MakeupApp.MeirenLog("sxl",
        // "Build.MODEL.toLowerCase==" + Build.MODEL.toLowerCase());
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc x515d");
    }

    // HTC_ONE_X
    public static boolean isHTC_ONE_X() {// one_x

        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc s720e")
                || Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc one x");
    }

    // HTC_ONE_m7 ,4连拍的时候，绿屏
    public static boolean isHTC_ONE_M7() {// one_x
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc one 801e");
    }

    public static boolean isHTC_Incredible() {// G11 htc incredible
        // 设备照片有问题，UI显示有问题
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc incredible");
    }

    public static boolean isHTC_X920E() {
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc x920e");
    }

    public static boolean isXIAO_MI1S() {// xiaomi 设备前置摄像头不能设置大尺寸，否则马赛克
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith("mi 1s");
    }

    public static boolean isGT_N7000() {// 后置摄像头尺寸跨度太大
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "gt-n7000");
    }

    public static boolean isHTC_t329t() {// 拍照crash
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "htc t329t");
    }

    // public static boolean isLenovo_A850() {// 这个设备的actionbar
    // // 有些问题，HomeButton不显示logo（也不能设置）,同时不能将title置为空，否则homeButton不显示
    // MakeupApp.MeirenLog("sxl",
    // "Build.MODEL.toLowerCase==" + Build.MODEL.toLowerCase());
    // return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
    // "lenovo a850");
    // }

    public static boolean isMotorola_xt682() {// 取景显示呗拉伸问题
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith("xt682");
    }

    public static boolean isGT_N7100() {// 前置摄像头第四声半声
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "gt-n7100");
    }

    public static boolean isMX2() {// 取景显示被拉伸问题，captureView显示图片旋转问题
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith("m040");
    }

    // 三星 S3
    public static boolean isS3() {// 前置摄像头第四声半声
        String deviceName = Build.MODEL.toLowerCase(Locale.getDefault());
        return deviceName.startsWith("gt-i9300")
                || deviceName.startsWith("gt-i9305");
    }

    // 三星 S4
    public static boolean isGTI9500() {// 前置摄像头第四声半声
        return Build.MODEL.toLowerCase(Locale.getDefault()).startsWith(
                "gt-i9500");
    }

    /**
     * @return
     */
    private static int getDeviceType() {
        int cpuNum = Runtime.getRuntime().availableProcessors();

        int cpuFre = 1000000;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            br = new BufferedReader(fr);
            String text = br.readLine();
            cpuFre = Integer.parseInt(text.trim());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        if ((cpuFre >= 1200000) && (cpuNum >= 4)) {
            return 2;
        } else if (cpuNum >= 4) {
            return 1;
        } else if ((cpuFre >= 1200000) && (cpuNum >= 2)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * check device cupinfo
     */
    public static String checkDeviceType() {
        if (TextUtils.isEmpty(sDeviceType)) {
            switch (DeviceUtil.getDeviceType()) {
                case 0:
                    sDeviceType = "A";
                    break;
                case 1:
                    sDeviceType = "B";
                    break;
                case 2:
                    sDeviceType = "C";
                    break;
                default:
                    sDeviceType = "C";
                    break;
            }
        }
        return sDeviceType;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
