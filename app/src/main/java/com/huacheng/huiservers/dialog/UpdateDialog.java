package com.huacheng.huiservers.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.AppUpdate;

/**
 * 类：
 * 时间：2018/6/6 20:28
 * 功能描述:Huiservers
 */
public class UpdateDialog extends AlertDialog implements View.OnClickListener {
    TextView tv_version, tv_update_no, tv_update_yes, tv_msg;
    private OnCustomDialogListener mOnCustomDialogListener;
    int type;
    Context mContext;
    ScrollView scroll;
    String msg;

    public UpdateDialog(Context context, int type, String msg, OnCustomDialogListener customDialogListener) {
        super(context, R.style.Dialog_down);
        this.mContext = context;
        this.mOnCustomDialogListener = customDialogListener;
        this.type = type;
        this.msg = msg;
    }


    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void back(String tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_result);
        setCanceledOnTouchOutside(false);

        scroll = (ScrollView) findViewById(R.id.scroll);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_update_no = (TextView) findViewById(R.id.tv_update_no);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_update_yes = (TextView) findViewById(R.id.tv_update_yes);
        /*WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;*/
        if (type == 1) {
            tv_update_no.setVisibility(View.GONE);
        } else {
            tv_update_no.setVisibility(View.VISIBLE);
        }
        tv_msg.setText(msg);
        tv_version.setText(AppUpdate.getVersionName(mContext) + "版本");
        tv_update_no.setOnClickListener(this);
        tv_update_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_yes:
                mOnCustomDialogListener.back("1");
                break;
            case R.id.tv_update_no:
                mOnCustomDialogListener.back("2");
                break;
        }
    }

}
