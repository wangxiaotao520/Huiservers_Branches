package com.huacheng.huiservers.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huacheng.libraryservice.dialog.SmallDialog;

public abstract class BaseFragmentOld extends Fragment{
	
	protected Context context;
    protected View mView;
    protected Activity mActivity;
    
    @Override
    public void onPause() {
        super.onPause();
    }

    protected SmallDialog smallDialog;//等待对话框

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity=activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getActivity().getApplicationContext();
    }
    
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        smallDialog=new SmallDialog(mActivity);
        smallDialog.setCanceledOnTouchOutside(false);
        if (getContentViewLayoutID() != 0) {
            mView=inflater.inflate(getContentViewLayoutID(), null);
           
            initView();
            return mView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    protected <T extends View> T findViewById(int resid){
        return (T) mView.findViewById(resid);
    }

    protected <T extends ViewGroup> T findViewGroupById(int resid){
        return (T) mView.findViewById(resid);
    }
    protected abstract void initView();

    protected abstract int getContentViewLayoutID();

    @Override
    public void onDestroy() {
        hideDialog(smallDialog);
        super.onDestroy();
    }
    /**
     * 显示对话框（防止窗体溢出）
     * @param mDialog
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showDialog(Dialog mDialog) {
        if(mDialog != null) {
            Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();
            if(context instanceof Activity) {
                if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                    mDialog.show();
            } else {
                mDialog.show();
            }
        }
    }

    /**
     * 隐藏对话框 （防止窗体溢出）
     * @param mDialog
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void hideDialog(Dialog mDialog) {
        if(mDialog != null && mDialog.isShowing()) {
            Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();
            if(context instanceof Activity) {
                if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                    mDialog.dismiss();
            } else {
                mDialog.dismiss();
            }
//				mDialog = null;
        }
    }
}
