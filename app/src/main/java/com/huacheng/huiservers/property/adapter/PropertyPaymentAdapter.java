package com.huacheng.huiservers.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.bean.PropertyInfo1;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：
 * 时间：2018/8/4 10:48
 * created by DFF
 */
public class PropertyPaymentAdapter extends BaseAdapter {

    private Context mContext;
    List<PropertyInfo1> mdatas;

    public PropertyPaymentAdapter(Context mContext, List<PropertyInfo1> mdatas) {
        this.mContext = mContext;
        this.mdatas = mdatas;

    }

    @Override
    public int getCount() {
        return mdatas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_payment_record_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvAllPrice.setText("¥ " + mdatas.get(position).getInfo().getTot_sumvalue());
        holder.mTvName.setText(mdatas.get(position).getInfo().getName());
        holder.mTvAddress.setText(mdatas.get(position).getInfo().getAddress());
        holder.mTvTime.setText(StringUtils.getDateToString(mdatas.get(position).getInfo().getPay_time(), "1"));
        holder.mTvOrderNumber.setText("账单号：" + mdatas.get(position).getInfo().getOrder_num());


        PropertyWYInfo1Adapter wyInfoAdapter = new PropertyWYInfo1Adapter(mContext, mdatas.get(position).getList());
        holder.mList.setAdapter(wyInfoAdapter);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.tv_order_number)
        TextView mTvOrderNumber;
        @BindView(R.id.list)
        ListView mList;
        @BindView(R.id.tv_all_price)
        TextView mTvAllPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
