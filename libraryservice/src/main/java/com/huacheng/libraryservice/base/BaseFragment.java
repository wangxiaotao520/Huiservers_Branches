package com.huacheng.libraryservice.base;

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

import java.lang.reflect.Field;


/**
 * Description:基类Fragment
 * Author: wangxiaotao
 * Create: 2018/6/11 16:40
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected Activity mActivity;
    protected SmallDialog smallDialog;//等待对话框
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity=activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        smallDialog=new SmallDialog(mActivity);
        initIntentData();
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mContext = null;
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 初始化view
     */
    public abstract void initView(View view);

    /**
     * 初始化intentdata
     */
    public abstract void initIntentData();

    /**
     * 初始化监听
     */
    public abstract void initListener();

    /**
     * 初始化数据
     */
    public abstract void initData(Bundle savedInstanceState);
    /**
     * 给出layoutId,获取layout
     */
    public abstract int getLayoutId();


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


