package com.gaozhao.remote;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import com.gaozhao.remote.service.RemoteAccessibilityService;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * https://me.csdn.net/gao511147456
 */

public class RemotePaymentMainActivity extends AppCompatActivity {


    private static final String TAG = RemotePaymentMainActivity.class.getSimpleName();
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        displayMetrics = getResources().getDisplayMetrics();

        //屏幕横滑手势
        findViewById(R.id.bt_main_ShouShi).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(RemotePaymentMainActivity.this, "7.0及以上才能使用手势", Toast.LENGTH_SHORT).show();
                    return;
                }

                Path path = new Path();

                path.moveTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 2 / 3);//从屏幕的2/3处开始滑动

                path.lineTo(10, displayMetrics.heightPixels * 2 / 3);

                final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 500);

                RemoteAccessibilityService.mService.dispatchGesture(

                        new GestureDescription.Builder().addStroke(sd).build(),
                        new android.accessibilityservice.AccessibilityService.GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {

                                super.onCompleted(gestureDescription);
                                Toast.makeText(RemotePaymentMainActivity.this, "手势成功", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {

                                super.onCancelled(gestureDescription);
                                Toast.makeText(RemotePaymentMainActivity.this, "手势失败，请重启手机再试",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }, null);

            }
        });

        //点击指定控件
        findViewById(R.id.bt_main_DianJi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("打开微信", true));
                if (ces == null) {
                    Utils.toast("找测试控件失败");
                    return;
                }

                RemoteAccessibilityService.clickView(ces);

            }
        });

        //用手势长按指定控件
        findViewById(R.id.bt_main_ChangAn).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

                    Toast.makeText(RemotePaymentMainActivity.this, R.string.android_version_tips, Toast.LENGTH_SHORT).show();
                    return;

                }

                AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("测试控件", true));
                if (ces == null) {

                    Utils.toast("找测试控件失败");
                    return;

                }

                //ces.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                //长按
                //这里为了示范手势的效果

                Rect absXY = new Rect();
                ces.getBoundsInScreen(absXY);

                //HongBaoService.mService.dispatchGestureClick(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2);//手势点击效果
                //手势长按效果

                Path path = new Path();
                path.moveTo(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2);

                //path.moveTo(absXY.bottom + (absXY.bottom - absXY.top) / 2, absXY.right + (absXY.right - absXY.left) / 2);
                //控件正中间

                RemoteAccessibilityService.mService.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                        (path, 0, 800)).build(), new android.accessibilityservice.AccessibilityService.GestureResultCallback() {

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {

                        super.onCancelled(gestureDescription);
                        Toast.makeText(RemotePaymentMainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();

                    }
                }, null);

            }
        });

        //用系统的返回效果
        findViewById(R.id.bt_main_FanHui).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RemoteAccessibilityService.mService.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);

            }

        });

        //测试的控件
        View viewCes = findViewById(R.id.bt_main_CeShi);
        viewCes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.toast("'测试控件'被点击了");
                Log.e(TAG, "onClick: '测试控件'被点击了");

            }
        });
        viewCes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.toast("'测试控件'被长按了");
                Log.e(TAG, "onLongClick: '测试控件'被长按了");
                return true;
            }
        });

        // 通知栏监控器开关

        Button notificationMonitorOnBtn =
                (Button) findViewById(R.id.notification_monitor_on_btn);
        notificationMonitorOnBtn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEnabled()) {

                            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

                        } else {

                            Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }
                });

        Button notificationMonitorOffBtn = (Button) findViewById(R.id.notification_monitor_off_btn);

        notificationMonitorOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnabled()) {

                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已关闭", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });
    }

    // 判断是否打开了通知监听权限
    private boolean isEnabled() {

        String pkgName = getPackageName();
        final String flat =
                Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {

            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RemoteAccessibilityService.isStart()) {
            try {
                this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            } catch (Exception e) {
                this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                e.printStackTrace();
            }
        }
    }


    private void openApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void openWeChat(View view) {

        String packageOfWeChat = "com.tencent.mm";
        openApp(packageOfWeChat);

    }

    public void openAlipay(View view) {
        String packageOfAlipay = "com.eg.android.AlipayGphone";
        openApp(packageOfAlipay);
    }

    private boolean isSlideDown = true;

    public void openPhoneNotificationSetting(View view) {

        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);

        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("通知", false));
        if (ces == null) {
            Utils.toast("找通知控件失败");
            return;
        }
        RemoteAccessibilityService.clickView(ces);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isSlideDown) findWeChat();



        /*

        String packageName = "com.tencent.mm";
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,packageName);

        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, getUid(this,packageName));
        } else {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", packageName);
            intent.putExtra("app_uid", getUid(this,packageName));
        }
        startActivity(intent);

        */
    }


    public static String getUid(Context context, String packageName) {
        String uid = "";
        try {

            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            uid = String.valueOf(ai.uid);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }


    private void moveDown() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            Toast.makeText(RemotePaymentMainActivity.this, "7.0及以上才能使用手势", Toast.LENGTH_SHORT).show();
            return;

        }

        Path path = new Path();
        path.moveTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 4 / 5);
        //从屏幕的2/3处开始滑动6
        path.lineTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 1 / 5);
        final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 200);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            RemoteAccessibilityService.mService.dispatchGesture(
                    new GestureDescription.Builder().addStroke(sd).build(),
                    new AccessibilityService.GestureResultCallback() {


                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);
                            //Toast.makeText(RemotePaymentMainActivity.this, "手势成功", Toast.LENGTH_SHORT).show();
                            if (isSlideDown) {
                                findWeChat();
                            }
                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            super.onCancelled(gestureDescription);
                            Toast.makeText(RemotePaymentMainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();
                        }


                    }, null);

        }

    }

    int index = 0;

    private void findWeChat() {

        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("微信", false));
        index++;

        if (index == 60) {

            Utils.toast("未找到微信，请您先下载安装微信");
            isSlideDown = false;
            return;

        }

        if (ces == null && isSlideDown) {

            moveDown();

        } else {

            RemoteAccessibilityService.clickView(ces);
            isSlideDown = true;
            
            openWeChatNotification();

        }
    }

    private void openWeChatNotification() {
        sleep();
        openFirstSwitch();
        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText(getString(R.string.other_setting), false));

        RemoteAccessibilityService.clickView(ces);
        sleep();
        
        
    }

    private void openFirstSwitch() {
        //测试系统 华为EMUI
        AccessibilityNodeInfo ces =
                RemoteAccessibilityService.mService.findFirst(AbstractTF.newClassName(AbstractTF.ST_SWITCH));
        if (ces == null) {
            Utils.toast(getString(R.string.auto_setting));
        } else{
            Utils.toast(getString(R.string.find));
            if (!ces.isChecked()){
                boolean isAction = RemoteAccessibilityService.clickView(ces);
            }
        }
    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    private void back(){
        RemoteAccessibilityService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


}
