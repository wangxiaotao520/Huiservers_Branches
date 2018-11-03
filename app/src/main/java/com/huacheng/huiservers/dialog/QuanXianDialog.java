package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.huacheng.huiservers.R;

/**
 * 类：
 * 时间：2018/3/20 16:23
 * 功能描述:Huiservers
 */
public class QuanXianDialog extends Dialog {
    public QuanXianDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanxian_dialog);
        LinearLayout lin_btn = (LinearLayout) findViewById(R.id.lin_btn);

        lin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
