package com.wushu.tomato.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {

    public static String msToDate(int ms) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("HH:mm:ss");
        simpleFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return simpleFormatter.format(ms);
    }

    public static void main(String[] args) {
        System.out.print(DateUtil.msToDate(0));
    }
}

