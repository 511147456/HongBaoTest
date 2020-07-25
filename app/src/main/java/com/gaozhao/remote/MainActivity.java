package com.gaozhao.remote;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaozhao.remote.aliyun.alink.devicesdk.demo.AliyunIotActivity;
import com.gaozhao.remote.operation.activity.ExplainActivity;
import com.gaozhao.remote.service.RemoteAccessibilityService;
import com.gaozhao.remote.util.AppUtils;
import com.gaozhao.remote.util.MobileInfoUtils;
import com.gaozhao.remote.operation.activity.QuestionsActivity;
import com.gaozhao.remote.util.SharedPreferenceUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * https://me.csdn.net/gao511147456
 */

public class MainActivity extends RemotePaymentBaseActivity {


    //region args
    private static final String TAG = MainActivity.class.getSimpleName();
    private DisplayMetrics displayMetrics;
    int rollDownCounts = 0;
    private int WECHAT_PACKAGE_URL = 100;
    private int ALIPAY_PACKAGE_URL = 200;
    int currentPackage = 100;
    private boolean isSlideDown = true;
    private boolean isNeedOpeningWeChat = false;
    private boolean isNeedOpeningAlipay = false;
    private ImageView ivAutoSetting,ivWechat,ivAlipay;
    //endregion

    //region
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayMetrics = getResources().getDisplayMetrics();

        //region 屏幕横滑手势
        findViewById(R.id.bt_main_ShouShi).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(MainActivity.this, "7.0及以上才能使用手势", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "手势成功", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {

                                super.onCancelled(gestureDescription);
                                Toast.makeText(MainActivity.this, "手势失败，请重启手机再试",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }, null);

            }
        });
        //endregion

        //点击指定控件
        findViewById(R.id.bt_main_DianJi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("打开微信", true));
                if (ces == null) {
                    Utils.toast("找测试控件失败");
                    return; }

                RemoteAccessibilityService.clickView(ces);

            }
        });

        //用手势长按指定控件
        findViewById(R.id.bt_main_ChangAn).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(MainActivity.this, R.string.android_version_tips, Toast.LENGTH_SHORT).show();
                    return;
                }

                AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText("测试控件", true));
                if (ces == null) {
                    Utils.toast("找测试控件失败");
                    return; }

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
                        Toast.makeText(MainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();

                    }
                }, null);

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

        LinearLayout notificationMonitorOnBtn =
                (LinearLayout) findViewById(R.id.notification_monitor_on_btn);
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

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.notification_monitor_off_btn);
        linearLayout.setOnClickListener(new View.OnClickListener() {
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
        preventDoubleOpeningApp();
        ivAutoSetting = findViewById(R.id.iv_automation);
        ivWechat = findViewById(R.id.iv_wechat);
        ivAlipay = findViewById(R.id.iv_alipay);
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

        Boolean isAutoSettingPhone = SharedPreferenceUtils.getIsAutoSettingPhone(this);
        ivAutoSetting.setSelected(isAutoSettingPhone);
        ivWechat.setSelected(SharedPreferenceUtils.getIsAutoSettingWechat(this));
        ivAlipay.setSelected(SharedPreferenceUtils.getIsSettingAlipay(this));

    }


    private void openApp(String packageName) {

        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public void openWeChat(View view) {

        showSettingAppWay(true);

    }

    public void openAlipay(View view) {
        showSettingAppWay(false);

    }

    private void showSettingAppWay(final boolean isWechat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(isWechat? getString(R.string.string_set_wechat):getString(R.string.string_set_alipay));
        builder.setMessage(getString(R.string.Please_select_the_setting_mode_according_to_the_mobile_version_and_open_it)+(isWechat?getString(R.string.wechat):getString(R.string.alipay))+getString(R.string.collection_notification_function));

        builder.setIcon(isWechat?R.drawable.ic_wechat:R.drawable.ic_alipay);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.string_auto_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isWechat)setWeChat();else setAlipay();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(R.string.string_manual_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAppByManual(isWechat?"com.tencent.mm":"com.eg.android.AlipayGphone");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void setAlipay() {
        if (!isGetAccessibility()){
            return;
        };
        String packageOfAlipay = "com.eg.android.AlipayGphone";
        openApp(packageOfAlipay);
        isNeedOpeningAlipay = true;
        SharedPreferenceUtils.saveIsSettingAlipay(this);
    }

    private void setAppByManual(String packageName) {

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
    }

    private void setWeChat(){
        if (!isGetAccessibility()){
            return;
        };
        String packageOfWeChat = "com.tencent.mm";
        openApp(packageOfWeChat);
        isNeedOpeningWeChat = true;
        SharedPreferenceUtils.saveIsSettingWechat(this);
    }



    public void openAppSelf() {


        String packageOfRemotePayment =
                "com.gaozhao.remote";
        openApp(packageOfRemotePayment);
        Utils.toast("自动设置完成");


    }

    public void openPhoneNotificationSetting(View view) {

        if (!isGetAccessibility()){
            return;
        };

        Intent intent = new Intent(Settings.ACTION_SETTINGS);

        startActivity(intent);
        waitUiChange(1000);
        findTextView(getString(R.string.text_notification));

        waitUiChange(1000);
        if (isSlideDown) findOtherApp(getString(R.string.weChatAppName));

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

        SharedPreferenceUtils.saveIsAutoSettingPhone(this);

    }

    private Boolean isGetAccessibility() {
        if (!RemoteAccessibilityService.isStart()) {

            try {
                this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                Utils.toast("请打开"+ AppUtils.getAppName(this) +"无障碍权限");
                return false;

            } catch (Exception e) {

                this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                e.printStackTrace();
                return false;

            }

        }else return true;

    }

    private void findTextView(String textName) {
        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService.findFirst(AbstractTF.newText(textName, false));
        if (ces == null) {

            Utils.toast("找" + textName + "控件失败");
            return;

        }
        RemoteAccessibilityService.clickView(ces);
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
            Toast.makeText(MainActivity.this, "7.0及以上才能使用手势", Toast.LENGTH_SHORT).show();
            return; }

        Path path = new Path();
        path.moveTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 4 / 5);
        //从屏幕的 2/3 处开始滑动6
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

                                if (currentPackage == WECHAT_PACKAGE_URL) findOtherApp(getString(R.string.weChatAppName));
                                if (currentPackage == ALIPAY_PACKAGE_URL) findOtherApp(getString(R.string.alipayAppName));

                            }

                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            super.onCancelled(gestureDescription);
                            //Toast.makeText(RemotePaymentMainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();
                        }

                    }, null);

        }

    }


    private void findOtherApp(String appName) {

        AccessibilityNodeInfo ces =
                RemoteAccessibilityService.mService.findFirst(AbstractTF.newText(appName, false));
        rollDownCounts++;

        if (rollDownCounts == 120) {
            Utils.toast("未找到" + appName + "，请您先下载安装");
            isSlideDown = false;
            return;
        }

        if (ces == null && isSlideDown) {
            moveDown();
        } else {

            RemoteAccessibilityService.clickView(ces);
            isSlideDown = true;
            if (currentPackage == WECHAT_PACKAGE_URL) openWeChatNotification();
            if (currentPackage == ALIPAY_PACKAGE_URL) alipaySettingTimerTask();


        }
    }

    protected void alipaySettingTimerTask() {

        TimerTask alipaySettingTimerTask = new TimerTask() {

            @Override
            public void run() {

                Message message = new Message();
                message.what = 0x11;
                handler.sendMessage(message);

            }
        };

        Timer startRefreshUI = new Timer();
        startRefreshUI.schedule(alipaySettingTimerTask, 1000);

    }

    protected void preventDoubleOpeningApp() {

        TimerTask preventDoubleOpeningApp = new TimerTask() {

            @Override
            public void run() {

                Message message = new Message();
                message.what = 0x12;
                handler.sendMessage(message);

            }
        };

        Timer startRefreshUI = new Timer();
        startRefreshUI.schedule(preventDoubleOpeningApp, 1000, 3000);

    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 0x11) {
                openAlipayNotification();
            } else if (msg.what == 0x12) {

                if (isNeedOpeningWeChat) {
                    autoInitWechatSetting();
                }
                if (isNeedOpeningAlipay) {
                    autoInitAlipaySetting();

                }

            }
        }
    };

    private void openAlipayNotification() {

        waitUiChange(500);
        openFirstSwitch();
        autoClick("普通通知");
        waitUiChange();
        openFirstSwitch();
        openAllCheckBox();
        rollBack();
        waitUiChange();
        autoClick("支付宝通知");
        waitUiChange();
        openAllCheckBox();
        currentPackage = WECHAT_PACKAGE_URL;
        openAppSelf();

    }

    //region 自动设置通知栏
    private synchronized void openWeChatNotification() {

        waitUiChange();
        openFirstSwitch();
        autoClick(getString(R.string.other_notification));
        waitUiChange();
        openFirstSwitch();
        openAllCheckBox();
        waitUiChange();
        //todo setting
        rollBack();
        waitUiChange();
        autoClick(getString(R.string.new_message_notification));
        waitUiChange();
        openFirstSwitch();
        openAllCheckBox();
        rollBack();
        //todo setting
        waitUiChange();
        rollBack();
        waitUiChange();
        currentPackage = 200;
        rollDownCounts = 0;
        isSlideDown = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findOtherApp(getString(R.string.alipayAppName));
            }
        });
    }

    //region 设置微信内部
    private void autoInitWechatSetting() {

        //waitUiChange(800);
        setWechatHome();
        if (isNeedOpeningWeChat)return;
        waitUiChange();
        autoClick("通讯录");
        waitUiChange();
        autoClick(getString(R.string.me),true);
        waitUiChange();

        autoClick(getString(R.string.setting));
        waitUiChange();
        autoClick(getString(R.string.new_message_notification));
        waitUiChange();

        //operationTheSwitch(true,0);
        openFirstSwitch();
        waitUiChange();
        operationTheSwitch(true,2);

        //不能执行
        waitUiChange();
        operationTheSwitch(false,1);
        waitUiChange();
        autoClick(getString(R.string.new_message_system_notification));

        //正常执行
        waitUiChange();
        operationTheSwitch(true,0);

        //0:允许通知
        //3:允许打扰
        waitUiChange();
        openWeChatCheckBox(0,1);
        waitUiChange();
        operationTheSwitch(false,0);
        waitUiChange();
        operationTheSwitch(true,0);
        waitUiChange();
        rollBack();
        waitUiChange();
        rollBack();

        waitUiChange();
        openAppSelf();
        Utils.toast(getString(R.string.wechat_setting_complete));

    }

    private void autoInitAlipaySetting() {

        setAlipayHome();
        if (isNeedOpeningAlipay)return;
        waitUiChange();
        autoClick(getString(R.string.my));
        waitUiChange(400);
        autoClick(getString(R.string.string_setting));
        waitUiChange();

        autoClick(getString(R.string.string_common));
        waitUiChange();
        autoClick(getString(R.string.new_message_inform));
        waitUiChange(1000);

        //operationTheSwitch(true,0);
        //openFirstSwitch();
        waitUiChange();
        autoClick(getString(
                R.string.string_collect_money_acount),true);
        waitUiChange();
        operationTheSwitch(true,0);


        waitUiChange();
        rollBack();
        waitUiChange();
        rollBack();
        waitUiChange();
        rollBack();


        waitUiChange();
        rollBack();
        waitUiChange();
        openAppSelf();

    }

    private synchronized void setAlipayHome() {

        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService
                .findFirst(AbstractTF.newText("我的", true));

        if (ces == null) {
            //Utils.toast(getString(R.string.error_in_alipay));
        } else {

            RemoteAccessibilityService.clickView(ces);

            isNeedOpeningAlipay = false;

        }
    }

    private void openWeChatCheckBox(int... seqs) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                RemoteAccessibilityService.mService.findAll(AbstractTF.newClassName(AbstractTF.ST_CHECKBOX));

        if (accessibilityNodeInfoList.size() == 0) {
            //Utils.toast(getString(R.string.auto_setting));

        } else {
            for (int i = 0;i<seqs.length;i++){
                if (!accessibilityNodeInfoList.get(seqs[i]).isChecked()) {
                    boolean isAction = RemoteAccessibilityService.clickView(accessibilityNodeInfoList.get(seqs[i]));
                    waitUiChange(200);
                }
            }
        }
    }

    private synchronized void setWechatHome() {

        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService
                .findFirst(AbstractTF.newText("发现", false));

        if (ces == null) {
            //Utils.toast(getString(R.string.error_in_wechat));
        } else {
            RemoteAccessibilityService.clickView(ces);
            isNeedOpeningWeChat = false;
        }

    }

    //endregion


    //region tools
    private void openAllCheckBox() {

        List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                RemoteAccessibilityService.mService.findAll(AbstractTF.newClassName(AbstractTF.ST_CHECKBOX));

        if (accessibilityNodeInfoList.size() == 0) {

            //Utils.toast(getString(R.string.auto_setting));

        } else {
            if (!accessibilityNodeInfoList.get(0).isChecked()) {
                boolean isAction = RemoteAccessibilityService.clickView(accessibilityNodeInfoList.get(0));
            }

            if (!accessibilityNodeInfoList.get(1).isChecked()) {
                boolean isAction = RemoteAccessibilityService.clickView(accessibilityNodeInfoList.get(1));
            }
        }

    }

    private synchronized void openFirstSwitch() {

        //测试系统 华为EMUI
        AccessibilityNodeInfo ces =
                RemoteAccessibilityService.mService.findFirst(AbstractTF.newClassName(AbstractTF.ST_SWITCH));

        if (ces == null) {

            Utils.toast("未解析到switch视图");

        } else {

            if (!ces.isChecked()) {

                boolean isAction = RemoteAccessibilityService.clickView(ces);

            }

        }

    }

    private synchronized void operationTheSwitch(Boolean isOpen,int... seqs) {

        //测试系统 华为EMUI
        List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                RemoteAccessibilityService.mService.findAll(AbstractTF.newClassName(AbstractTF.ST_SWITCH));

        if (accessibilityNodeInfoList.size() == 0) {

            Utils.toast("未找到Switch开关请手动打开");

        } else {

            for (int seq : seqs) {
                int position = seqs[seq];
                if (accessibilityNodeInfoList.get(position).isChecked() != isOpen) {
                    boolean isAction = RemoteAccessibilityService.clickView(accessibilityNodeInfoList.get(position));
                    waitUiChange(200);
                }
            }

        }
    }

    private void waitUiChange() {

        waitUiChange(500);

    }

    private void waitUiChange(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void rollBack() {

        RemoteAccessibilityService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

    }

    private void autoClick(String viewText){
        autoClick(viewText,false);
    }

    private void autoClick(String viewText,boolean isEquals) {

        AccessibilityNodeInfo ces = RemoteAccessibilityService.mService
                .findFirst(AbstractTF.newText(viewText, isEquals));
        RemoteAccessibilityService.clickView(ces);
        //进入Emui其他设置界面

    }

    public void automaticStart(View view) {

        MobileInfoUtils.jumpStartInterface(this);

    }

    public void showBackgroundOptimizeWay(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.optimize_back_task);
        builder.setMessage(R.string.alert_dialog_tip_content);

        builder.setIcon(R.drawable.ic_running_backstage);
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(R.string.string_go_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toSelfSetting(getApplicationContext());
                dialog.dismiss();

            }
        });
        //设置反面按钮
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

    public void showWifiConfiguration(View view) {
    }

    public void showSelfDefinedPush(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_push);
        builder.setTitle("自定义推送");
        builder.setMessage("请选择你要推送的内容类型");
        builder.setPositiveButton("推送天气", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //      设置一个NegativeButton
        //
        builder.setNegativeButton("推送文字", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        //    设置一个NeutralButton
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
    }

    public void intentToAliyun(View view) {
        startActivity(new Intent().setClass(this, AliyunIotActivity.class));
    }

    public void intentToQuestion(View view) {
        startActivity(new Intent().setClass(this, QuestionsActivity.class));
    }

    public void intentToExplain(View view) {
        startActivity(new Intent().setClass(this, ExplainActivity.class));
    }

    //endregion


}
