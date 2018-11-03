package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class Coupon40RecordingAdapter extends BaseAdapter {
    private Context context;
    List<CouponBean> mBeans;
    BitmapUtils bitmapUtils;

    public Coupon40RecordingAdapter(List<CouponBean> beans, Context context) {
        this.context = context;
        this.mBeans = beans;
        bitmapUtils = new BitmapUtils(context);
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
    public View getView(int i, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.coupon40_list_all_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mBeans.get(i).getCategory_id().equals("54")) {
            holder.lin_coupon40.setVisibility(View.VISIBLE);
            holder.rel_coupon40_toshop.setVisibility(View.GONE);
            String amount = mBeans.get(i).getAmount();
            if (!StringUtils.isEmpty(amount)) {
                holder.tv_coupon40_price_expired.setVisibility(View.VISIBLE);
                holder.tv_coupon40_price_unit_expired.setVisibility(View.VISIBLE);
                double doubleAmount = Double.valueOf(amount);
                if (doubleAmount > 1) {
                    holder.tv_coupon40_price_expired.setText((int) doubleAmount + "");
                    holder.tv_coupon40_price_unit_expired.setText("元");
                } else {
                    if (doubleAmount > 0 && doubleAmount < 1) {
                        String decimal = (doubleAmount + "").substring(2);
                        holder.tv_coupon40_price_expired.setText(decimal);
                        holder.tv_coupon40_price_unit_expired.setText("折");
                    }
                }
            } else {
                holder.tv_coupon40_price_expired.setVisibility(View.GONE);
                holder.tv_coupon40_price_unit_expired.setVisibility(View.GONE);
            }
            holder.tv_coupon40_name_expired.setText(mBeans.get(i).getName());
            holder.tv_coupon40_condition_expired.setText(mBeans.get(i).getCondition());
            holder.tv_coupon40_status_expired.setText(mBeans.get(i).getStatus_cn());

        } else {
            holder.lin_coupon40.setVisibility(View.GONE);
            holder.rel_coupon40_toshop.setVisibility(View.VISIBLE);

            if (!mBeans.get(i).getPhoto().equals("")) {
                bitmapUtils.display(holder.iv_toshop_logo_expired, MyCookieStore.URL + mBeans.get(i).getPhoto());
            }
            holder.tv_toshop_name_expired.setText(mBeans.get(i).getName());

            String amount = mBeans.get(i).getAmount();
            if (!StringUtils.isEmpty(amount)) {
                holder.tv_toshop_price_expired.setVisibility(View.VISIBLE);
                holder.tv_toshop_price_unit_expired.setVisibility(View.VISIBLE);
                double doubleAmount = Double.valueOf(amount);
                if (doubleAmount > 1) {
                    holder.tv_toshop_price_expired.setText((int) doubleAmount + "");
                    holder.tv_toshop_price_unit_expired.setText("元");
                } else {
                    if (doubleAmount > 0 && doubleAmount < 1) {
                        String decimal = (doubleAmount + "").substring(2);
                        holder.tv_toshop_price_expired.setText(decimal);
                        holder.tv_toshop_price_unit_expired.setText("折");
                    }
                }
            } else {
                holder.tv_toshop_price_expired.setVisibility(View.GONE);
                holder.tv_toshop_price_unit_expired.setVisibility(View.GONE);
            }
            holder.tv_toshop_condition_expired.setText(mBeans.get(i).getCondition());
            holder.tv_toshop_status_expired.setText(mBeans.get(i).getStatus_cn());
        }

        return convertView;
    }

    private class ViewHolder {
        LinearLayout lin_coupon40;
        RelativeLayout rel_coupon40_toshop;

        TextView tv_coupon40_price_expired,
                tv_coupon40_price_unit_expired,
                tv_coupon40_name_expired,
                tv_coupon40_condition_expired,
                tv_coupon40_status_expired,

        tv_toshop_name_expired,
                tv_toshop_price_expired,
                tv_toshop_price_unit_expired,
                tv_toshop_condition_expired,
                tv_toshop_status_expired;
        ImageView iv_toshop_logo_expired;

        public ViewHolder(View v) {
            lin_coupon40 = (LinearLayout) v.findViewById(R.id.lin_coupon40);

            tv_coupon40_price_expired = (TextView) v.findViewById(R.id.tv_coupon40_price_expired);
            tv_coupon40_price_unit_expired = (TextView) v.findViewById(R.id.tv_coupon40_price_unit_expired);
            tv_coupon40_name_expired = (TextView) v.findViewById(R.id.tv_coupon40_name_expired);
            tv_coupon40_condition_expired = (TextView) v.findViewById(R.id.tv_coupon40_condition_expired);
            tv_coupon40_status_expired = (TextView) v.findViewById(R.id.tv_coupon40_status_expired);

            rel_coupon40_toshop = (RelativeLayout) v.findViewById(R.id.rel_coupon40_toshop);

            iv_toshop_logo_expired = (ImageView) v.findViewById(R.id.iv_toshop_logo_expired);
            tv_toshop_name_expired = (TextView) v.findViewById(R.id.tv_toshop_name_expired);
            tv_toshop_price_expired = (TextView) v.findViewById(R.id.tv_toshop_price_expired);
            tv_toshop_condition_expired = (TextView) v.findViewById(R.id.tv_toshop_condition_expired);
            tv_toshop_price_unit_expired = (TextView) v.findViewById(R.id.tv_toshop_price_unit_expired);
            tv_toshop_status_expired = (TextView) v.findViewById(R.id.tv_toshop_status_expired);
        }

    }

}
