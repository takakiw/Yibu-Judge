package com.yibu.yibuJudge.utils;

public class DataConvertUtil {
    public static String getTime(long interval) {
        long hour = interval / 3600;
        long minute = (interval % 3600) / 60;
        long second = interval % 60;
        String time = "";
        if (hour > 0) {
            time += hour + "h:";
        }
        if (minute > 0) {
            time += minute + "m:";
        }
        if (second > 0) {
            time += second + "s";
        }
        return time;
    }
}
