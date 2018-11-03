package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

public class Coupon40ListMyAdapter extends BaseAdapter {

    private Context mContext;
    List<CouponBean> mBeans;
    int mStatus;

    public Coupon40ListMyAdapter(List<CouponBean> beans, int i, Context context) {
        this.mContext = context;
        this.mStatus = i;
        this.mBeans = beans;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mBeans.size();
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
    public View getView(final int p, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coupon40list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String amount = mBeans.get(p).getAmount();
        if (!StringUtils.isEmpty(amount)) {
            double doubleAmount = Double.valueOf(amount);

            holder.tv_coupon40_price.setText((int) doubleAmount + "");
            holder.tv_coupon40_price_unit.setVisibility(View.VISIBLE);
        } else {
            holder.tv_coupon40_price_unit.setVisibility(View.GONE);
        }


        holder.tv_coupon40_name.setText(mBeans.get(p).getName());
        holder.tv_coupon40_condition.setText(mBeans.get(p).getCondition());
        String endTime = mBeans.get(p).getEndtime();
        if (!StringUtils.isEmpty(endTime)) {
            holder.tv_coupon40_effectivedate.setText("有效期至 " + StringUtils.getDateToString(endTime, "2"));
        }

        if (mStatus==0) {
            holder.tv_coupon40_status.setText("立即领取");
        }else if (mStatus==1){
            holder.tv_coupon40_status.setText("立即使用");
        }

       /* holder.lin_coupon40_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onDeleteClick(p);
            }
        });*/

        return convertView;
    }

   /* */

    /**
     * 领取或使用的监听接口
     *//*
    public interface IOnItemDeleteListener {
        void onDeleteClick(int i);
    }

    private IOnItemDeleteListener OnItemDeleteListener;

    public void setOnItemDeleteListener(IOnItemDeleteListener mOnItemDeleteListener) {
        this.OnItemDeleteListener = mOnItemDeleteListener;
    }*/


    private class ViewHolder {
        TextView tv_coupon40_price, tv_coupon40_price_unit,
                tv_coupon40_name,
                tv_coupon40_condition,
                tv_coupon40_effectivedate,
                tv_coupon40_status;

        LinearLayout lin_coupon40_status;


        public ViewHolder(View v) {
            tv_coupon40_price = (TextView) v.findViewById(R.id.tv_coupon40_price);
            tv_coupon40_price_unit = (TextView) v.findViewById(R.id.tv_coupon40_price_unit);
            tv_coupon40_name = (TextView) v.findViewById(R.id.tv_coupon40_name);
            tv_coupon40_condition = (TextView) v.findViewById(R.id.tv_coupon40_condition);
            tv_coupon40_effectivedate = (TextView) v.findViewById(R.id.tv_coupon40_effectivedate);
            tv_coupon40_status = (TextView) v.findViewById(R.id.tv_coupon40_status);

            lin_coupon40_status = (LinearLayout) v.findViewById(R.id.lin_coupon40_status);
        }

    }

}
