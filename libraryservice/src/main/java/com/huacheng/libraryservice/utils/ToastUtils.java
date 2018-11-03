package com.huacheng.libraryservice.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.libraryservice.R;

/**
 * Toast相关方法
 * Created by wangxiaotao
 */
public class ToastUtils {

    /**
     * 显示short message
     * @param context 全局context
     * @param resId string string资源id
     */
    public static void showShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示short message
     * @param context 全局context
     * @param message 显示msg
     */
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示long message
     * @param context 全局context
     * @param resId string string资源id
     */
    public static void showLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示long message
     * @param context 全局context
     * @param message 显示msg
     */
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * 分享成功toast
     */
    public static void showToastShare(final Context context, final String left_text) {
        final Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = RelativeLayout.inflate(context, R.layout.toast_layout_with_img, null);
        TextView tv_left = (TextView) view.findViewById(R.id.toast_text_left);
        tv_left.setText(left_text);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
