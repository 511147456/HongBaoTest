package com.gaozhao.remote;

import android.widget.Toast;

import java.util.Collection;


/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * https://me.csdn.net/gao511147456
 */
public class Utils {

    public static void toast(CharSequence cs) {
        Toast.makeText(MyApp.mApp, cs, Toast.LENGTH_SHORT).show();
    }

    //集合是否是空的
    public static boolean isEmptyArray(Collection list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmptyArray(T[] list) {
        return list == null || list.length == 0;
    }
}
