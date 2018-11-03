package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.utils.StringUtils;
import com.lidroid.xutils.BitmapUtils;

public class Coupon40ToShopUseAdapter extends BaseAdapter {
    CouponBean mBean;
    private Context context;

    //    List<CouponBean> beans;
    BitmapUtils bitmapUtils;

    public Coupon40ToShopUseAdapter(CouponBean bean, Context context) {
        this.mBean = bean;
        this.context = context;
//        this.beans = beans;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;//beans.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int p, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.coupon40_toshopuse_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String condition = mBean.getCondition();
        String userule = mBean.getUserule();
        if (!StringUtils.isEmpty(condition)) {
            holder.tv_1.setVisibility(View.VISIBLE);
            holder.tv_1.setText(condition);
        } else {
            holder.tv_1.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(userule)) {
            holder.tv_2.setVisibility(View.VISIBLE);
            holder.tv_2.setText(userule);
        } else {
            holder.tv_2.setVisibility(View.GONE);
        }
        /*if (p == 2) {
            holder.tv_divider_line.setVisibility(View.GONE);
        } else {
            holder.tv_divider_line.setVisibility(View.VISIBLE);
        }*/
        return convertView;
    }

    private class ViewHolder {
        TextView tv_toshopuse_title, tv_1, tv_2, tv_divider_line;

        public ViewHolder(View v) {
            tv_toshopuse_title = (TextView) v.findViewById(R.id.tv_toshopuse_title);
            tv_1 = (TextView) v.findViewById(R.id.tv_1);
            tv_2 = (TextView) v.findViewById(R.id.tv_2);
            tv_divider_line = (TextView) v.findViewById(R.id.tv_divider_line);
        }

    }

}
