package com.huacheng.huiservers.oldhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
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
import com.huacheng.huiservers.cricle.Circle2DetailsActivity;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.view.CircularImage;

import java.util.List;

import static com.huacheng.huiservers.R.id.tv_title;

/**
 * 类：
 * 时间：2018/6/1 09:33
 * 功能描述:Huiservers
 */
public class OldListQzAdapter extends BaseAdapter {
    List<BannerBean> mList;
    Context mContext;

    public OldListQzAdapter(Context context, List<BannerBean> mList) {
        this.mList = mList;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.old_list_all_q_item,
                    null);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.iv_head = (CircularImage) convertView.findViewById(R.id.iv_head);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_title = (TextView) convertView.findViewById(tv_title);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_circleViews = (TextView) convertView.findViewById(R.id.tv_circleViews);
            holder.tv_circleReply = (TextView) convertView.findViewById(R.id.tv_circleReply);
            holder.ly_onclick = (LinearLayout) convertView.findViewById(R.id.ly_onclick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        byte[] bytes1 = Base64.decode(mList.get(position).getTitle(), Base64.DEFAULT);
        holder.tv_title.setText(new String(bytes1));
        holder.tv_name.setText(mList.get(position).getNickname() + "   发布于  " + mList.get(position).getC_name());
        holder.tv_time.setText(mList.get(position).getAddtime());
        byte[] bytes2 = Base64.decode(mList.get(position).getContent(), Base64.DEFAULT);
        holder.tv_content.setText(new String(bytes2));
      /*  System.out.println("holder.tv_title.(getLineCount)****"+holder.tv_title.getLineCount());
        int with = holder.tv_title.getLineCount();*/
       /* final int with1 = new String(bytes1).length();
        holder.tv_title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (width <= with1) {
                    finalHolder.tv_content.setLines(3);
                } else {
                    finalHolder.tv_content.setLines(2);
                }
            }
        });*/
        holder.tv_circleViews.setText(mList.get(position).getClick());
        holder.tv_circleReply.setText(mList.get(position).getReply_num());
        if (mList.get(position).getImg_list() != null && mList.get(position).getImg_list().size() > 0) {
            if (!TextUtils.isEmpty(mList.get(position).getImg_list().get(0).getImg())) {
                Glide
                        .with(mContext)
                        .load(MyCookieStore.URL + mList.get(position).getImg_list().get(0).getImg())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.icon_girdview)
                        .into(holder.iv_img);
                holder.iv_img.setVisibility(View.VISIBLE);
            } else {
                holder.iv_img.setVisibility(View.GONE);
            }
        } else {
            holder.iv_img.setVisibility(View.GONE);
        }

        Glide
                .with(mContext)
                .load(MyCookieStore.URL + mList.get(position).getAvatars())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview)
                .into(holder.iv_head);


        holder.ly_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Circle2DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", mList.get(position).getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private CircularImage iv_head;
        private LinearLayout ly_onclick;
        private ImageView iv_img;
        private TextView tv_time, tv_content, tv_circleViews, tv_circleReply, tv_name, tv_title;
    }
}
