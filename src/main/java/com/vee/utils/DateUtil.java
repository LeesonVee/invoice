package com.vee.utils;/**
 * Arthur Administrator
 * date 2020/5/19.
 */

import com.alibaba.fastjson.JSON;
import com.vee.rsa.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *@ClassName DateUtil
 *@Description TODO
 *@Author Administrator
 *@Date 2020/5/19 13:50
 *@Version 1.0
 **/
public class DateUtil {
    public static final String dtLong = "yyyyMMddHHmmss";
    public static final String simple = "yyyy-MM-dd HH:mm:ss";
    public static final String dtShort = "yyyyMMdd";

    public static String getOrderNum()
    {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }

    public static String getDateFormatter()
    {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String getDate()
    {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }

    public static String getThree()
    {
        Random rad = new Random();
        return rad.nextInt(1000) + "";
    }
}
