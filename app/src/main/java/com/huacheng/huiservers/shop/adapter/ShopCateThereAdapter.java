package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.ShopListActivity;
import com.huacheng.huiservers.shop.bean.CateBean;
import com.huacheng.huiservers.utils.MyCookieStore;

import java.util.List;

/**
 * GridView加载数据的适配器
 * @author Administrator
 *
 */
public class ShopCateThereAdapter extends BaseAdapter{

	private Context context;
	private List<CateBean> lists;//数据源

	public ShopCateThereAdapter(Context context, List<CateBean> lists
			) {
		this.context = context;
		this.lists = lists;
	}


	@Override
	public int getCount() {
		if (lists == null) {
			return 0;
		} else {
			return this.lists.size();
		}
	}

	@Override
	public Object getItem(int position) {
		//return 0;
		if (lists == null) {
			return null;
		} else {
			return this.lists.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_view, null);
			holder.ly = (LinearLayout)convertView.findViewById(R.id.ly);
			holder.tv_name = (TextView)convertView.findViewById(R.id.item_name);
			holder.iv_nul = (ImageView)convertView.findViewById(R.id.item_image);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_name.setText(lists.get(position).getCate_name());
		holder.ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					Intent intent = new Intent(context, ShopListActivity.class);
					intent.putExtra("cateID", lists.get(position).getId());
					context.startActivity(intent);

			}
		});
		Glide
		.with(context)
		.load(MyCookieStore.URL +lists.get(position).getIcon())
		.skipMemoryCache(true)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.placeholder(R.drawable.icon_girdview)
		.into(holder.iv_nul);

		return convertView;
	}
	static class ViewHolder{
		private TextView tv_name;
		private LinearLayout ly;
		private ImageView iv_nul;
	}
}
