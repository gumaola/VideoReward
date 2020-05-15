package cn.nano.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * 本地Local 数据管理工具类PreferenceUtil
 * Created by sxl4287 on 16/3/2.
 * Email : sxl4287@arcsoft.com
 * <p>apply()和apply()区别：</p>
 * 1.apply没有返回值而commit返回boolean表明修改是否提交成功2.apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘, 而commit是同步的提交到硬件磁盘3.apply方法不会提示任何失败的提示
 * apply的效率高一些，如果没有必要确认是否提交成功建议使用apply。
 */
public class PreferenceUtil {

    private static final String TAG = PreferenceUtil.class.getSimpleName();
    private static boolean enableLog = false;
    public final static String BAGE_STATE = "bage_state";//File
    public final static String BAGE_SHOP = "bage_shop";//Key

    public final static String CONFIG_USER_INFO = "config_user_info";

    public final static String FILE_NORMAL_CACHE = "file_normal_cache";

    /**
     * *************** get ******************
     */

    public static String getString(Context context, String fileName, String key, String defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        try {
            return sp.getString(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }


    public static Set<String> getStringSet(Context context, String fileName, String key, Set<String> defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        try {
            return sp.getStringSet(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static boolean getBoolean(Context context, String fileName, String key, boolean defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        return sp.getBoolean(key, defValue);
    }


    public static float getFloat(Context context, String fileName, String key, float defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        return sp.getFloat(key, defValue);
    }

    public static double getDouble(Context context, String fileName, String key, final double defValue) {

        if (null == context) {
            return defValue;
        }

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if (null == sp || !sp.contains(key)) {
            return defValue;
        }

        return Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(defValue)));
    }

    public static int getInt(Context context, String fileName, String key, int defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        return sp.getInt(key, defValue);
    }

    public static long getLong(Context context, String fileName, String key, long defValue) {
        if (null == context) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (null == sp) {
            return defValue;
        }
        return sp.getLong(key, defValue);
    }

    public static Object getObject(Context context, String fileName, String key) {
        if (null == context) {
            return null;
        }
        try {
            String hex = getString(context, fileName, key, null);
            if (hex == null) return null;
            byte[] bytes = HexUtil.decodeHex(hex.toCharArray());
            Object obj = ByteUtil.byteToObject(bytes);
            logInfo(key + " get: " + obj);
            return obj;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * *************** put ******************
     */
    public static void putObject(Context context, String fileName, String key, Object ser) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        try {
            logInfo(key + " put: " + ser);
            if (ser == null) {
                sp.edit().remove(key).apply();
            } else {
                byte[] bytes = ByteUtil.objectToByte(ser);
                putString(context, fileName, key, HexUtil.encodeHexStr(bytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void putStringSet(Context context, String fileName, String key, Set<String> value) {
        if (null == context) {
            return;
        }
        try {
            SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            if (value == null) {
                sp.edit().remove(key).apply();
            } else {
                sp.edit().putStringSet(key, value).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putString(Context context, String fileName, String key, String value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        try {
            if (value == null) {
                sp.edit().remove(key).apply();
            } else {
                sp.edit().putString(key, value).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putBoolean(Context context, String fileName, String key, boolean value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void putFloat(Context context, String fileName, String key, float value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    public static void putDouble(Context context, String fileName, final String key, final double value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, Double.doubleToRawLongBits(value)).apply();
    }

    public static void putLong(Context context, String fileName, String key, long value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static void putInt(Context context, String fileName, String key, int value) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static boolean isExistedKey(Context context, String fileName, String key) {

        if (null == context
                || TextUtils.isEmpty(key)
                || TextUtils.isEmpty(fileName)) {
            return false;
        }

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if (sp == null) {
            return false;
        } else
            return sp.contains(key);
    }

    /**
     * *********************** clear file ****************************
     */
    public static void clear(Context context, String fileName) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


    public static Map<String, ?> getAll(Context context, String fileName) {
        if (null == context || TextUtils.isEmpty(fileName)) {
            return null;
        }

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (sp == null) {
            return null;
        } else {
            return sp.getAll();
        }
    }

    /**
     * Remove one key record from the existed SharedPreferences
     *
     * @param context
     * @param fileName
     * @param key
     */
    public static void removeDataByKey(Context context, String fileName, String key) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        //asynchronous
        editor.apply();
    }

    /**
     * *********************** 获取SharedPreferences对象 ****************************
     */
    public static SharedPreferences getPreferences(Context context, String fileName) {
        if (null == context) {
            return null;
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    private static void logInfo(String content) {
        if (!enableLog) return;
        Log.i(TAG, content);
    }


    public static void setLogStatus(boolean status) {
        enableLog = status;
    }
}
