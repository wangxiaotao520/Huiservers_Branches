package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.util.List;

/**
 * Description: 服务列表
 * created by wangxiaotao
 * 2018/9/4 0004 上午 11:40
 */
public class ServiceFragmentAdapter <T>extends BaseAdapter {
    private List<T> mDatas;

    public ServiceFragmentAdapter(List<T> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    private Context mContext;


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
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_service_item, null);
            viewHolder=new ViewHolder();
            viewHolder.sdv_pic=convertView.findViewById(R.id.sdv_pic);
            viewHolder.tv_service_name=convertView.findViewById(R.id.tv_service_name);
            viewHolder.tv_price=convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ModelServiceItem item = (ModelServiceItem) getItem(position);
        FrescoUtils.getInstance().setImageUri(viewHolder.sdv_pic, ApiHttpClient.IMG_SERVICE_URL+item.getTitle_img());
        viewHolder.tv_service_name.setText(item.getTitle()+"");
        viewHolder.tv_price.setText("¥ "+item.getPrice());
        return convertView;
    }
    static class ViewHolder{
        SimpleDraweeView sdv_pic;
        TextView tv_service_name;
        TextView tv_price;
    }

}

