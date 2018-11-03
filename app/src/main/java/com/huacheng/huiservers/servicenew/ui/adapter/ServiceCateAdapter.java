package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceDetail;

import java.util.List;

/**
 * 类描述：
 * 时间：2018/9/4 16:28
 * created by DFF
 */
public class ServiceCateAdapter extends BaseAdapter {
    private Context mContext;
    List<ModelServiceDetail.TagListBean> mdatas;
    int selectItem = -1;

    public ServiceCateAdapter(Context mContext, List<ModelServiceDetail.TagListBean> mdatas) {

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

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_cate, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mdatas.get(position).getTagname());
        if (selectItem == position) {
            holder.tv_name.setEnabled(true);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.tv_name.setBackground(mContext.getResources().getDrawable(R.drawable.bg_orange));
        } else {
            holder.tv_name.setEnabled(false);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.grey96));
            holder.tv_name.setBackground(null);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_name;

    }
}
