package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class SeeAllOrderListAdapter extends BaseAdapter {
	private Context context;
	List<BannerBean> list;
	BitmapUtils bitmapUtils;
	public SeeAllOrderListAdapter(Context context,List<BannerBean> list){
		this.context=context;
		this.list=list;
		bitmapUtils=new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (null==arg1) {
			// 获得ViewHolder对象  
			holder = new ViewHolder();
			arg1=LinearLayout.inflate(context,R.layout.shop_all_order_item,null);
			holder.img_title=(ImageView) arg1.findViewById(R.id.img_title);
			holder.txt_name= (TextView) arg1.findViewById(R.id.txt_name);
			holder.txt_num= (TextView) arg1.findViewById(R.id.txt_num);
			holder.txt_price= (TextView) arg1.findViewById(R.id.txt_price);
			arg1.setTag(holder);
		}else{ 
			holder=(ViewHolder)arg1.getTag(); 
		} 
		bitmapUtils.display(holder.img_title, MyCookieStore.URL+list.get(arg0).getP_title_img());
		holder.txt_name.setText(list.get(arg0).getP_title());
		holder.txt_num.setText(list.get(arg0).getNumber());
		holder.txt_price.setText("¥"+list.get(arg0).getPrice());
		return arg1;
	}
	public  class ViewHolder {
		private ImageView img_title;
		private TextView txt_name,txt_num,txt_price;
	}

}
