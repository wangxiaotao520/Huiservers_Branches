package com.huacheng.huiservers.view;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ajb.call.service.KeepAliveService;
import com.ajb.call.utlis.CommonUtils;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";
    private NotificationManager nm;
    SharedPreferences preferencesLogin;
    SharedPreferences userInfo;
    private String login_type, lat, lon;
    String urlh5, phone;
    Bundle b = new Bundle();
    Intent intent = new Intent();
    public static NotificationManager manger;
    public static Notification notification;
    public static int id;
    int num = 0;
    public static String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            SharedPreferences preferences1 = context.getSharedPreferences(
                    "jpush_id", 0);
            Editor editor = preferences1.edit();
            editor.putString("reg_id", regId);
            editor.commit();
            Log.d("TAG", "[MyReceiver] 接收Registration Id : " + regId);
            System.out.println("接收Registration Id^^^^^^^^^^^^^^^^^^^^" + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推 下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
            skip(context, bundle);//点击跳转
            //receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void skip(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        message = bundle.getString(JPushInterface.EXTRA_ALERT);
        try {
            JSONObject jsonObject = new JSONObject(extras);
            if (message.equals("Calling")) {

                KeepAliveService.handlePushMsg(context, message, extras);
                JPushInterface.clearNotificationById(context,
                        bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));

                return;
            } else {
                String url_type = jsonObject.getString("url_type");
                String url_link = jsonObject.getString("url_link");

                if (!TextUtils.isEmpty(url_type) && !TextUtils.isEmpty(url_link)) {
                    new Jump(context, url_type, url_link, "", "");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    private void processCustomMessage(Context context, Bundle bundle) {

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(extras);
            if (!jsonObject.optString("badge").equals("")) {
                String num = jsonObject.getString("badge");
                if (ToolUtils.isNum(num)) {
                    if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
                        setBadgeOfHuaWei(context, Integer.parseInt(num));
                    }
                } else {
                    XToast.makeText(context, "类型异常", XToast.LENGTH_SHORT).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 华为 系列
    public static void setBadgeOfHuaWei(Context context, int num) {

        try {
            Bundle localBundle = new Bundle();
            localBundle.putString("package", context.getPackageName());
            localBundle.putString("class", getLauncherClassName(context));
            localBundle.putInt("badgenumber", num);
            context.getContentResolver()
                    .call(Uri
                                    .parse("content://com.huawei.android.launcher.settings/badge/"),
                            "change_badge", null, localBundle);
            if (num != 0) {
                manger.notify(id, notification);
            } else if (id != 0) {
                // 想要清空通知
                if (null == manger) {
                    manger = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                }
                // manger.cancel(id);

                JPushInterface.clearNotificationById(context, id);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HUAWEI" + " Badge error", "set Badge failed");
        }

    }

    public static void clearBadgeOfHuaWei() {
        if (id != 0) {
            manger.cancel(id);
        }
    }

    // 获取类名
    public static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }

    //处理接收到的通知
    private void receivingNotification(Context context, Bundle bundle) {
        CommonUtils.setDebugMode(context, true, "http://47.104.92.9:8081");
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, " msg : " + message);
        Log.d(TAG, " extra : " + extra);
        if (message.equals("Calling")) {
            KeepAliveService.handlePushMsg(context, message, extra);
            JPushInterface.clearNotificationById(context,
                    bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
            return;
        }
    }

}
