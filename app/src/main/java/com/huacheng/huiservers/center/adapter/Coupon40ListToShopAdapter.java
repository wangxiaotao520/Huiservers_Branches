package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

public class Coupon40ListToShopAdapter extends BaseAdapter {

    private Context mContext;
    List<CouponBean> mBeans;
    int mStatus;

    public Coupon40ListToShopAdapter(List<CouponBean> beans, int status, Context context) {
        this.mBeans = beans;
        this.mStatus = status;
        this.mContext = context;
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
    public View getView(int p, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coupon40list_toshop_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext)
                .load(MyCookieStore.URL + mBeans.get(p).getPhoto())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_toshop_logo);

        holder.tv_toshop_name.setText(mBeans.get(p).getName());
        holder.tv_toshop_condition.setText(mBeans.get(p).getCondition());

        String amount = mBeans.get(p).getAmount();
        if (!StringUtils.isEmpty(amount)) {
            holder.tv_toshop_price.setVisibility(View.VISIBLE);
            holder.tv_toshop_price_unit.setVisibility(View.VISIBLE);
            double doubleAmount = Double.valueOf(amount);
            if (doubleAmount > 1) {
                holder.tv_toshop_price.setText((int) doubleAmount + "");
                holder.tv_toshop_price_unit.setText("元");
            } else {
                if (doubleAmount > 0 && doubleAmount < 1) {
                    String decimal = (doubleAmount + "").substring(2);
                    holder.tv_toshop_price.setText(decimal);
                    holder.tv_toshop_price_unit.setText("折");
                }

            }

        } else {
            holder.tv_toshop_price.setVisibility(View.GONE);
            holder.tv_toshop_price_unit.setVisibility(View.GONE);
        }

        String endTime = mBeans.get(p).getEndtime();
        if (!StringUtils.isEmpty(endTime)) {
            holder.tv_toshop_effectivedate.setText("有效期至 " + StringUtils.getDateToString(endTime, "2"));
        }
        if (mStatus == 0) {
            holder.tv_toshop_status.setText("立即领取");
        } else if (mStatus == 1) {
            holder.tv_toshop_status.setText("立即使用");
        }

        return convertView;
    }


    private class ViewHolder {
        ImageView iv_toshop_logo;

        TextView tv_toshop_name,
                tv_toshop_condition,
                tv_toshop_price,
                tv_toshop_price_unit,
                tv_toshop_effectivedate,
                tv_toshop_status;


        public ViewHolder(View v) {
            iv_toshop_logo = (ImageView) v.findViewById(R.id.iv_toshop_logo);

            tv_toshop_name = (TextView) v.findViewById(R.id.tv_toshop_name);
            tv_toshop_condition = (TextView) v.findViewById(R.id.tv_toshop_condition);
            tv_toshop_price = (TextView) v.findViewById(R.id.tv_toshop_price);
            tv_toshop_price_unit = (TextView) v.findViewById(R.id.tv_toshop_price_unit);


            tv_toshop_effectivedate = (TextView) v.findViewById(R.id.tv_toshop_effectivedate);
            tv_toshop_status = (TextView) v.findViewById(R.id.tv_toshop_status);
        }

    }

}
