package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.ListBean;

import java.util.List;

public class HorizontalScrollViewAdapter1 extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ListBean>  mDatas;

	public HorizontalScrollViewAdapter1(Context context, List<ListBean> mDatas)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return null;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_index_gallery_item, parent, false);

			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.id_index_gallery_item_text);

			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mText.setText(mDatas.get(position).getName());
		if (mDatas.get(position).isSelect()==true) {
//			viewHolder.mText.setBackgroundResource(R.drawable.chakan);
			viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
		}else {
			viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.grey));
//			viewHolder.mText.setBackgroundResource(R.drawable.order_delete);

		}
		return convertView;
	}

	private class ViewHolder
	{

		TextView mText;
	}

}
