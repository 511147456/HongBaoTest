package com.gaozhao.remote.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.nio.file.FileAlreadyExistsException;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * Date on 2020/7/23.
 * https://me.csdn.net/gao511147456
 */
public class SharedPreferenceUtils {
    private static final String REMOTE_PAYMENT = "remotePayment";
    private static final String IS_AUTO_SETTING_PHONE = "autoSettingPhone";
    private static final String IS_SETTING_WECHAT = "isSettingWechat";
    private static final String IS_SETTING_ALIPAY = "IsSettingAlipay";

    public static void saveIsAutoSettingPhone(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_AUTO_SETTING_PHONE,true);
        editor.apply();
    }

    public static boolean getIsAutoSettingPhone(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_AUTO_SETTING_PHONE, false);
    }

    public static void saveIsSettingWechat(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SETTING_WECHAT,true);
        editor.apply();
    }

    public static boolean getIsAutoSettingWechat(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_SETTING_WECHAT, false);
    }

    public static void saveIsSettingAlipay(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SETTING_ALIPAY,true);
        editor.apply();
    }

    public static boolean getIsSettingAlipay(Context context){
        SharedPreferences preferences = context.getSharedPreferences(REMOTE_PAYMENT,Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_SETTING_ALIPAY, false);
    }



}
