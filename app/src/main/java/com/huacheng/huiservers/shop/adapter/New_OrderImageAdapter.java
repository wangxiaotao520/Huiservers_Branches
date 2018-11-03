package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;

import java.util.List;

public class New_OrderImageAdapter extends BaseAdapter {

    private Context context;
    private List<BannerBean> list;

    public New_OrderImageAdapter(Context context, List<BannerBean> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
        /*if (beans.size()==0) {
            return 0;
		}else if(beans.size()>=3)
			return 3;
		else{
			return beans.size();
		}*/
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
    public View getView(int arg0, View view, ViewGroup arg2) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.image_order_item, null);
            holder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context)
                .load(MyCookieStore.URL + list.get(arg0).getImg())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.img);

        //bitmapUtils1.display(holder.img,MyCookieStore.URL+beans.get(arg0).getOne_img());
        return view;
    }

    class ViewHolder {
        private ImageView img;

    }
}
