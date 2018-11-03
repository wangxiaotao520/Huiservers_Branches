package com.huacheng.huiservers.shop.adapter;

import java.util.HashMap;
import java.util.List;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.adapter.GridViewAdapter.ViewHolder;
import com.huacheng.huiservers.geren.bean.GerenMapBean;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;

import android.R.bool;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddShopAdapter extends BaseAdapter{
	private int lastPosition = -1;//定义一个标记为最后选择的位置
	private Context context;
	private List<ShopDetailBean> beans;
	//状态标志位
	private int clickTemp = -1;
	//标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}
	public AddShopAdapter(Context context,List<ShopDetailBean> beans){
		this.context=context;
		this.beans=beans;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if(null==convertView){ 
			// 获得ViewHolder对象  
			holder = new ViewHolder(); 
			convertView=LinearLayout.inflate(context,R.layout.add_shop_item,null);
			holder.txt= (TextView)convertView.findViewById(R.id.txt);
			convertView.setTag(holder);
		}else{ 
			holder=(ViewHolder)convertView.getTag();
		} 
		holder.txt.setText(beans.get(position).getTagname());
		//选中时，改变背景色    实体类中添加判断字段
		if (beans.get(position).isSelect()==true) {
			holder.txt.setBackgroundResource(R.drawable.orange_onclick);
			holder.txt.setTextColor(context.getResources().getColor(R.color.orange));
		}else {
			holder.txt.setBackgroundResource(R.drawable.orange);
			holder.txt.setTextColor(context.getResources().getColor(R.color.gray2));
		}

		return convertView;
	}
	public class ViewHolder{
		private TextView txt;
	}

}
