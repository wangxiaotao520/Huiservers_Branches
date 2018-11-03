package com.huacheng.huiservers.utils;

import android.text.TextUtils;

/**
 * 日志信息
 * Created by Administrator on 2018/1/30.
 */

public class LogJson {
    /**
     * 日志开关
     */
    private static boolean isDebug = true;

    private static String mTag = "wk";

    private static final String AUTHOR = "HARLAN -->";


    public static void debug(boolean status) {
        isDebug = status;
    }

    public static void d(String message) {
        if (isDebug) {
            android.util.Log.d(mTag, AUTHOR + message);
        }
    }

    public static void i(String message) {
        if (isDebug) {
            android.util.Log.i(mTag, AUTHOR + message);
        }
    }

    /**
     * Json格式化输出
     *
     * @param tag
     * @param message                 内容
     * @param isOutputOriginalContent 是否输入原内容
     */
    public static void iJsonFormat(String tag, String message) {
        if (isDebug && !TextUtils.isEmpty(message)) {
            android.util.Log.i(tag, AUTHOR + "\n" + JsonUtils.format(StringUtils.convertUnicode(message)));
        }
    }

    public static void iJsonFormat(String message, boolean isOutputOriginalContent) {
        if (isDebug && !TextUtils.isEmpty(message)) {
            if (isOutputOriginalContent)
                android.util.Log.i(mTag, AUTHOR + message);
            android.util.Log.i(mTag, AUTHOR + "\n" + JsonUtils.format(StringUtils.convertUnicode(message)));
        }
    }

    public static void w(String message) {
        if (isDebug) {
            android.util.Log.w(mTag, AUTHOR + message);
        }

    }

    public static void e(String message) {
        if (isDebug) {
            android.util.Log.e(mTag, AUTHOR + message);
        }
    }
}
