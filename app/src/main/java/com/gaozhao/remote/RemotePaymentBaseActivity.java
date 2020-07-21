package com.gaozhao.remote;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.gaozhao.remote.util.RomUtil;
import com.gaozhao.remote.util.StatusBarColorUtil;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * Date on 2020/7/20.
 * https://me.csdn.net/gao511147456
 */
public class RemotePaymentBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q && !RomUtil.isMiui()) {
                window.setNavigationBarColor(getResources().getColor(R.color.lt_white_11));
            }
            StatusBarColorUtil.setStatusBarLightMode(window);
        }
    }
}
