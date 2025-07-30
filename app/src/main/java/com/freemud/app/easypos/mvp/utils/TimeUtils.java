package com.freemud.app.easypos.mvp.utils;

import android.text.TextUtils;

import com.jess.arms.utils.LogUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shuyuanbo on 2022/2/25.
 * Description:
 */
public class TimeUtils {
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_2 = "yyyy/MM/dd";

    /**
     * 得到日期 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(time * 1000);
        return date;
    }

    /**
     * 得到日期 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatDate(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(time);
        return date;
    }

    /**
     * 格式化日期
     **/
    public static String formatData(long time, String format) {
        if (time <= 0) {
            return "未知时间";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    //把日期转为字符串
    public static String ConverToString(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String getDayEnd(String date) {
        return date + " 23:59:59";
    }

    /**
     * 转化时间戳
     *
     * @return
     */
    public static String transTimeStamp(String time, String format) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = d.getTime();
        String str = String.valueOf(l);
        re_time = str.substring(0, 10);
        LogUtils.debugInfo("时间" + time + "转化为:" + re_time);
        return re_time;
    }


    /**
     * 格式化日期
     **/
    public static String formatData(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long getDataTime(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        try {
            Date date = dateFormat.parse(data);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    /**
     * 获取当前时间的起点（00:00:00）
     *
     * @param currentTime
     * @return
     */
    public static long getTodayStart(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间的小时值
     *
     * @param currentTime
     * @return
     */
    public static int getHour(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间的分钟值
     *
     * @param currentTime
     * @return
     */
    public static int getMinute(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间的分钟值
     *
     * @param currentTime
     * @return
     */
    public static int getSecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当前时间的分钟值+毫秒值
     *
     * @param currentTime
     * @return
     */
    public static int getMinuteMillisecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MINUTE) * 60 * 1000 + calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);
    }

    public static String removeHour(String date) {
        if (!TextUtils.isEmpty(date) && date.contains(" ")) {
            date = date.split(" ")[0];
        }
        return date;
    }

    public static String removeSecond(String time) {
        if (!TextUtils.isEmpty(time) && time.contains(":")) {
            String[] arr = time.split(":");
            if (arr.length == 3) {
                return arr[0] + ":" + arr[1];
            }
        }
        return time;
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @param currentTime
     * @return
     */
    public static int getMillisecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 获取当前时间的终点（23:59:59）
     *
     * @param currentTime
     * @return
     */
    public static long getTodayEnd(long currentTime) {
        return getTodayStart(currentTime) + 24 * 60 * 60 * 1000L - 1000;
    }

    /**
     * 获取指定时间的年月日
     *
     * @param currentTime
     * @return
     */
    public static String getDateByCurrentTiem(long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
    }

    /**
     * 获取指定时间的年月日
     *
     * @param currentTime
     * @return
     */
    public static String getDateTime(long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
    }

    /**
     * 根据下标获取HH:mm格式的时间
     *
     * @param timeIndex
     * @return
     */
    public static String getHourMinute(int timeIndex) {
        return format.format(timeIndex * 60 * 1000 + 16 * 60 * 60 * 1000);
    }

    /**
     * 获取指定日期的时间（如：10:11:12）
     *
     * @param currentTime
     * @return
     */
    public static String getTime(long currentTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(currentTime));
    }

    /**
     * 获取指定日期的时间（如：10:11:12）
     *
     * @param
     * @return
     */
    public static String getMinute() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date());
    }

    /**
     * 根据当前的秒数计算时间
     *
     * @param currentSecond
     * @return
     */
    public static String getTimeByCurrentSecond(int currentSecond) {
        currentSecond = currentSecond / 60;
        int minute = currentSecond % 60;
        int hour = currentSecond / 60;
        if (hour >= 24) {
            hour = hour % 24;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 根据当前的秒数计算时间
     *
     * @param currentSecond
     * @return
     */
    public static String getTimeByCurrentHours(int currentSecond) {
        currentSecond = currentSecond * 10;
        currentSecond = currentSecond / 60;
        int minute = currentSecond % 60;
        int hour = currentSecond / 60;
        if (hour >= 24) {
            hour = hour % 24;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean isCurrentTimeArea(long nowTime, long beginTime, long endTime) {
        return nowTime >= beginTime && nowTime <= endTime;
    }


    /**
     * @Description: 某个时间距当前日期的时间差天和小时
     * @Author MengXY
     * @Emil xiangyongmeng@4d-bios.com
     * @Date 2019/1/14
     * @Params
     * @Return [ ]
     */
    public static String getDiffTime(String date1, String date2) {
        String str = "00:00";
        try {
            long diff = 0;
            long day, hour, min;
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date dta = dateFormat.parse(date1);
            Date dtb = dateFormat.parse(date2);
            long time1 = dta.getTime();
            long time2 = dtb.getTime();
            if (time1 < time2) {
                diff = time2 - time1;
            } else if (time1 > time2) {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            if (day == 1 && hour == 0 && min == 0) {
                return "24:00";
            }
            if (hour < 10) {
                str = "0" + hour;
            } else {
                str = hour + "";
            }
            str += ":";
            if (min < 10) {
                str += "0" + min;
            } else {
                str += min + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @Description: 某个时间距当前日期的时间差天和小时
     * @Author MengXY
     * @Emil xiangyongmeng@4d-bios.com
     * @Date 2019/1/14
     * @Params
     * @Return [ ]
     */
    public static int[] getDistanceDay(long date) {
        if (date == 0) {
            return new int[]{0, 0, 0};
        }
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String now = df.format(new Date());
//        Date one;
//        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
//            one = df.parse(now);
//            two = df.parse(date);
            long time1 = new Date().getTime();
            long time2 = new Date(date).getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] diffdate = new int[]{(int) day, (int) hour, (int) min};
        return diffdate;
    }

    /**
     * @Description: 某个时间距当前日期的时间差天和小时
     * @Author MengXY
     * @Emil xiangyongmeng@4d-bios.com
     * @Date 2019/1/14
     * @Params
     * @Return [ ]
     */
    public static int[] getDistanceDay(Date date) {
        if (date == null) {
            return new int[]{0, 0, 0};
        }
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String now = df.format(new Date());
//        Date one;
//        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
//            one = df.parse(now);
//            two = df.parse(date);
            long time1 = new Date().getTime();
            long time2 = date.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] diffdate = new int[]{(int) day, (int) hour, (int) min};
        return diffdate;
    }

    public static int[] getDistanceDay(Date date, Date date2) {
        if (date == null) {
            return new int[]{0, 0, 0};
        }
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String now = df.format(new Date());
//        Date one;
//        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
//            one = df.parse(now);
//            two = df.parse(date);
            long time1 = date2.getTime();
            long time2 = date.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] diffdate = new int[]{(int) day, (int) hour, (int) min};
        return diffdate;
    }

    //Calendar 转化 String
    public static String calendarToStr(Calendar calendar, String format) {

//    Calendar calendat = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(calendar.getTime());
    }


    //String 转化Calendar
    public static Calendar strToCalendar(String str, String format) {

//    String str = "2012-5-27";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        Calendar calendar = null;
        try {
            date = sdf.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    //    Date 转化String
    public static String dateTostr(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
//    String dateStr = sdf.format(new Date());
        String dateStr = sdf.format(date);
        return dateStr;
    }


    //  String 转化Date
    public static Date strToDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Calendar strToCalendar(String str) {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    //Date 转化Calendar
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    //Calendar转化Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


    // String 转成    Timestamp

    public static Timestamp strToTimeStamp(String str) {

//    Timestamp ts = Timestamp.valueOf("2012-1-14 08:11:00");
        return Timestamp.valueOf(str);
    }


    //Date 转 TimeStamp
    public static Timestamp dateToTimeStamp(Date date, String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        String time = df.format(new Date());

        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    public static String getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }

    /**
     * 获取目标n天相隔的日期
     * @param beginDate
     * @param distanceDay
     * @return
     */
    public static String getDistanceDay(Date beginDate, int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(beginDate.getTime() + (long) distanceDay * 24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 获取目标n月相隔的日期
     * @param date
     * @param distanceMonth
     * @return
     */
    public static String getDistanceMonth(Date date, int distanceMonth) {
        Calendar beginCalendar = dateToCalendar(date);
        beginCalendar.add(Calendar.MONTH,-distanceMonth);
        beginCalendar.add(Calendar.YEAR,-1);
        beginCalendar.add(Calendar.DATE,1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(beginCalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 获取上一个星期的第一天和最后一天
     * @return
     */
    public static String[] getLastWeekBeginEndDay() {
        String[] tmp = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int lastMonday = calendar.get(Calendar.DATE);

        calendar.add(Calendar.DATE,6);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH)+1;
        int lastSunday = calendar.get(Calendar.DATE);

        tmp[0] = year + "-" + (month<10?"0"+month:month)+"-"+(lastMonday<10?"0"+lastMonday:lastMonday);
        tmp[1] = endYear + "-" + (endMonth<10?"0"+endMonth:endMonth)+"-"+ (lastSunday<10?"0"+lastSunday : lastSunday);
        return tmp;
    }

    /**
     * 获取上一个月的第一天和最后一天
     * @return
     */
    public static String[] getLastMonthBeginEndDay() {
        String[] tmp = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        int year = calendar.get(Calendar.YEAR);
        int lastMonth = calendar.get(Calendar.MONTH)+1;
        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        tmp[0] = year + "-" + (lastMonth<10?"0"+lastMonth:lastMonth)+"-"+"01";
        tmp[1] = year + "-" + (lastMonth<10?"0"+lastMonth:lastMonth)+"-"+ endDay;
        return tmp;
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);
        return isSameDate;
    }

    /**
     * 是否是过去的时间 date 2相对date1
     * @param date1 参照时间
     * @param date2 比较时间
     * @return true date1大
     */
    public static boolean isOldDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        if (cal1.get(Calendar.YEAR)>cal2.get(Calendar.YEAR)){
            return true;
        }else if (cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR)){
            if (cal1.get(Calendar.MONTH)>cal2.get(Calendar.MONTH)){
                return true;
            }else if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)){
                if (cal1.get(Calendar.DAY_OF_MONTH)>cal2.get(Calendar.DAY_OF_MONTH)){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
