package com.huacheng.huiservers.geren.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.GerenMapBean;

import java.util.HashMap;
import java.util.List;

public class GridViewAdapter extends BaseAdapter{
	private Context context;
	public static List<GerenMapBean> list;
	private static HashMap<Integer, Boolean> isSelected; 
	public GridViewAdapter(Context context,	List<GerenMapBean> list){
		this.context=context;
		this.list=list;
		isSelected = new HashMap<Integer, Boolean>();  
		// 初始化isSelected的数据  
		for (int i = 0; i < list.size(); i++) {  
			getIsSelected().put(i, false);  
		}  

	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		GridViewAdapter.isSelected = isSelected;
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if(null==convertView){ 
			// 获得ViewHolder对象  
			holder = new ViewHolder(); 
			convertView=LinearLayout.inflate(context,R.layout.gridview_child,null);
			holder.ck = (CheckBox)convertView.findViewById(R.id.ck);
			convertView.setTag(holder);
		}else{ 
			holder=(ViewHolder)convertView.getTag();
		} 

		holder.ck.setOnClickListener(new View.OnClickListener() {  

			public void onClick(View v) {  

				if (isSelected.get(arg0)) {  
					isSelected.put(arg0, false);  
					setIsSelected(isSelected);  
				} else {  
					isSelected.put(arg0, true);  
					setIsSelected(isSelected);  
				} 
				setData();
			}  
		});  

		// 根据isSelected来设置checkbox的选中状况  
		holder.ck.setChecked(getIsSelected().get(arg0));  
		holder.ck.setText(list.get(arg0).getValue());

		return convertView;
	}
	public class ViewHolder{
		private CheckBox ck;;

	}
	public void setData(){
		GridViewAdapter.this.notifyDataSetChanged();
	}
}
