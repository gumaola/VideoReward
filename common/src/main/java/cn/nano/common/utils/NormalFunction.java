package cn.nano.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wjj3771 on 2016/10/21.
 */

public class NormalFunction {

    public static String getFormatedStrWithLan(String srcStr) {
        try {
            return String.format(Locale.ENGLISH,
                    srcStr,
                    LanguageUtil.getLocaleLanguage());
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFormatedStr(String srcStr, String suffix) {
        try {
            return String.format(Locale.ENGLISH,
                    srcStr,
                    suffix);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPrintStrFromList(List<String> strList) {
        String printStr = "";
        if (strList == null || strList.size() <= 0) {
            return printStr;
        }
        for (int i = 0; i < strList.size(); i++) {
            if (i != 0) {
                printStr = printStr + ",";
            }
            printStr = printStr + strList.get(i);
        }
        return "[" + printStr + "]";
    }

    public static String getPrintStrFromArray(String[] srcStr) {
        String printStr = "";
        if (srcStr == null || srcStr.length <= 0) {
            return printStr;
        }
        for (int i = 0; i < srcStr.length; i++) {
            if (i != 0) {
                printStr = printStr + ",";
            }
            printStr = printStr + srcStr[i];
        }
        return "[" + printStr + "]";
    }

    public static String getPrintStrFromMapStrList(Map<String, List<String>> srcStrMap) {
        String printStr = "";
        if (srcStrMap == null
                || srcStrMap.size() <= 0) {
            return printStr;
        }
        Iterator iterator = srcStrMap.entrySet().iterator();
        int time = 0;
        while (iterator.hasNext()) {
            Map.Entry obj = (Map.Entry) iterator.next();
            String key = (String) obj.getKey();
            List<String> valueList = (List<String>) obj.getValue();
            printStr = printStr + "key:" + key + ",value:" + NormalFunction.getPrintStrFromList(valueList);
            if (time != srcStrMap.size() - 1) {
                printStr = printStr + ";";
            }
            time++;
        }
        printStr = "[" + printStr + "]";
        return printStr;
    }


    public static String getVersionSuffix(String version) {
        if (!TextUtils.isEmpty(version)) {
            return version.replace(".", "_");
        }
        return "";
    }

    public static UUID generateTaskID() {
        return UUID.randomUUID();
    }

    public static UUID getUUIDFromString(String uuidStr) {
        return UUID.fromString(uuidStr);
    }

    public static String generateRandomID() {
        return UUID.randomUUID().toString();
    }

    public static boolean findStrInStrs(String[] strs, String findStr) {
        if (strs == null
                || strs.length == 0
                || TextUtils.isEmpty(findStr)) {
            return false;
        }

        for (int i = 0; i < strs.length; i++) {
            if (findStr.equalsIgnoreCase(strs[i])) {
                return true;
            }
        }

        return false;
    }

    public static int[] getRandomInteger(int min, int max, int n) {

        if (n > (max - min + 1) || max < min) {
            return null;
        }

        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean isExisted = false;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static void goToNotificationSetting(Context context, Activity activity, int requestCode) {

        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", activity.getPackageName());
        intent.putExtra("app_uid", activity.getApplicationInfo().uid);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            goToAppDetail(context, activity, requestCode);
        }

        //APP_NOTIFICATION_SETTINGS may be removed by the manufacturer.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Intent intent = new Intent();
//            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//            intent.putExtra("app_package", activity.getPackageName());
//            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
//            activity.startActivityForResult(intent, requestCode);
//        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            goToAppDetail(activity, requestCode);
//        }
    }

    public static void goToAppDetail(Context context, Activity activity, int requestCode) {
//        ToastManager.getInstance(context).showToast("goToAppDetail");
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {

        }
    }


    public static <K, V> K getArrayMapKey(ArrayMap<K, V> arrayMapContent, V findValue) {
        K returnData = null;
        if (null != findValue
                && null != arrayMapContent) {
            for (int i = 0; i < arrayMapContent.size(); i++) {
                if (findValue == arrayMapContent.valueAt(i)) {
                    returnData = arrayMapContent.keyAt(i);
                    break;
                }
            }
        }
        return returnData;
    }

    public static <K, V> V getArrayMapValue(ArrayMap<K, V> arrayMapContent, K findKey) {
        V returnData = null;
        if (null != findKey
                && null != arrayMapContent) {
            for (int i = 0; i < arrayMapContent.size(); i++) {
                if (findKey == arrayMapContent.keyAt(i)) {
                    returnData = arrayMapContent.valueAt(i);
                    break;
                }
            }
        }
        return returnData;
    }

    /**
     * Returns an immutable copy of {@code list}.
     */
    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    /**
     * Returns an immutable list containing {@code elements}.
     */
    public static <T> List<T> immutableList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements.clone()));
    }

    public static String getResValue(Context context, int resId) {
        if (null == context)
            return null;
        return context.getResources().getString(resId);
    }

}
