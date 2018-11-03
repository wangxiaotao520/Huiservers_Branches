package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;

public class ImgDialog extends Dialog implements OnClickListener{
	private LinearLayout ll_popup;
	private RelativeLayout parent;
	private Button  bt1 , bt2,bt3;
	private Context context;
	OnCustomDialogListener customDialogListener;
	//定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener{
		public void back(String name);
	}
	public ImgDialog(Context context,OnCustomDialogListener onCustomDialogListener) {
		super(context);
		this.context=context;
		this.customDialogListener=onCustomDialogListener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_file_photo);
		ll_popup = (LinearLayout) findViewById(R.id.ll_popup);
		//		parent = (RelativeLayout)findViewById(R.id.parent);
		bt1 = (Button) findViewById(R.id.item_popupwindows_camera);
		bt2 = (Button) findViewById(R.id.item_popupwindows_Photo);
		bt3 = (Button) findViewById(R.id.item_popupwindows_cancel);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		/*getWindow().setGravity(Gravity.BOTTOM); //此处可以设置dialog显示的位置
		getWindow().setWindowAnimations(R.style.take_photo_anim);  //添加动画
		WindowManager windowManager =getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();*/
//		getWindow().getDecorView().setPadding(0, 0, 0, 0); //就能够水平占满了
		/*lp.width = (int)(display.getWidth()); //设置宽度
		getWindow().setAttributes(lp); */

		// android 7.0系统解决拍照的问题
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.item_popupwindows_camera://拍照
			customDialogListener.back("1");
			ll_popup.clearAnimation();
			dismiss();
			break;
		case R.id.item_popupwindows_Photo://相册
			customDialogListener.back("2");
			ll_popup.clearAnimation();
			dismiss();
			break;
		case R.id.item_popupwindows_cancel://取消
			customDialogListener.back("3");
			ll_popup.clearAnimation();
			dismiss();
			break;
			/*case R.id.parent:
			ll_popup.clearAnimation();
			dismiss();
			break;*/

		default:
			break;
		}

	}

}
