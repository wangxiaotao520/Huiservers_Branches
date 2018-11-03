package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.ShopMainBean;

import java.util.List;

public class ShopDetailListAdapter extends BaseAdapter {
	private Context context;
	List<ShopMainBean> bean;
	String num;
	public ShopDetailListAdapter(Context context,List<ShopMainBean> bean){
		this.context=context;
		this.bean=bean;
	}

	@Override
	public int getCount() {
		/*if (num.equals("all")) {
			return bean.size();
		}else {
			if (bean.size()>3) {
				return 3;
			}else {
				return bean.size();
			}
		}*/
		return bean.size();

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
			arg1=LinearLayout.inflate(context,R.layout.shop_detail_list_item,null);
			holder.room_ratingbar=(RatingBar) arg1.findViewById(R.id.room_ratingbar);
			holder.txt_name=(TextView) arg1.findViewById(R.id.txt_name);
			holder.txt_content=(TextView) arg1.findViewById(R.id.txt_content);
			arg1.setTag(holder);
		}else{ 
			holder=(ViewHolder)arg1.getTag(); 
		} 
		holder.room_ratingbar.setRating(Float.parseFloat(bean.get(arg0).getScore()));
		holder.txt_name.setText(bean.get(arg0).getUsername());
		holder.txt_content.setText(bean.get(arg0).getDescription());
		return arg1;
	}
	public  class ViewHolder {
		private TextView txt_name,txt_content;
		private RatingBar room_ratingbar;
	}

}
