package com.huacheng.huiservers.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class HouseBillRecordingAdapter extends BaseAdapter {

    private List<HouseBean> mOrders;
    private Context mContext;

    public HouseBillRecordingAdapter(List<HouseBean> orders, Context context) {
        this.mOrders = orders;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mOrders.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.house_billrecording_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_housebill_date.setText(StringUtils.getDateToString(mOrders.get(position).getPay_time(), "7"));
        viewHolder.tv_housebill_price.setText(mOrders.get(position).getCharge_type() + "：" + mOrders.get(position).getSumvalue() + "元");
        viewHolder.tv_houseorder_number.setText("付款编号：" + mOrders.get(position).getOrder_num());

        String payType = mOrders.get(position).getPay_type();
        String payTypeStr = "";
        if (payType.equals("alipay")) {
            payTypeStr = "支付宝";
        } else if (payType.equals("wxpay")) {
            payTypeStr = "微信";
        } else if (payType.equals("bestpay")) {
            payTypeStr = "翼支付";
        } else if (payType.equals("hcpay")) {
            payTypeStr = "华晟一卡通";
        }
        viewHolder.tv_houseorder_paytype.setText("缴费方式：" + payTypeStr);

        return convertView;
    }

    public class ViewHolder {
        TextView tv_housebill_date,
                tv_housebill_price,
                tv_houseorder_number,
                tv_houseorder_paytype;

        public ViewHolder(View v) {
            tv_housebill_date = (TextView) v.findViewById(R.id.tv_housebill_date);
            tv_housebill_price = (TextView) v.findViewById(R.id.tv_housebill_price);
            tv_houseorder_number = (TextView) v.findViewById(R.id.tv_houseorder_number);
            tv_houseorder_paytype = (TextView) v.findViewById(R.id.tv_houseorder_paytype);
        }
    }
}
