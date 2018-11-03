package com.huacheng.huiservers.servicenew.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huacheng.huiservers.R;

/**
 * 类描述：
 * 时间：2018/9/7 09:11
 * created by DFF
 */
public class ServiceSuccessDialog extends Dialog implements View.OnClickListener {
    private OnCloseListener listener;
    int themeResId;
    Context mContext;

    public ServiceSuccessDialog(Context context, int themeResId ,OnCloseListener listener) {
        super(context,themeResId);
        this.mContext = context;
        this.listener = listener;
        this.themeResId = themeResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_success);
        setCanceledOnTouchOutside(false);
        TextView tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_btn) {
            if (listener != null) {
                listener.onClick(this, "1");
            }
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String str);
    }
}
