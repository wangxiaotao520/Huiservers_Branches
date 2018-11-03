package com.huacheng.huiservers.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.XToast;

/**
 * Created by hwh on 2018/1/3.
 */

public class IsChooseXiaoquDialog implements View.OnClickListener {

    private Activity mcontext;
    private Dialog mDialog;
    private TextView tv_1,tv_2,textView;
    private LeaveMeetingDialogListener listener;
    View v;

    public interface LeaveMeetingDialogListener{
        public void onClick(String data);
    }


    public IsChooseXiaoquDialog(final Activity context,final String type, final LeaveMeetingDialogListener listener) {
        this.mcontext = context;
        this.listener = listener;
        v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_is_call, null);
        tv_1 = (TextView) v.findViewById(R.id.tv_1);
        tv_2 = (TextView) v.findViewById(R.id.tv_2);
        textView = (TextView) v.findViewById(R.id.textView);
      /*  if (type.equals("splash")){
            tv_1.setVisibility(View.GONE);
        }
*/
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);

        textView.setText("选择小区\n" +
                "\n" +
                "·选择小区后，仅显示本小区的商品和购物车信息\n" +
                "\n" +
                "·首页顶部可重新选择小区");

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick("");
                mDialog.dismiss();
            }
        });


    }
    @Override
    public void onClick(View v) {
        mDialog.dismiss();
    }
    public void dismiss(){
        mDialog.dismiss();
    }

    public void show(){
        mDialog.show();
        mDialog.getWindow().setContentView(v);
        WindowManager windowManager = mcontext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()-130); //设置宽度
        mDialog.getWindow().setAttributes(lp);
    }


}
