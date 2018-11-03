package com.huacheng.huiservers.property.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.bean.WuyeListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：
 * 时间：2018/8/11
 * created by DFF
 */
public class PropertyWYInfo1Adapter extends BaseAdapter {
    private Context mContext;
    List<List<WuyeListBean>> wyListData;

    public PropertyWYInfo1Adapter(Context mContext, List<List<WuyeListBean>> wyListData) {
        this.mContext = mContext;
        this.wyListData = wyListData;

    }

    @Override
    public int getCount() {
        return wyListData.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_homelist_item1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mLinView.removeAllViews();
        for (int i = 0; i < wyListData.get(position).size(); i++) {
            holder.mTvTagName.setText(wyListData.get(position).get(i).getCharge_type());
            View v = LayoutInflater.from(mContext).inflate(R.layout.include_property_linear, null);
            TextView tv_timeInterval = v.findViewById(R.id.tv_timeInterval);
            TextView tv_timePrice = v.findViewById(R.id.tv_timePrice);
            if (!TextUtils.isEmpty(wyListData.get(position).get(i).getStartdate()) &&
                    !TextUtils.isEmpty(wyListData.get(position).get(i).getEnddate())) {
                tv_timeInterval.setText(wyListData.get(position).get(i).getStartdate() + "/" + wyListData.get(position).get(i).getEnddate());
            } else {
                tv_timeInterval.setText(wyListData.get(position).get(i).getBill_time());
            }

            tv_timePrice.setText(wyListData.get(position).get(i).getSumvalue() + "元");
            holder.mLinView.addView(v);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_tag_name)
        TextView mTvTagName;
        @BindView(R.id.lin_view)
        LinearLayout mLinView;
        @BindView(R.id.view_hine)
        View mViewHine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
