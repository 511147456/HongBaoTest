package com.gaozhao.remote;

import android.app.Application;


/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * https://me.csdn.net/gao511147456
 */
public class MyApp extends Application {
    public static MyApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
