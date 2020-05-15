package cn.nano.common.utils;

/**
 * Created by sxl4287 on 17/6/13.
 * Email : sxl4287@arcsoft.com
 */

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double lat1 = rad(latitude1);
        double lat2 = rad(latitude2);
        double a = lat1 - lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 1 英里 = 5 280 英尺 = 63 360 英寸 = 1 609.344 米=1.609344公里
     *
     * @param miles
     * @return
     */
    public static double miles2km(double miles) {
        return miles * 1609.344;
    }

}
