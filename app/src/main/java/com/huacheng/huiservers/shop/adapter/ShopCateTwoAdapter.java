package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.CateBean;
import com.huacheng.huiservers.view.MyGridview;

import java.util.List;

public class ShopCateTwoAdapter extends BaseAdapter{
	List<CateBean> mList;
	private Context mContext;
	ShopCateThereAdapter adapter;
	String str_img;

	public ShopCateTwoAdapter(Context mContext,List<CateBean> mList) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		} else {
			return this.mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		//return 0;
		if (mList == null) {
			return null;
		} else {
			return this.mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_cate_item, null, false);		
			holder.txt_one = (TextView) convertView.findViewById(R.id.txt_one);
			holder.myGridView = (MyGridview) convertView.findViewById(R.id.myGridView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_one.setText(mList.get(position).getCate_name());
		adapter = new ShopCateThereAdapter(mContext,mList.get(position).getSub_arr());
		holder.myGridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		return convertView;
	}
	private class ViewHolder {
		TextView txt_one;
		MyGridview myGridView;
	}

}
