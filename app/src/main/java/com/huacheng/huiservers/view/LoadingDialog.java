package com.huacheng.huiservers.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.huacheng.huiservers.R;

public class LoadingDialog {
    private Context mContext;
    private Dialog mDialog;
    //private LoadingView mLoadingView;
    private View mDialogContentView;

    public LoadingDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        //	mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setContentView(mDialogContentView);
       // mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
       //mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
       /// mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_PHONE));
       mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_TOAST));
        setCanceledOnTouchOutside(false);
    }

    public void setBackground(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence) {
        //mLoadingView.setLoadingText(charSequence);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() { return mDialog.isShowing();}

    public Dialog getDialog() {
        return mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

}
