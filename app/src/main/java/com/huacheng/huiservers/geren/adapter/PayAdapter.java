package com.huacheng.huiservers.geren.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.PayTypeBean;
import com.huacheng.huiservers.utils.MyCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：
 * 时间：2018/1/5 17:51
 * 功能描述:Huiservers
 */
public class PayAdapter extends BaseAdapter {

    List<PayTypeBean> mTypeBeen = new ArrayList<>();
    Context mContext;

    public PayAdapter(Context mContext, List<PayTypeBean> mTypeBeen) {
        this.mContext = mContext;
        this.mTypeBeen = mTypeBeen;

    }

    @Override
    public int getCount() {
        return mTypeBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.zhifu_item,
                    null);
            holder.icon_type = (ImageView) convertView.findViewById(R.id.icon_type);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_name.setText(mTypeBeen.get(position).getByname());
        holder.txt_desc.setText(mTypeBeen.get(position).getP_introduction());
        Glide
                .with(mContext)
                .load(MyCookieStore.URL+mTypeBeen.get(position).getIcon())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.icon_type);


        return convertView;
    }

    public class ViewHolder {
        private ImageView icon_type;
        private TextView txt_name,txt_desc;
    }
}
