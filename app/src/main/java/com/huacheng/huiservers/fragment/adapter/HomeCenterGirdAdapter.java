package com.huacheng.huiservers.fragment.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

/**
 * 类：
 * 时间：2018/5/24 09:22
 * 功能描述:Huiservers
 */
public class HomeCenterGirdAdapter extends BaseAdapter {
    Context mContext;
    List<BannerBean> list;
    Jump jump;

    public HomeCenterGirdAdapter(Context context, List<BannerBean> list) {

        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoldler holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_center_grid_item_img, null);
            holder = new ViewHoldler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHoldler) convertView.getTag();
        }
        /*//获取图片的宽高--start
        holder.iv_banner.getLayoutParams().width = ToolUtils.getScreenWidth(mContext);
        Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext)) / (Double.valueOf(list.get(0).getImg_size()));
        holder.iv_banner.getLayoutParams().height = (new Double(d)).intValue();*/
        Glide.with(mContext)
                .load(list.get(position).getImg())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.iv_banner);
        holder.iv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(list.get(position).getUrl())) {
                    if (list.get(position).getUrl_type().equals("0") || TextUtils.isEmpty(list.get(position).getUrl_type())) {
                        jump = new Jump(mContext, list.get(position).getType_name(), list.get(position).getAdv_inside_url());
                    } else {
                        jump = new Jump(mContext, list.get(position).getUrl_type(), list.get(position).getType_name(), "", list.get(position).getUrl_type_cn());
                    }
                } else {//URL不为空时外链
                    jump = new Jump(mContext, list.get(position).getUrl());

                }
            }
        });
        return convertView;
    }

    class ViewHoldler {

        ImageView iv_banner;

        public ViewHoldler(View v) {

            iv_banner = (ImageView) v.findViewById(R.id.iv_banner);

        }
    }
}
