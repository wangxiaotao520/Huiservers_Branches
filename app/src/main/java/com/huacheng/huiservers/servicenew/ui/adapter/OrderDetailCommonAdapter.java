package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderDetailCommon;

import java.util.List;

/**
 * Description:投诉，取消订单adapter
 * created by wangxiaotao
 * 2018/9/5 0005 下午 4:42
 */
public class OrderDetailCommonAdapter<T> extends BaseAdapter{
    Context mContext;
    List<T>mDatas ;

    public OrderDetailCommonAdapter(Context mContext, List<T> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.layout_order_detail_common_item,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_check=convertView.findViewById(R.id.iv_check);
            viewHolder.tv_reason=convertView.findViewById(R.id.tv_reason);
            viewHolder.tv_example=convertView.findViewById(R.id.tv_example);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ModelOrderDetailCommon item = (ModelOrderDetailCommon) getItem(position);
        if (item.isSelected()==false){
            viewHolder.iv_check.setBackgroundResource(R.mipmap.ic_order_detail_uncheck);
        }else {
            viewHolder.iv_check.setBackgroundResource(R.mipmap.ic_order_detail_check);
        }
        viewHolder.tv_reason.setText(""+item.getC_name());
        viewHolder.tv_example.setText(""+item.getC_text());
        return convertView;
    }
    static class ViewHolder{
        ImageView iv_check;
        TextView tv_reason;
        TextView tv_example;
    }
}
