package com.huacheng.huiservers.servicenew.ui.adapter.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.view.MyGridview1;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/4 10:56
 */
public class HorizontalPopluarAdapter extends BaseAdapter {

    List<ModelServiceItem> mService;
    Context mContext;

    public HorizontalPopluarAdapter(List<ModelServiceItem> service, Context context) {
        this.mService = service;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mService.size();
    }

    @Override
    public Object getItem(int position) {
        return mService.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {//322dp 152dp
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_mostpopluar_item_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (((MyGridview1)parent).isOnMeasure){
            return convertView;
        }
        String title = mService.get(position).getTitle();
        String titleImg = mService.get(position).getTitle_img();
        FrescoUtils.getInstance().setImageUri(holder.sdv_premium_merchat_bg, ApiHttpClient.IMG_SERVICE_URL + titleImg);
        holder.tv_titleName.setText(title);

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView sdv_premium_merchat_bg;
        TextView tv_titleName;

        public ViewHolder(View v) {
            sdv_premium_merchat_bg = v.findViewById(R.id.sdv_premium_merchat_bg);
            tv_titleName = v.findViewById(R.id.tv_titleName);

        }
    }
}
