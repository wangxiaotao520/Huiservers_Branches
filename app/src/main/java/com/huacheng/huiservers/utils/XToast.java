package com.huacheng.huiservers.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class XToast {
    private Context mContext;
    static TextView tv;
    private static String msgString;

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static int checkNotification = -1;
    private Object mToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    @SuppressLint("ShowToast")
    private XToast(Context context, String message, int duration) {
        try {

            if (context instanceof Application)
                checkNotification = 0;
            else
                checkNotification = isNotificationEnabled(context) ? 0 : 1;
            if (checkNotification == 1) {
                mToast = EToast2.makeText(context, message, duration);
            } else {
                mToast = EToast2.makeText(context, message, duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XToast(Context context, int resId, int duration) {
        checkNotification = isNotificationEnabled(context) ? 0 : 1;
        if (checkNotification == -1) {

        }
        if (checkNotification == 1) {
            mToast = XToast.makeText(context, resId, duration);
        } else {
            mToast = Toast.makeText(context, resId, duration);

        }

        View toastRoot = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
        tv.setText(resId);
        ((Toast) mToast).setView(toastRoot);
        ((Toast) mToast).setGravity(Gravity.CENTER, 0, 0);
        ((Toast) mToast).show();
    }

    public static XToast makeText(Context context, String message, int duration) {
        return new XToast(context, message, duration);
    }

    public static XToast makeText(Context context, int resId, int duration) {
        return new XToast(context, resId, duration);
    }

    public void show() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).show();
        } else if (mToast instanceof EToast2) {
            ((EToast2) mToast).show();
        }
    }

    public void cancel() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).cancel();
        } else if (mToast instanceof EToast2) {
            ((EToast2) mToast).cancel();
        }
    }

    public void setText(int resId) {
        if (mToast instanceof XToast) {
            ((XToast) mToast).setText(resId);
        } else if (mToast instanceof Toast) {
            ((Toast) mToast).setText(resId);
        }
    }

    public void setText(CharSequence s) {
        if (mToast instanceof XToast) {
            ((XToast) mToast).setText(s);
        } else if (mToast instanceof Toast) {
            ((Toast) mToast).setText(s);
        }
    }

    /**
     * 用来判断是否开启通知权限
     */

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
        }
        return false;
    }

}