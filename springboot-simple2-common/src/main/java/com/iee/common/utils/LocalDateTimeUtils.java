package com.iee.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @ClassName LocalDateTimeUtils
 * @Description 时间获取工具类. 注意:必须使用jdk8以上
 * @Author longxiaonan@163.com
 * @Date 2018/11/2 0002 10:16
 * @see:jdk8 Copyright (c) 2017, mikuismywifu@gmail.com All Rights Reserved.
 */
public class LocalDateTimeUtils {

    //默认使用系统当前时区
    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_NANO = "yyyy-MM-dd HH:mm:ss SSS";

    public static final String TIME_NOFUII_FORMAT = "yyyyMMddHHmmss";

    public static final String REGEX = "\\:|\\-|\\s";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 将Date转换成LocalDateTime
     *
     * @param d date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime;
    }

    /**
     * 将Date转换成LocalDate
     *
     * @param d date
     * @return
     */
    public static LocalDate dateToLocalDate(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param d date
     * @return
     */
    public static LocalTime dateToLocalTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return date
     */
    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 根据ChronoUnit枚举计算两个时间差，日期类型对应枚举
     * 注:注意时间格式，避免cu选择不当的类型出现的异常
     *
     * @param cu chronoUnit.enum.key
     * @param d1 date
     * @param d2 date
     * @return
     */
    public static long chronoUnitBetweenByDate(ChronoUnit cu, Date d1, Date d2) {
        return cu.between(LocalDateTimeUtils.dateToLocalDateTime(d1),LocalDateTimeUtils.dateToLocalDateTime(d2));
    }

    /**
     * 根据ChronoUnit枚举计算两个时间相加，日期类型对应枚举
     * 注:注意时间格式，避免cu选择不当的类型出现的异常
     *
     * @param cu chronoUnit.enum.key
     * @param d1 date
     * @param d2 long
     * @return
     */
    public static Date chronoUnitPlusByDate(ChronoUnit cu, Date d1, long d2) {
        return LocalDateTimeUtils.localDateTimeToDate(cu.addTo(LocalDateTimeUtils.dateToLocalDateTime(d1),d2));
    }

    /**
     * 将相应格式yyyy-MM-dd yyyy-MM-dd HH:mm:ss 时间字符串转换成Date
     *
     * @param time string
     * @param format string
     * @return date
     */
    public static Date stringParse(String time , String format) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        if (DATE_FORMAT_DEFAULT.equals(format) || DATE_FORMAT_NANO.equals(format)) {
            return LocalDateTimeUtils.localDateTimeToDate(LocalDateTime.parse(time, f));
        } else if (DATE_FORMAT.equals(format)){
            return LocalDateTimeUtils.localDateToDate(LocalDate.parse(time, f));
        }
        throw new RuntimeException("format不支持!, 请选择工具类中存在的format字符串");
    }
    public static Date stringParseDateTime(String time) {
        return stringParse(time,DATE_FORMAT_DEFAULT);
    }
    public static Date stringParseDate(String time) {
        return stringParse(time,DATE_FORMAT);
    }

    /**
     * 根据传入的时间格式返回系统当前的时间
     *
     * @param format string
     * @return
     */
    public static String nowFormat(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }
    public static String nowFormat() {
        return nowFormat(DATE_FORMAT_DEFAULT);
    }
    public static String nowFormatNano() {
        return nowFormat(DATE_FORMAT_NANO);
    }

    /**
     * time 类型等于Date返回String time 类型等于String返回对应格式化日期类型
     * time 等于String 暂时支持format为yyyy-MM-dd. yyyy-MM-dd HH:mm:ss. yyyyMMddHHmmss
     * time 等于Date 不限制格式化类型，返回String
     *
     * @param time string or date
     * @param format string
     * @param <T> T
     * @return localDateTime or localDate or string
     */
    public static <T> T timeFormat(T time,String format){
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        //暂时支持三种格式转换
        if (ClassIdentical.isCompatible(String.class,time)){
            if (DATE_FORMAT_DEFAULT.equals(format) || DATE_FORMAT_NANO.equals(format)) {
                LocalDateTime localDateTime = LocalDateTime.parse(time.toString(), f);
                return (T) localDateTime.atZone(ZONE).toInstant();
            }
            if (DATE_FORMAT.equals(format)) {
                LocalDate localDate = LocalDate.parse(time.toString(), f);
                return (T) localDate;
            }
            if (TIME_NOFUII_FORMAT.equals(format)) {
                String rp =  time.toString().replaceAll(REGEX,"");
                LocalDateTime localDate = LocalDateTime.parse(time.toString(), f);
                return (T) localDate;
            }
        }
        if (ClassIdentical.isCompatible(Date.class,time)){
            Date date = (Date) time;
            Instant instant = date.toInstant();
            instant.atZone(ZONE).format(f);
            return (T) instant.atZone(ZONE).format(f);
        }
        throw new RuntimeException("时间format失败");
    }
    public static <T> T timeFormat(T time){
        return timeFormat(time,DATE_FORMAT_DEFAULT);
    }
    public static <T> T timeFormatNano(T time){
        return timeFormat(time,DATE_FORMAT_NANO);
    }

    /**
     * 将time时间转换成毫秒时间戳
     *
     * @param time string
     * @return
     */
    public static long stringDateToMilli (String time) {
        return LocalDateTimeUtils.stringParse(time,DATE_FORMAT_DEFAULT).toInstant().toEpochMilli();
    }

    /**
     * 将毫秒时间戳转换成Date
     *
     * @param time string
     * @return
     */
    public static Date timeMilliToDate (String time) {
        return Date.from(Instant.ofEpochMilli(Long.parseLong(time)));
    }

    public static void main(String[] args) {
        long l = chronoUnitBetweenByDate(ChronoUnit.DAYS, new Date(), stringParseDateTime("2018-11-01 11:00:00"));
        System.out.println(l);//超过24小时算间隔一天
        Date date = chronoUnitPlusByDate(ChronoUnit.DAYS, new Date(), -1);
        System.out.println(date);//当为负数的时候就是减

        Date date1 = stringParse("2018-11-01 11:00:00 235", DATE_FORMAT_NANO);
        System.out.println(date1);

        System.out.println(nowFormatNano());

        System.out.println(timeFormat(new Date(), DATE_FORMAT_NANO));


    }
}
