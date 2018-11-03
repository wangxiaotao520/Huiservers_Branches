package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.CateBean;

import java.util.List;

public class ShopCateOneAdapter extends BaseAdapter {
    List<CateBean> mList;
    private Context mContext;
    String str_img;

    public ShopCateOneAdapter(Context mContext, List<CateBean> mList) {
        super();
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
        //return 3;
    }

    @Override
    public Object getItem(int position) {
        //return 0;
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_cate_one_item, null, false);
            holder.txt_one = (TextView) convertView.findViewById(R.id.txt_one);
            holder.tv_view = (TextView) convertView.findViewById(R.id.tv_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList.get(position).isSelect() == true) {
            holder.txt_one.setBackgroundResource(R.color.white);
            holder.txt_one.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.txt_one.setTextSize(14);
            holder.txt_one.getPaint().setFakeBoldText(true);
            holder.tv_view.setVisibility(View.VISIBLE);


        } else {
            holder.txt_one.getPaint().setFakeBoldText(false);
            holder.txt_one.setBackgroundResource(R.color.all_gray);
            holder.txt_one.setTextColor(mContext.getResources().getColor(R.color.black_jain_54));
            holder.txt_one.setTextSize(12);
            holder.tv_view.setVisibility(View.GONE);
        }
        holder.txt_one.setText(mList.get(position).getCate_name());
        return convertView;
    }

    private class ViewHolder {
        TextView txt_one, tv_view;

    }

}
