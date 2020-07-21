package com.gaozhao.remote;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;


/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * https://me.csdn.net/gao511147456
 */
public class RemotePaymentApplication extends Application {
    public static RemotePaymentApplication remotePaymentApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        remotePaymentApplication = this;
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
