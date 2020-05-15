package cn.nano.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    // 长日期格式
    public final static String US_TIME_FORMAT_ALL = "MM-dd-yyyy HH:mm:ss";
    public final static String TIME_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String US_TIME_FORMAT_YEAR_HOUR = "yyyy-MM-dd hh:mm";
    public final static String US_TIME_FORMAT_TIME_INFO_AM = "EEEE (MM-dd) hh:mm a";
    public final static String TIME_FORMAT_BACKSLASH_MM_DD_YYYY = "MM/dd/yyyy";

    public final static String TIME_FORMAT_CHAT_SERVICE_TIME = "MM-dd-yyyy, hh:mm a";
    public final static String TIME_FORMAT_CHAT_APPOINTMENT_TIME = "EEEE (MM-dd) \nhh:mm a";

    public final static String US_TIME_FORMAT_DATE = "MM-dd-yyyy";
    public final static String US_TIME_FORMAT_TIME = "HH:mm";

    public final static String US_TIME_FORMAT_YEAR_HOUR_12 = "yyyy-MM-dd h:mmaa";

    /**
     * 获取本地的时间，格式为08-05-2014 10:53
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeStr(long time, String formate) {
        // long time=System.currentTimeMillis();//long now =
        // android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(formate, Locale.ENGLISH);
        format.setTimeZone(getTimeZone("GMT+08:00"));
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
        // Log.e("msg", t1);
    }

    public static String getDateTime(String timezoneId, String format, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        TimeZone timeZone = getTimeZone(timezoneId);
        if (timeZone != null) {
            simpleDateFormat.setTimeZone(timeZone);
        }
        return simpleDateFormat.format(new Date(time));
    }

    public static TimeZone getTimeZone(String timezoneId) {
        TimeZone timeZone = TimeZone.getDefault();
        if (!TextUtils.isEmpty(timezoneId)) {
            try {
                timeZone = TimeZone.getTimeZone(timezoneId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return timeZone;
    }


    public static long getCurrentTimeInTimezone(String timezone) {
        TimeZone timeZone = getTimeZone(timezone);
        return Calendar.getInstance(timeZone).getTime().getTime();
    }

    /**
     * 获取本地的时间，格式为08-05-2014 10:53
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getLongTime2Str(String time) {
        Date date = new Date(Integer.parseInt(time) * 1000L);
        DateFormat shortDF = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.getDefault());
        return shortDF.format(date);
        // Log.e("msg", t1);
    }

    public static String getFormatLong2Str(long time, String formatType, TimeZone timeZone) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(formatType, Locale.getDefault());
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }

        return format.format(date);
    }


    /**
     * 获取本地的时间，格式为08-05-2015
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeStr(String strformat) {
        long time = System.currentTimeMillis();// long now =
        // android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(strformat);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
        // Log.e("msg", t1);
    }

    /**
     * 比较两个日期的大小
     *
     * @param until 解析的格式需要自行定义
     * @param date1 时间1
     * @param data2 时间2
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int compare_date(String until, String date1, String data2) {

        // Log.e("sxl", "DATE1 : " + date1 + " DATE2 : " + data2);
        SimpleDateFormat df = new SimpleDateFormat(until);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(data2);
            if (dt1.getTime() > dt2.getTime()) {
                // Log.e("sxl", "dt1在dt2之后");
                return -1;
            } else if (dt1.getTime() < dt2.getTime()) {
                // Log.e("sxl", "dt1在dt2之前");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * appointment 本地时间 转成 标准时间
     * formatIn: like yyyy-MM-dd HH:mm
     *
     * @return: 返回毫秒数
     */
    @SuppressLint("SimpleDateFormat")
    public static long localTime2GMT(String date, String formatIn) {
        try {
            if (!TextUtils.isEmpty(date)) {
                if (TextUtils.isEmpty(formatIn))
                    formatIn = US_TIME_FORMAT_ALL;

                SimpleDateFormat localSf = new SimpleDateFormat(formatIn);
                Date localDate = localSf.parse(date);

                return localDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * appointment 本地时间 转成 标准时间
     * formatIn: like yyyy-MM-dd HH:mm
     *
     * @return: 返回毫秒数
     */
    @SuppressLint("SimpleDateFormat")
    public static long localTime2GMT(String date, String formatIn, TimeZone timeZone) {
        try {
            if (!TextUtils.isEmpty(date)) {
                if (TextUtils.isEmpty(formatIn))
                    formatIn = US_TIME_FORMAT_ALL;

                SimpleDateFormat localSf = new SimpleDateFormat(formatIn);
                if (timeZone != null) {
                    localSf.setTimeZone(timeZone);
                }
                Date localDate = localSf.parse(date);

                return localDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 转换时间时区
     *
     * @param gmtMs      GMT毫秒数
     * @param formatStr: 时间输出格式,like yyyy-MM-dd HH:mm
     * @return local Date String
     * @throws ParseException
     */
    public static String GMT2LocalTime(Context context, Long gmtMs, String formatStr, boolean bAutoAdapte) {
        return GMT2LocalTime(context, gmtMs, formatStr, bAutoAdapte, null);
    }

    /**
     * 转换时间时区
     *
     * @param gmtMs      GMT毫秒数
     * @param formatStr: 时间输出格式,like yyyy-MM-dd HH:mm
     * @return local Date String
     * @throws ParseException
     */
    public static String GMT2LocalTime(Context context, Long gmtMs, String formatStr, boolean bAutoAdapte, TimeZone timeZone) {

        //获取源时区时间相对的GMT时间
        Long sourceRelativelyGMT = gmtMs - TimeZone.getTimeZone("GMT").getRawOffset();

        //GMT时间+目标时间时区的偏移量获取目标时间
        Long targetTime = sourceRelativelyGMT /*+ TimeZone.getDefault().getRawOffset()*/;

        boolean is24HF = android.text.format.DateFormat.is24HourFormat(context);
        String newForMatStr = formatStr;
        if (TextUtils.isEmpty(formatStr)) {
            newForMatStr = US_TIME_FORMAT_ALL;
        }
        if (!bAutoAdapte) {
            ;
        } else {
            if (is24HF && formatStr.contains("hh")) {
                newForMatStr = formatStr.replace("hh", "HH");
                if (formatStr.contains("a")) {
                    String[] forMatStrs = newForMatStr.split("a");
                    newForMatStr = "";
                    for (int i = 0; i < forMatStrs.length; i++) {
                        if (!TextUtils.isEmpty(forMatStrs[i])) {
                            int emptyIndex = forMatStrs[i].lastIndexOf(" ");
                            if (i != 0 && !TextUtils.isEmpty(newForMatStr)) {
                                newForMatStr = newForMatStr + " ";
                            }
                            if (emptyIndex >= 0) {
                                newForMatStr = newForMatStr + forMatStrs[i].substring(0, emptyIndex);
                            } else {
                                newForMatStr = newForMatStr + forMatStrs[i];
                            }
                        }
                    }
                }

            } else if (!is24HF && formatStr.contains("HH")) {
                newForMatStr = formatStr.replace("HH", "hh");
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(newForMatStr);
        if (timeZone != null) {
            sdf.setTimeZone(timeZone);
        }
        return sdf.format(new Date(targetTime));
    }

    /**
     * 获取手机当前的设置时区,语言设置为中文下，中间没有“：”
     *
     * @return 08:00 或者-8:00
     */
    public static String getGMTZone() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        TimeZone tz = cal.getTimeZone();
        int time = tz.getOffset(System.currentTimeMillis());
        int gmtHours = time / 1000 / 3600;
        int gmtMinutes = Math.abs(time / 1000 % 3600 / 60);

        StringBuilder gmtStr = new StringBuilder();
        if (gmtHours >= 0 && gmtHours < 10) {
            gmtStr.append("0");
            gmtStr.append(gmtHours);
            gmtStr.append(":");
            if (gmtMinutes == 0) {
                gmtStr.append("00");
            } else
                gmtStr.append(gmtMinutes);

        } else if (gmtHours > -10 && gmtHours < 0) {
            gmtStr.append("-0");
            gmtStr.append(Math.abs(gmtHours));
            gmtStr.append(":");
            if (gmtMinutes == 0) {
                gmtStr.append("00");
            } else
                gmtStr.append(gmtMinutes);
        } else if (gmtHours >= 10) {
            gmtStr.append(gmtHours);
            gmtStr.append(":");
            if (gmtMinutes == 0) {
                gmtStr.append("00");
            } else
                gmtStr.append(gmtMinutes);
        } else if (gmtHours <= -10) {
            gmtStr.append(gmtHours);
            gmtStr.append(":");
            if (gmtMinutes == 0) {
                gmtStr.append("00");
            } else
                gmtStr.append(gmtMinutes);
        }
        return gmtStr.toString();
    }

    /**
     * 从yyyy-MM-dd格式的String转换为date
     *
     * @param time yyyy-MM-dd
     * @return
     */
    public static Date parseTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 从yyyy-MM-dd格式的String转换为date
     *
     * @return yyyy-MM-dd
     */
    public static String getNowTime(TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        if (tz != null) {
            sdf.setTimeZone(tz);
        }
        return sdf.format(new Date());
    }

    /**
     * judge if the date is between dateStart and dateEnd
     *
     * @return WEEK[i]
     */
    public static boolean isDateInRange(String dateStart, String dateEnd) {

        try {
            SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = simple.parse(dateStart);
            Date endDate = simple.parse(dateEnd);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            // endDate也需要显示，因此比较下一天的凌晨
            endCalendar.roll(Calendar.DAY_OF_YEAR, 1);
            Calendar nowCalendar = Calendar.getInstance();

            if (nowCalendar.before(endCalendar)
                    && nowCalendar.after(startCalendar)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String formatString(String format, long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(milliseconds));
    }


    /**
     * 获取这个月天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthOfDay(int year, int month) {
        int day = 0;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }
}
