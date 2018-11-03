package com.huacheng.huiservers.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.huacheng.huiservers.BaseApplication;

/**
 * Description: 权限工具类
 * created by wangxiaotao
 * 2018/6/22 0022 上午 8:50
 */
public class PermissionUtils {

    public static boolean checkPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                int targetSdkVersion = info.applicationInfo.targetSdkVersion;
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    // targetSdkVersion >= Android M, we can
                    // use Context#checkSelfPermission
                    result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                } else {
                    // targetSdkVersion < Android M, we have to use PermissionChecker
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    //    调用动态获取权限API
    public static String[] PERMISSION = {Manifest.permission.READ_PHONE_STATE};

    public static boolean isLacksOfPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                    BaseApplication.getApplication().getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED;
        }
        return false;
    }
}
