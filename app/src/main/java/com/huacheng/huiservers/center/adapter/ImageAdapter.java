package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class ImageAdapter extends BaseAdapter{

	private Context context;
	private BitmapUtils bitmapUtils1;
	List<BannerBean> beans;

	public ImageAdapter(Context context, List<BannerBean> beans){
		this.context=context;
		this.beans=beans;
		bitmapUtils1 = new BitmapUtils(context);
	}
	@Override
	public int getCount() {
		if (beans.size()==0) {
			return 0;
		}else if(beans.size()>=3)
			return 3;
		else{
			return beans.size();
		}
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (view==null) {
			holder = new ViewHolder(); 
			view = LayoutInflater.from(context).inflate(R.layout.image_item, null);
			holder.img = (ImageView) view.findViewById(R.id.img);
			view.setTag(holder);
		}else{ 
			holder=(ViewHolder)view.getTag(); 
		} 
		bitmapUtils1.display(holder.img, MyCookieStore.URL+beans.get(arg0).getOne_img());
		return view;
	}
	class ViewHolder{
		private  ImageView img;

	}
}
