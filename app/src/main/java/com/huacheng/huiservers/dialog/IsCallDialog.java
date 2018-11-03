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
 * Created by hwh on 2017/11/28.
 */

public class IsCallDialog implements View.OnClickListener {

    private Activity mcontext;
    private Dialog mDialog;
    private String phone;
    private TextView tv_1,tv_2,textView;
    View v;
    public IsCallDialog(final Activity context, final String phone) {
        this.mcontext = context;
        this.phone = phone;
        v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_is_call, null);
        tv_1 = (TextView) v.findViewById(R.id.tv_1);
        tv_2 = (TextView) v.findViewById(R.id.tv_2);
        textView = (TextView) v.findViewById(R.id.textView);

        mDialog = new AlertDialog.Builder(context).create();
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (!TextUtils.isEmpty(phone)) {
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 没有获得授权，申请授权
                        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                                Manifest.permission.CALL_PHONE)) {
                            // 返回值：
                            //	                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                            //	                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                            //	                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                            // 弹窗需要解释为何需要该权限，再次请求授权
                            XToast.makeText(context, "请授权！", XToast.LENGTH_SHORT).show();
                            // 帮跳转到该应用的设置界面，让用户手动授权
                            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent1.setData(uri);
                            context.startActivity(intent1);
                        } else {
                            // 不需要解释为何需要该权限，直接请求授权
                            ActivityCompat.requestPermissions(context,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        }
                    } else {
                        // 已经获得授权，可以打电话
                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        context.startActivity(intent1);
                        //getPhone();
                    }
                }
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
