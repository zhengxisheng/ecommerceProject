package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by fanlinglong on 2018/1/14.
 * 时间转化工具类
 */
public class DateTimeUtil {

    //时间标准格式
    public static final String STANDARD_FORMAY = "yyyy-MM-dd HH:mm:ss";

    /**
     *  字符串转时间
     * @param dateTimeStr  字符串
     * @param formatStr  时间格式
     * @return
     */
    public static Date strToDate (String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     *  时间转字符串
     * @param date  时间
     * @param formatStr  时间格式
     * @return
     */
    public static String dateToStr (Date date,String formatStr){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
     *  字符串转标准时间格式
     * @param dateTimeStr
     * @return
     */
    public static Date strToDate (String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAY);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     *  时间转标准时间字符串
     * @param date
     * @return
     */
    public static String dateToStr (Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAY);
    }

    public static void main(String [] args){
        System.out.println(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd"));
        System.out.println(DateTimeUtil.strToDate("2018/1/14","yyyy/MM/dd"));
    }
}
