package com.huacheng.libraryservice.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.huacheng.libraryservice.base.ActivityStackManager;


/***
 * 双击退出
 * @author dong.he
 */
public class DoubleClickExitHelper {

	private final Activity mActivity;
	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;
	private Runnable onBackTimeRunnable;

	public DoubleClickExitHelper(Activity activity) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
		initRunnable();
	}
	
	/**
	 * Activity onKeyDown事件
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if(isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if(mBackToast != null){
				mBackToast.cancel();
			}
			// 退出
			mActivity.finish();
			ActivityStackManager.getActivityStackManager().finishAllActivity();

			System.exit(0);
			return true;
		} else {
			isOnKeyBacking = true;
			if(mBackToast == null) {
				mBackToast = Toast.makeText(mActivity, "再按一次退出", Toast.LENGTH_SHORT);
			}
			mBackToast.show();
			mHandler.postDelayed(onBackTimeRunnable, 2000);
			return true;
		}
	}

	private void initRunnable() {
		onBackTimeRunnable = new Runnable() {
			@Override
			public void run() {
				isOnKeyBacking = false;
				if(mBackToast != null)
					mBackToast.cancel();
			}
		};
	}

}
