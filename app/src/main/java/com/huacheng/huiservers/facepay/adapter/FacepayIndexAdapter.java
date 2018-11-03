package com.huacheng.huiservers.facepay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.facepay.FacePayBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class FacepayIndexAdapter extends BaseAdapter {

    private List<FacePayBean> mFacePays;
    private Context mContext;

    public FacepayIndexAdapter(List<FacePayBean> facePays, Context context) {
        this.mFacePays = facePays;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mFacePays.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_item_content.setText(mFacePays.get(position).getC_name());

        if ((mFacePays.size()-1) == position) {
            holder.lin_driver.setVisibility(View.GONE);
        } else {
            holder.lin_driver.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_item_content;
        LinearLayout lin_driver;

        public ViewHolder(View v) {
            tv_item_content = (TextView) v.findViewById(R.id.tv_item_content);
            lin_driver = (LinearLayout) v.findViewById(R.id.lin_driver);
        }
    }


}
