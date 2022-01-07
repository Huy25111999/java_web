package vn.m2m.utils;

import java.util.Date;

public class ParseUtil {
    public static int parseInt(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return i;
    }

    public static double parseDouble(String s) {
        double i = 0D;
        try {
            i = Double.parseDouble(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0D;
        }
        return i;
    }

    public static long parseLong(String s) {
        long  i = 0L;
        try {
            i = Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
        return i;
    }
    public static float parseFloat(String s) {
        float  i = 0F;
        try {
            i = Float.parseFloat(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0F;
        }
        return i;
    }

    public static float parseFloatRoundToHalf(String s) {
        float  i = 0F;
        try {
            i = Float.parseFloat(s);
            return (float) (Math.ceil(i * 2) / 2);
        } catch (Exception e) {
            e.printStackTrace();
            return 0F;
        }
    }

    public static long parseDate(Date date){
        if(date == null)
            return 0l;
        else
            return date.getTime();
    }
    public static String parseString(Object obj) {
        String str ="";
        try {
            str = String.valueOf(obj);
        } catch (Exception e) {
            return "";
        }
        return str;
    }

    public static String keyIpRequest(String ipAddress, String method){
        return ipAddress + " " + method;
    }

    // %.3f: 10.243, 2.123, 100.242
    public static String formatDouble(int digit, double number){
        String pattern = "%."+digit+"f";
        return String.format(pattern, number);
    }
}