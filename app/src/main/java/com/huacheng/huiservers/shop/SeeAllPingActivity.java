package com.huacheng.huiservers.shop;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.ShopDetailListAdapter;
import com.huacheng.huiservers.shop.bean.ShopMainBean;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class SeeAllPingActivity extends BaseUI implements OnClickListener{
	private LinearLayout lin_left,lin_jiesuan;
	private RelativeLayout title_rel,rel_no_data;
	private TextView title_name;
	//private Zks_FL_ListView list_center;
	private ListView list_center;
	private SmartRefreshLayout refreshLayout;

	private String shop_id;
	private int page = 1;//当前页
	int totalPage = 0;//总页数
	private int count=0;
	ShopDetailListAdapter listAdapter;
	ShopProtocol protocol=new ShopProtocol();
	List<ShopMainBean> bean=new ArrayList<ShopMainBean>();
	List<ShopMainBean> beans=new ArrayList<ShopMainBean>();
	@Override
	protected void init() {
		super.init();
		setContentView(R.layout.order_history);
//		SetTransStatus.GetStatus(this);//系统栏默认为黑色
		shop_id=this.getIntent().getExtras().getString("shop_id");
		lin_left=(LinearLayout) findViewById(R.id.lin_left);
		lin_left.setOnClickListener(this);
		title_name=(TextView) findViewById(R.id.title_name);
		title_name.setText("商品评价");

		rel_no_data=(RelativeLayout) findViewById(R.id.rel_no_data);
		list_center=(ListView) findViewById(R.id.list_center);
		beans= new ArrayList<ShopMainBean>();
		//map.put("page", page + "");
		listAdapter=new ShopDetailListAdapter(SeeAllPingActivity.this,beans);
		list_center.setAdapter(listAdapter);
		refreshLayout=findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull RefreshLayout refreshLayout) {
				page = 1;
				getpingjia();
			}
		});
		refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
				getpingjia();
			}
		});
//		list_center.setReflshInterface(this);
//		list_center.setLoadInterface(this);


	}
	@Override
	protected void initData() {
		super.initData();
		showDialog(smallDialog);
		getpingjia();
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
	private void getpingjia() {//评价列表
		Url_info info=new Url_info(this);
		String url=info.goods_review+"/id/"+shop_id+"/p/"+page;
		HttpHelper hh=new HttpHelper(url,SeeAllPingActivity.this) {

			@Override
			protected void setData(String json) {
				hideDialog(smallDialog);
				refreshLayout.finishRefresh();
				refreshLayout.finishLoadMore();
				bean=protocol.getpingjia(json);
				if (bean.size()!=0) {
					list_center.setVisibility(View.VISIBLE);
					rel_no_data.setVisibility(View.GONE);
					System.out.println("$$$$$$$$$$$"+bean.size());
					if (page==1){
						beans.clear();
					}
					beans.addAll(bean);
					page++;
					listAdapter.notifyDataSetChanged();
//					list_center.setCount(count);
//					list_center.reflashComplete();// 上面
//					list_center.loadComplete();
					totalPage = Integer.valueOf(beans.get(0).getTotal_Pages());// 设置总页数
					if (page>totalPage){
						refreshLayout.setEnableLoadMore(false);
					}else {
						refreshLayout.setEnableLoadMore(true);
					}
				}else {
					if (page==1){
						list_center.setVisibility(View.GONE);
						rel_no_data.setVisibility(View.VISIBLE);
					}
					refreshLayout.setEnableLoadMore(false);
				}
			}

			@Override
			protected void requestFailure(Exception error, String msg) {
				refreshLayout.finishRefresh();
				refreshLayout.finishLoadMore();
				refreshLayout.setEnableLoadMore(false);
				hideDialog(smallDialog);
				UIUtils.showToastSafe("网络异常，请检查网络设置");
			}
		};
	}
//	/**
//	 * 下拉刷新
//	 */
//	@Override
//	public void onReflash() {
//		list_center.setCount(bean.size());
//		list_center.setLoad(true);
//		page = 1;
//		beans= new ArrayList<ShopMainBean>();
//		//map.put("page", page + "");
//		listAdapter=new ShopDetailListAdapter(SeeAllPingActivity.this,beans);
//		list_center.setAdapter(listAdapter);
//		getpingjia();
//	}
//
//	/**
//	 * 上拉加载
//	 */
//	@Override
//	public void onLoad() {
//		System.out.println(page+"------"+totalPage+"------");
//		if (page < totalPage) {
//			page++;
//			//map.put("page", page + "");
//			getpingjia();
//		} else {
//			list_center.loadComplete();// 下面
//			list_center.setLoad(false);
//		}
//
//	}
}
