package com.huacheng.huiservers.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SetTransStatus {
	public static void GetStatus(Context context){
		// 1、4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true,context);
		}
		/*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
			((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			try {
				Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
				Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
				field.setAccessible(true);
				field.setInt(((Activity) context).getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
			} catch (Exception e) {
			}
		}*/
		SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);

		// 自定义颜色
		tintManager.setTintColor(Color.TRANSPARENT);
		//tintManager.setTintColor(Color.parseColor("#FEFEFE"));
	}
	@TargetApi(19)
	private static void setTranslucentStatus(boolean on,Context context) {
		Window win =((Activity) context).getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
