package cn.nano.common.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.UUID;

/**
 * Created by sxl4287 on 16/2/24.
 * Email : sxl4287@arcsoft.com
 */
public class SystemUtil {
    /**
     * 更新app
     *
     * @param mContext 上下文
     */
    public static void showRateDialog(final Context mContext) {
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());

        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            List<ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(goToMarket, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfos != null && resolveInfos.size() > 0) {
                mContext.startActivity(goToMarket);
            }
        } catch (ActivityNotFoundException e) {
//            ToastManager.getInstance().showToast(mContext.getString(R.string.setting_activity_rate_app_failed));
        }
    }

    /**
     * 检查某个是否安装了某个包
     *
     * @param packageName 包名
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, 0);
            return null != info;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

//	public static String getGoogleAdId(Context context) {
//		AdvertisingIdClient.Info adInfo = null;
//		try {
//			adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		String id = "";
//		if (adInfo != null) {
//			id = adInfo.getId();
//			LogUtil.logE("google", "google ad id = " + id);
//		}
//
//		return id;
//	}


    public static String getStoreEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {

        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }


    /**
     * 返回当前App的version
     */
    public static String getAppVersionName(Context context) {
        String currentVersion = "1.0.0";
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                currentVersion = pi.versionName == null ? "1.0.0"
                        : pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return currentVersion;
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p/>
     * 渠道标志为： 1，andriod（a）
     * <p/>
     * 识别符来源标志： 1， wifi mac地址（wifi）； 2， IMEI（imei）； 3， 序列号（sn）； 4，
     * id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("d");

        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // wifi mac地址
                String wifiMac = NetworkUtil.getMacAddress();
                if (!TextUtils.isEmpty(wifiMac)) {
                    deviceId.append("wifi").append(wifiMac);
                    return deviceId.toString();
                }
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); // IMEI（imei）
                if (tm != null) {
                    String imei;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        imei = tm.getImei();
                    } else {
                        imei = tm.getDeviceId();
                    }
                    if (!TextUtils.isEmpty(imei)) {
                        deviceId.append("imei").append(imei);
                        return deviceId.toString();
                    }
                }

                // 序列号（sn）
                if (tm != null) {
                    String sn = tm.getSimSerialNumber();
                    if (!TextUtils.isEmpty(sn)) {
                        deviceId.append("sn").append(sn);
                        return deviceId.toString();
                    }
                }
            }

            //优先获取串列号，如果失败获取androidID
            String uuid = Build.SERIAL;
            if (TextUtils.equals(uuid, Build.UNKNOWN) || TextUtils.isEmpty(uuid)) {
                uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id").append(uuid);
                return deviceId.toString();
            } else {
                deviceId.append("id").append(UUID.randomUUID().toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            return deviceId.toString();
        }

    }

    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }


    /**
     * 判断当前app时候置于前台 activityStack中，主要是用户按了Home按键
     */
    public static boolean checkAppExistForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        if (null == context)
            return false;
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context
                .ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

//	/**
//	 * Check the device to make sure it has the Google Play Services APK. If
//	 * it doesn't, display a dialog that allows users to download the APK from
//	 * the Google Play Store or enable it in the device's system settings.
//	 */
//	public static boolean checkPlayServices(Activity activity, int requestCode) {
//		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
////        GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) is deprecated.
//		int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (apiAvailability.isUserResolvableError(resultCode)) {
//				apiAvailability.getErrorDialog(activity, resultCode, requestCode).show();
//			}
//			return false;
//		}
//		return true;
//	}
//
//	public static boolean isPlayServicesAvailable(Context context) {
//		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//		int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
//		return resultCode == ConnectionResult.SUCCESS;
//	}
}
