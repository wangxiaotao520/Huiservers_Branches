package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.AddressBean;

import java.util.List;

/**
 * 类：
 * 时间：2017/10/14 15:48
 * 功能描述:Huiservers
 */
public class New_AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressBean> beans;
    private String address;

    public New_AddressAdapter(Context mContext, List<AddressBean> beans, String address) {
        this.mContext = mContext;
        this.beans = beans;
        this.address = address;
    }

    @Override
    public int getCount() {
        if (beans.size() > 0) {

            return beans.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.new_address_item, null, false);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_num = (TextView) convertView.findViewById(R.id.txt_num);
            holder.txt_address = (TextView) convertView.findViewById(R.id.txt_address);
            holder.txt_yezhu = (TextView) convertView.findViewById(R.id.txt_yezhu);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_name.setText(beans.get(arg0).getConsignee_name());
        holder.txt_num.setText(beans.get(arg0).getConsignee_mobile());
        String str = beans.get(arg0).getRegion_cn().replaceAll(",", "");
        holder.txt_address.setText(str +" "+ beans.get(arg0).getCommunity_cn() + beans.get(arg0).getDoorplate());


        return convertView;
    }

    public class ViewHolder {
        private TextView txt_name, txt_num, txt_address, txt_yezhu;
    }
}
