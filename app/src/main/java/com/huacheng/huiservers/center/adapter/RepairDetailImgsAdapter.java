package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.huodong.ImagePagerActivity;
import com.huacheng.huiservers.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class RepairDetailImgsAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;

    public RepairDetailImgsAdapter(ArrayList<String> mList, Context context) {
        if (mList == null) {
            mList = new ArrayList<String>();
        }
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder view = null;
        if (convertView == null) {
            view = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.repair_detail_img, null);
            view.iv_repair_detail_img = (ImageView) convertView.findViewById(R.id.iv_repair_detail_img);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        Glide
                .with(mContext)
                .load(mList.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_girdview)
                .error(R.drawable.icon_girdview)
                .into(view.iv_repair_detail_img);

        view.iv_repair_detail_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageZoom(position, mList);
            }
        });
        return convertView;
    }

    /***
     * 图片放大
     */
    private void ImageZoom(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    public class ViewHolder {
        private ImageView iv_repair_detail_img;
    }
}
