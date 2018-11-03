package com.huacheng.huiservers.shop;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.SeeAllOrderListAdapter;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.shop.bean.SubmitOrderBean;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SeeAllOrderActivity extends BaseUI implements OnClickListener {
	private LinearLayout lin_left;
	private TextView title_name;
	List<SubmitOrderBean> pro=new ArrayList<SubmitOrderBean>();
	private ListView list_center;
	ShopProtocol protocol=new ShopProtocol();
	SeeAllOrderListAdapter allOrderListAdapter;
	List<BannerBean> beans=new ArrayList<BannerBean>();
	String m_id;

	@Override
	protected void init() {
		super.init();
		setContentView(R.layout.queren_order_all);
//		SetTransStatus.GetStatus(this);//系统栏默认为黑色
		pro = (List<SubmitOrderBean>) getIntent().getExtras().getSerializable("pro");
		m_id=this.getIntent().getExtras().getString("m_id");
		lin_left=(LinearLayout) findViewById(R.id.lin_left);
		lin_left.setOnClickListener(this);
		title_name=(TextView) findViewById(R.id.title_name);
		title_name.setText("确认订单列表");
		list_center=(ListView) findViewById(R.id.list_center);

	}
	@Override
	protected void initData() {
		super.initData();
		getAllShopOrder();

	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_left://返回
			finish();
			break;
		default:
			break;
		}

	}
	private void getAllShopOrder() {//所有确定的订单商品列表
		showDialog(smallDialog);
		Url_info info=new Url_info(this);
		Gson gson=new Gson();
		gson.toJson(pro);
		List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("m_id",m_id );
		nameValuePair.add(new BasicNameValuePair("products", gson.toJson(pro)));  
		params.addBodyParameter(nameValuePair);
		System.out.println("======="+gson.toJson(pro));
		HttpHelper hh=new HttpHelper(info.order_info,params,SeeAllOrderActivity.this) {

			@Override
			protected void setData(String json) {
				hideDialog(smallDialog);
				beans=protocol.getAllOrder(json);
				allOrderListAdapter=new SeeAllOrderListAdapter(SeeAllOrderActivity.this,beans);
				list_center.setAdapter(allOrderListAdapter);

			}

			@Override
			protected void requestFailure(Exception error, String msg) {
				hideDialog(smallDialog);
				UIUtils.showToastSafe("网络异常，请检查网络设置");
			}
		};
	}
}
