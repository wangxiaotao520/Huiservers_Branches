package com.huacheng.huiservers.cricle.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.bean.CircleDetail4Bean;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.view.CircularImage;

import java.util.List;

/**
 * 类：
 * 时间：2018/3/16 17:31
 * 功能描述:Huiservers
 */
public class Circle2detailListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CircleDetail4Bean> mList;

    public Circle2detailListAdapter(Context mContext,List<CircleDetail4Bean> mList) {
        this.mContext = mContext;
        this.mList=mList;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cricle2_detail_list_item, null, false);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_photo= (CircularImage) convertView.findViewById(R.id.iv_photo);
            holder.tv_circledel= (TextView) convertView.findViewById(R.id.tv_circledel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(StringUtils.getImgUrl(mList.get(position).getR_avatars())).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(holder.iv_photo);
        holder.tv_name.setText(mList.get(position).getR_nickname());
        holder.tv_time.setText(mList.get(position).getAddtime());
        byte[] bytes = Base64.decode(mList.get(position).getContent(), Base64.DEFAULT);
        holder.tv_content.setText(new String(bytes));

        SharedPreferences pref = mContext.getSharedPreferences("login", 0);
        String uid = pref.getString("login_uid", "");
        if (uid.equals(mList.get(position).getUid())) {
            holder.tv_circledel.setVisibility(View.VISIBLE);
            holder.tv_circledel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemDeleteListener.onDeleteClick(position);
                }
            });
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tv_time.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            holder.tv_time.setLayoutParams(lp);
        } else {
            holder.tv_circledel.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tv_time.getLayoutParams();
            lp.setMargins(0, 0, 50, 0);
            holder.tv_time.setLayoutParams(lp);
        }

        return convertView;
    }


    //删除接口
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setmOnItemDeleteListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }
    
    private class ViewHolder {
        CircularImage iv_photo;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content,tv_circledel;
        

    }
}
