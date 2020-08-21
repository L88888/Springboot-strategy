package com.sailing.di.sysservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    /**
     * 获得当前时间，格式 yyyy-MM-dd HH:mm:ss
     * return String
     */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 获得当前时间，格式 yyyy-MM-dd HH:mm:ss
     * return Date
     *
     */
    public static Date getDate() {

        Date now = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            now = dtf.parse(dtf.format(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得当前时间，格式自定义
     *
     */
    public static String getCurrentDate(String format) {
        Calendar day = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(day.getTime());
    }

    /**
     * 获得昨天时间，格式自定义
     * 
     * @params format
     * @return String
     */
    public static String getYesterdayDate(String format) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(day.getTime());
    }

    /**
     * 获取每个月的第一天时间
     * 
     * @params year
     * @params month
     * @return String
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1, 0, 0, 0);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * 获取每个月的最后一天时间
     * 
     * @params year
     * @params month
     * @return String
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1, 0, 0, 0);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }


    /**
     * 根据给定日期输出指定格式的日期字符串
     * 
     * @params date
     * @params format
     * @return String
     * @see
     * @since
     */
    public static String formatData(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 按照时间字符串和格式转换成Date类
     * 
     * @params date
     * @params format
     * @return Date
     * @throws ParseException
     * @see
     * @since
     */
    public static Date getDate(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }


    /**
     * 判断两个时间是否跨月
     *
     * @params startTime
     * @params endTime
     * @return boolean
     */
    public static boolean isCrossMonth(String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            // 格式化开始日期和结束日期
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(sdf.parse(startTime));

            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(sdf.parse(endTime));

            // 开始时间结束时间大小比较
            long start = objCalendarDate1.getTimeInMillis();
            long end = objCalendarDate2.getTimeInMillis();
            if(start-end>0){
                return true;
            }

            int startYear = objCalendarDate1.get(Calendar.YEAR);
            int endYear = objCalendarDate2.get(Calendar.YEAR);

            // 判断是否跨年
            if(startYear-endYear!=0){
                return true;
            }
            // 判断是否跨月
            int startMonth = objCalendarDate1.get(Calendar.MONTH);
            int endMonth = objCalendarDate2.get(Calendar.MONTH);
            if(startMonth-endMonth!=0){
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return true;
         }

        return false;
    }
    // ----------------------------------------------------------------------

    /**
     * 根据输入的日期字符串 和 提前天数 ，
     * 获得 指定日期提前几天的日期对象
     * @param format 格式 如 yyyy-MM-dd
     * @param beforeDays 指定天数
     * @return 指定日期倒推指定天数后的日期
     */
    public static String getBeforeDaysDate(String format , int beforeDays) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, - beforeDays);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(day.getTime());
    }

    public static String getAfterDaysDate( String date, int afterDays) {
        String format = "yyyy-MM-dd";
        Calendar day = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date time = null;
        try {
            time = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        //Calendar day = Calendar.
        calendar.add(Calendar.DATE,afterDays);
        return sdf.format(calendar.getTime());
    }

    public static String getAfterSecondsDate(String date,int afterSeconds,String format){
        //String format = "yyyyMMddHHmmss";
        Calendar day = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date time = null;
        try {
            time = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        //Calendar day = Calendar.
        calendar.add(Calendar.SECOND,afterSeconds);
        return sdf.format(calendar.getTime());
    }

    public static long date2TimeStamp(String date_str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(getAfterDaysDate("2020-03-16",1));
        System.out.println(getCurrentDate());
        System.out.println("20220212111012".compareTo(getCurrentDate("yyyyMMddHHmmss")));
        System.out.println(getAfterSecondsDate("20140212111012",50,"yyyyMMddHHmmss"));
        System.out.println(getAfterSecondsDate("2015-10-14 02:56:50",1,"yyyy-MM-dd HH:mm:ss"));
        System.out.println(date2TimeStamp("20140212111012089"));
        System.out.println(getCurrentDate("yyyyMMddHHmmssSSS"));
        System.out.println(date2TimeStamp(getCurrentDate("yyyyMMddHHmmssSSS")));
        DateUtil.date2TimeStamp(DateUtil.getCurrentDate("yyyyMMddHHmmssSSS"));
        System.out.println("2019-06-01 18:02:13".substring(11,13));
        System.out.println("2019-06-01 18:02:13".substring(14,16));
    }



}
