package com.huacheng.huiservers.geren.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.huodong.ImagePagerActivity;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;

import java.util.ArrayList;
import java.util.List;

public class CircleItemImageAdapter extends BaseAdapter {

    //    private ArrayList<String> mList=null;
    private List<BannerBean> mBeans;
    private Context mContext;

    public CircleItemImageAdapter(Context context, List<BannerBean> beans) {//, ArrayList<String> list
        this.mBeans = beans;
        this.mContext = context;
//        this.mList = list;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mBeans.size() > 3) {
            return 3;
        } else {
            return mBeans.size();
        }
        /*if (mList.size() > 3) {
            return 3;
        } else {
            return mList.size();
        }*/
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.circle5_image_item,
                    null);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.rel_circleWhiteMarkImg = (RelativeLayout) view.findViewById(R.id.rel_circleWhiteMarkImg);
            holder.tv_picnum = (TextView) view.findViewById(R.id.tv_picnum);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide
                .with(mContext)
                .load(MyCookieStore.URL + mBeans.get(position).getImg())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.imageView);
        if (mBeans.size() >= 3) {
            if (position == 2) {
                holder.rel_circleWhiteMarkImg.setVisibility(View.VISIBLE);
                holder.tv_picnum.setText("共" + mBeans.size() + "张");
            }
        }

        /*Glide
                .with(mContext)
                .load(mList.get(position))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.imageView);
        if (mList.size() >= 3) {
            if (position == 2) {
                holder.rel_circleWhiteMarkImg.setVisibility(View.VISIBLE);
                holder.tv_picnum.setText("共" + mList.size() + "张");
            }
        }

        */
        final ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < mBeans.size(); i++) {
            lists.add(MyCookieStore.URL + mBeans.get(i).getImg());
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageBrower(position, lists);
            }
        });
        return view;
    }


    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }


    public class ViewHolder {
        private ImageView imageView;
        private RelativeLayout rel_circleWhiteMarkImg;
        private TextView tv_picnum;
    }
}
