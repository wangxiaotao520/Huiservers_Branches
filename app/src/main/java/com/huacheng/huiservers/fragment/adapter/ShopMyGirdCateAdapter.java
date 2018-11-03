package com.huacheng.huiservers.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.shop.ShopCateActivity;
import com.huacheng.huiservers.shop.ShopListActivity;
import com.huacheng.huiservers.utils.MyCookieStore;

import java.util.List;

/**
 * 类：
 * 时间：2018/5/24 09:22
 * 功能描述:Huiservers
 */
public class ShopMyGirdCateAdapter extends BaseAdapter {
    Context mContext;
    private List<ShopIndexBean> mShopList;

    public ShopMyGirdCateAdapter(Context context, List<ShopIndexBean> mShopList) {

        this.mContext = context;
        this.mShopList = mShopList;

    }

    @Override
    public int getCount() {
        if (mShopList.size() <= 9) {

            return mShopList.size();

        } else if (mShopList.size() >= 10) {

            return 10;

        } else
            return mShopList.size();

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_my_cate_item, null);
            holder = new ViewHoldler(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHoldler) convertView.getTag();
        }


        if (mShopList.size() <= 9) {
            Glide.with(mContext)
                    .load(MyCookieStore.URL + mShopList.get(position).getIcon())
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_girdview)
                    .into(holder.iv_img);

            holder.tv_name.setText(mShopList.get(position).getCate_name());

            holder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra("cateID", mShopList.get(position).getId());
                        mContext.startActivity(intent);

                }
            });

        } else if (mShopList.size() >= 10) {
            if (position == 9) {
                holder.tv_name.setText("全部");
                holder.iv_img.setImageResource(R.drawable.iv_icon_all);
                holder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShopCateActivity.class);
                        mContext.startActivity(intent);

                    }
                });
            } else {
                Glide.with(mContext)
                        .load(MyCookieStore.URL + mShopList.get(position).getIcon())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.icon_girdview)
                        .into(holder.iv_img);
                holder.tv_name.setText(mShopList.get(position).getCate_name());
                holder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra("cateID", mShopList.get(position).getId());
                        mContext.startActivity(intent);
                    }
                });
            }

        }

        return convertView;
    }

    class ViewHoldler {
        ImageView iv_img;
        TextView tv_name;
        LinearLayout ly_onclick;

        public ViewHoldler(View v) {
            iv_img = (ImageView) v.findViewById(R.id.iv_img);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            ly_onclick = (LinearLayout) v.findViewById(R.id.ly_onclick);

        }
    }
}
