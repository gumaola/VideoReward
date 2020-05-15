package cn.nano.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cl2567 on 2016/3/7.
 * <p>正则匹配工具类</p>
 */
public class RegexUtil {

    /**
     * 确认字符串是否为email格式
     *
     * @param strEmail
     * @return
     */
    public static boolean isValidEmail(String strEmail) {
        String str = "^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 包含a-z,A-Z,0-9 以及特殊字符
     *
     * @param paramString
     * @return
     */
    public static boolean isValidPassword(String paramString) {
        return Pattern.matches(
                "^[A-Za-z0-9`=\\\\;',./~!@#$%^&*()_+|{}:\\\"<>? -]{6,16}$",
                paramString);
    }

}
