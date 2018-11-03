package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.huacheng.huiservers.R;

public class DeleteAddressDialog extends Dialog implements OnClickListener{

	private Context context;
	private TextView txt_delete,txt_edit;
	private OnCustomDialogListener customDialogListener;
	//定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener{
		public void back(String name);
	}
	public DeleteAddressDialog(Context context, OnCustomDialogListener onCustomDialogListener) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context=context;
		this.customDialogListener = onCustomDialogListener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delete_address_diaolg);
		txt_edit=(TextView) findViewById(R.id.txt_edit);
		txt_delete=(TextView) findViewById(R.id.txt_delete);
		txt_edit.setOnClickListener(this);
		txt_delete.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.txt_edit:
			customDialogListener.back("1");
			dismiss();
			break;
		case R.id.txt_delete:
			customDialogListener.back("2");
			dismiss();
		default:
			break;
		}

	}

}
