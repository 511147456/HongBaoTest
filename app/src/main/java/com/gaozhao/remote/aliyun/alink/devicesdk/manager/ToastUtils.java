package com.gaozhao.remote.aliyun.alink.devicesdk.manager;

import android.content.Context;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.gaozhao.remote.RemotePaymentApplication;

public class ToastUtils {

    public static void showToast(String message) {
        showToast(message, RemotePaymentApplication.getAppContext());
    }

    private static void showToast(final String message, final Context context) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
