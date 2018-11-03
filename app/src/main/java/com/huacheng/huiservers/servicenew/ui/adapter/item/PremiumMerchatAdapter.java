package com.huacheng.huiservers.servicenew.ui.adapter.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelInfoBean;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/4 10:56
 */
public class PremiumMerchatAdapter extends BaseAdapter {

    List<ModelInfoBean> datas;
    Context mContext;

    public PremiumMerchatAdapter(List<ModelInfoBean> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {//322dp 152dp
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_premium_merchant_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*String title = datas.get(position).getTitle();
        String titleImg = datas.get(position).getTitle_img();

        Glide.with(mContext).load(ApiHttpClient.IMG_SERVICE_URL + titleImg).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview).error(R.drawable.icon_girdview).into(holder.iv_cardView_popular);
        holder.tv_titleName.setText(title);*/



        return convertView;
    }



    class ViewHolder {
        ImageView iv_cardView_popular;
        TextView tv_titleName;

        public ViewHolder(View v) {
            iv_cardView_popular = v.findViewById(R.id.iv_cardView_popular);
            tv_titleName = v.findViewById(R.id.tv_titleName);

        }
    }
}
