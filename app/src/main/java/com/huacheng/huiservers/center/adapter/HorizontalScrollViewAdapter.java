package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

public class HorizontalScrollViewAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<BannerBean> mDatas;

	public HorizontalScrollViewAdapter(Context context, List<BannerBean> mDatas)
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

		Log.e("qqqqqqqqqq",mDatas.size()+"个");
		viewHolder.mText.setText(mDatas.get(position).getC_name());
		if (mDatas.get(position).isSelect()==true) {
			//viewHolder.mText.setBackgroundResource(R.drawable.chakan);
			viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.orange));
		}else {
			viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.gray2));
			viewHolder.mText.setBackgroundResource(R.drawable.order_delete);

		}
		return convertView;
	}

	private class ViewHolder
	{

		TextView mText;
	}

}
