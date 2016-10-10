package com.humming.ascwg.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Elvira on 2016/5/23.
 */
public class SharePrefUtil {

    public static void putBoolean(String fileName, String key, boolean value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String fileName, String key, boolean defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String fileName, String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String fileName, String key, String defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(String fileName, String key, int value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String fileName, String key, int defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String fileName, String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    /**
     * 存储手机号
     */
//    public static void setPhoneNum(String fileName, String value, Context ctx) {
//        String str = "";
//        String regularEx = ".";
//        String[] phoneValues = getPhoneNum(fileName, ctx);
//        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        if (phoneValues!=null){
//            List phoneList = new ArrayList();//Arrays.asList(phoneValues);
//            for (String phone : phoneValues){
//                phoneList.add(phone);
//            }
//            for (int i=0;i<phoneList.size();i++){//去除重复号码
//                if (value.equals(phoneList.get(i))){
//                    phoneList.remove(i);
//                }
//            }
//            if (phoneValues.length==5) {//超过5个  删除第一个
//                phoneList.remove(0);
//            }
//            String[] arr = (String[])phoneList.toArray(new String[phoneList.size()]);
//            for (int i=0;i<arr.length;i++){
//                str += arr[i]+regularEx;
//            }
//            str += value;
//            sp.edit().putString(Constants.SHAREPREF_INFO_PHONENUM, str).commit();
//        } else {
//            sp.edit().putString(Constants.SHAREPREF_INFO_PHONENUM, value).commit();
//        }
//    }
//
//    /**
//     * 读取手机号
//     */
//    public static String[] getPhoneNum(String fileName, Context ctx) {
//        String regularEx = "\\.";
//        String[] str = null;
//        SharedPreferences sp = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        String values;
//        values = sp.getString(Constants.SHAREPREF_INFO_PHONENUM, "");
//        if (values.length() > 0 && !values.equals("")) {
//            str = values.split(regularEx);
//        }
//        return str;
//    }
//
//    /**
//     * 获取登录的手机号码
//     * */
//    public static String getLoginPhoneNum(String fileName, Context ctx){
//        String[] phoneValues = getPhoneNum(fileName, ctx);
//        return phoneValues[phoneValues.length-1];
//    }
}
