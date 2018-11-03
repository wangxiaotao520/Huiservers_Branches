package com.huacheng.huiservers.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Blincheng.
 * Date: 2017/6/30.
 * Description:EToast2.0
 */

public class EToast2 {

    private WindowManager manger;
    private Long time = 2000L;
    private static View contentView;
    private WindowManager.LayoutParams params;
    private static Timer timer;
    private Toast toast;
    private static Toast oldToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private static Handler handler;
    private CharSequence text;
    private Context mContext;

    private EToast2(Context context, CharSequence text, int HIDE_DELAY) {
        this.mContext=context;
        manger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        this.text = text;

        if (HIDE_DELAY == EToast2.LENGTH_SHORT)
            this.time = 2000L;
        else if (HIDE_DELAY == EToast2.LENGTH_LONG)
            this.time = 3500L;

        if (oldToast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            View toastRoot = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
            TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
            tv.setText(text);
            contentView = toastRoot;
            params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = -1;
            params.setTitle("EToast2");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        }
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    EToast2.this.cancel();
                }
            };
        }
    }

    public static EToast2 makeText(Context context, String text, int HIDE_DELAY) {
        EToast2 toast = new EToast2(context, text, HIDE_DELAY);
        return toast;
    }

    public static EToast2 makeText(Context context, int resId, int HIDE_DELAY) {
        return makeText(context, context.getText(resId).toString(), HIDE_DELAY);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void show() {
        if (mContext!=null){
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing() && !((Activity) mContext).isDestroyed())
                    if (oldToast == null) {
                        oldToast = toast;
                        manger.addView(contentView, params);
                        timer = new Timer();
                    } else {
                        timer.cancel();
                        oldToast.setText(text);
                    }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (handler!=null){
                            handler.sendEmptyMessage(1);
                        }else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    EToast2.this.cancel();
                                }
                            });
                        }
                    }
                }, time);
            }
        }
    }

    public void cancel() {
        try {
            manger.removeView(contentView);
        } catch (IllegalArgumentException e) {
            //这边由于上下文被销毁后removeView可能会抛出IllegalArgumentException
            //暂时这么处理，因为EToast2是轻量级的，不想和Context上下文的生命周期绑定在一块儿
            //其实如果真的想这么做，可以参考博文2的第一种实现方式，添加一个空的fragment来做生命周期绑定
        }
        if (timer!=null){
            timer.cancel();
        }
        if (oldToast!=null){
            oldToast.cancel();
        }
        timer = null;
        toast = null;
        oldToast = null;
        contentView = null;
        handler = null;
    }

    public void setText(CharSequence s) {
        toast.setText(s);
    }
}