package com.huacheng.huiservers.fragment.shop;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/8/11 10:26
 */
public class ShopBannerAdapter extends BaseAdapter {

    private Context mContext;
    List<BannerBean> mBeansCenter;

    public ShopBannerAdapter(List<BannerBean> beansCenter, Context context) {
        this.mBeansCenter = beansCenter;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mBeansCenter.size();
    }//

    @Override
    public Object getItem(int i) {
        return mBeansCenter.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View v, ViewGroup viewGroup) {
        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Glide.with(mContext)
                .load(mBeansCenter.get(i).getImg())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.iv_banner);

        holder.iv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBeansCenter.get(i).getUrl())) {
                    if (mBeansCenter.get(i).getUrl_type().equals("0") || TextUtils.isEmpty(mBeansCenter.get(i).getUrl_type())) {
                        new Jump(mContext, mBeansCenter.get(i).getType_name(), mBeansCenter.get(i).getAdv_inside_url());
                    } else {
                        new Jump(mContext, mBeansCenter.get(i).getUrl_type(), mBeansCenter.get(i).getType_name(), "", mBeansCenter.get(i).getUrl_type_cn());
                    }
                } else {//URL不为空时外链
                    new Jump(mContext, mBeansCenter.get(i).getUrl());

                }
            }
        });

       /* holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort(mContext.getApplicationContext(), "banner");
                *//*Intent intent = new Intent(mContext, ShopDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", shopIndex.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);*//*
            }
        });*/

        return v;
    }


    class ViewHolder {
        ImageView iv_banner;
        LinearLayout linear;

        public ViewHolder(View v) {
            iv_banner = (ImageView) v.findViewById(R.id.iv_banner);
            linear = v.findViewById(R.id.linear);

        }
    }

}
