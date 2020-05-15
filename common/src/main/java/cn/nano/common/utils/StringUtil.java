package cn.nano.common.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    private static final DecimalFormat threeDigitSplitFormat = new DecimalFormat("#,###");


    public static String ThreeDigitSplitIntToString(int num) {
        return threeDigitSplitFormat.format(num);
    }

    /**
     * 验证输入的字符串中是否包含空字符
     *
     * @param strPassWord
     * @return
     */
    public static boolean ishasBlock(String strPassWord) {
        if (null == strPassWord) {
            return true;
        }
        if (strPassWord.equals(strPassWord.trim())) {
            return false;
        }
        if (strPassWord.indexOf(" ") < 0) {
            return false;
        }
        return true;
    }

    /**
     * 将arrayList拼接为字符串
     *
     * @param arrayList
     * @return
     */
    public static String appendStr(List<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(arrayList.get(i));
        }
        return sb.toString();
    }

    /**
     * 将字符串经行分割为数组
     *
     * @param srcString
     * @return
     */
    public static List<String> splitStr(String srcString) {

        if (TextUtils.isEmpty(srcString)) {
            return null;
        }
        List<String> childsIdStrs = new ArrayList<>();
        // 只有一个childId
        if (!srcString.contains(",")) {
            childsIdStrs.add(srcString);
        } else {
            String[] childIds = srcString.split(",");
            for (int i = 0; i < childIds.length; i++) {
                childsIdStrs.add(childIds[i]);
            }
        }
        // 包含多个childId
        return childsIdStrs;
    }

    /**
     * VersionName地址：a.b.c比较
     * 版本号的大小就是 先比较两个版本号的a部分，如果谁的大，那这个版本号就是叫大的版本号；如果相等，就比较b部分；
     * * @param startIp
     *
     * @param oldVersion
     * @param newVersion
     * @return true: newVersion>oldVersion
     */
    public static boolean compareVersion(String oldVersion, String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return false;
        }
        boolean flag = false;
        String startIps[] = oldVersion.split("\\.");
        String endIps[] = newVersion.split("\\.");
        for (int i = 0; i < startIps.length; i++) {
            if (Integer.parseInt(endIps[i]) > Integer.parseInt(startIps[i])) {
                flag = true;
                break;
            } else {
                if (Integer.parseInt(endIps[i]) == Integer.parseInt(startIps[i])) {
                    continue;
                } else {
                    break;
                }
            }
        }
        return flag;

    }


    /**
     * @param str
     * @return
     */
    public static String urlDecode(String str) {
        String encodedURL;
        try {
            encodedURL = (str == null) ? "" : URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            encodedURL = "";
        }
        return encodedURL;
    }

    /**
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        String decodedURL;
        try {
            decodedURL = (str == null) ? "" : URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            decodedURL = "";
        }
        return decodedURL;
    }

    /**
     * 判断两个数组是否相等，包含里面的元素内容
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T extends Object> boolean compare(List<T> a, List<T> b) {
        if (null == a || a.size() == 0 || null == b || b.size() == 0) {
            return false;
        }
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
//            LogUtil.logE("compare", "a[" + i + "] = " + a.get(i).toString());
//            LogUtil.logE("compare", "b[" + i + "] = " + b.get(i).toString());
            if (!a.get(i).toString().equalsIgnoreCase(b.get(i).toString())) {
//                LogUtil.logE("compare", "a not equal b");
                return false;
            }

        }
        return true;
    }

    /**
     * 为tracking做处理，将大写转换为小写，空格变为下划线
     * <p>注意在TrackingManager中已经做了同一处理，外部不需要再次调用！！</p>
     *
     * @param string
     * @return
     */
    public static String upper2lower(String string) {
        if (TextUtils.isEmpty(string))
            return string;
        String value = "";
        value = string.replaceAll(" ", "_");
        value = value.toLowerCase();
        return value;
    }

    /**
     * string 转 ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * ascii 转 string
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}
