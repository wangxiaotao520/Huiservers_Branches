package com.huacheng.huiservers.geren;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;

public class OrderSuccessActivity extends BaseUI implements OnClickListener {
	private TextView title_name;
	private LinearLayout lin_left;
	View v_head_line;
	@Override
	protected void init() {
		super.init();
		setContentView(R.layout.order_success_geren);
//		SetTransStatus.GetStatus(this);
		//标题栏
		title_name=(TextView) findViewById(R.id.title_name);
		lin_left=(LinearLayout) findViewById(R.id.lin_left);
		v_head_line=(View)findViewById(R.id.v_head_line);
		//get
		title_name.setText("");
		//listener
		lin_left.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_left:
			finish();
			break;
		default:
			break;
		}

	}

}
