package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.GroupMemberBean;

import java.util.HashMap;
import java.util.List;

public class XiaoquAdapter extends BaseAdapter implements SectionIndexer{
	Context context;
	//List<CityBean> beans;
	List<GroupMemberBean> list;
	public static HashMap<Integer, Boolean> isSelected;
	public XiaoquAdapter(Context context,List<GroupMemberBean> list){
		this.context=context;
		this.list=list;
		isSelected=new HashMap<Integer, Boolean>();
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<GroupMemberBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		XiaoquAdapter.isSelected = isSelected;
	}
	@Override
	public int getCount() {
		if (list.size()==0) {
			return 0;
		}else {
			return list.size();
		}
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		final GroupMemberBean mContent = list.get(position);
		if(null==convertView){ 
			// 获得ViewHolder对象  
			holder = new ViewHolder(); 
			convertView=LinearLayout.inflate(context,R.layout.activity_group_member_item,null);
			holder.text=(TextView) convertView.findViewById(R.id.text);
			holder.rb_state = (RadioButton)convertView.findViewById(R.id.radiobutton);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);

			convertView.setTag(holder);
		}else{ 
			holder=(ViewHolder)convertView.getTag();
		} 
		holder.text.setText(list.get(position).getName());
		holder.rb_state.setFocusable(false);// 无此句点击item无响应的
		//holder.rb_state.setChecked(getIsSelected().get(position));

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(mContent.getSortLetters());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder{
		private TextView text,tvLetter;
		private RadioButton rb_state;;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
}
