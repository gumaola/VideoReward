package cn.nano.common.utils;

/**
 * Created by cl2567 on 2016/5/3.
 * <p>用于单位转换的工具类</p>
 */
public class ConvertUtil {

    /**
     * 请求参数已经指定 温度单位为 F
     *
     * @param Fahrenheit 华氏度
     * @return 摄氏温度四舍五入
     */
    public static int F2C(int Fahrenheit) {
        //Fahrenheit
        //Celsius temperature
        //℃ = (οF - 32) / 1.8
        return (int) ((Fahrenheit - 32) / 1.8 + 0.5);
    }

    public static int C2F(int centigrade) {
        //Fahrenheit
        //Celsius temperature
        //℃ = (οF - 32) / 1.8
        return (int) (centigrade * 1.8 + 32);
    }

    /**
     * 1 mph = 1.609344 km/h
     * 1 km/h = 0.6213712 mph
     *
     * @param mph
     * @return KPH 四舍五入
     */
    public static int mph2kph(int mph) {
        return (int) (mph * 1.609344 + 0.5);
    }

    public static int kph2mph(int kph) {
        return (int) (kph * 0.6213712 + 0.5);
    }
}
