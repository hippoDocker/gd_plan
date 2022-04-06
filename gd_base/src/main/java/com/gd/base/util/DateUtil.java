package com.gd.base.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Auther: tangxl
 * @Date: 2021年12月1日19:52:09
 * @Description: 时间处理工具类
 */
@Component
public class DateUtil {

    /**
     * @description: TODO  Date转换为LocalDateTime
     * @return: localdatetime
     * @author: tangxl
     * @time:  19:53
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @description: TODO  String转换为LocalDateTime
     * @param date 字符串时间()
     */
    public static LocalDateTime stringToLocalDateTime(String date,String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date,df);
    }

    /**
     * @description: TODO String格式时间校验，默认yyyy-MM-dd HH:mm:ss格式
     * @Param: dateStr 字符串时间，pattern 时间格式
     * @author: tangxl
     * @date: 2022年3月4日14:38:11
     * @return: boolean
     */
    public static boolean validDateStr(String dateStr, String pattern) {
        if (dateStr == null || dateStr == "") {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            Date date = formatter.parse(dateStr);
            return dateStr.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static String dateToStrDate(Date date,String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static void main(String[] args) {
        System.out.println(Math.random() * (8 - 2 + 1) + 2);
    }
}
