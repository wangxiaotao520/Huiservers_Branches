package com.huacheng.huiservers.property.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.bean.ModelPropertyWyInfo;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：
 * 时间：2018/8/11
 * created by DFF
 */
public class PropertyWYInfoAdapter extends BaseAdapter {
    private Context mContext;
    List<ModelPropertyWyInfo.WuyeBean> wyListData;

    public PropertyWYInfoAdapter(Context mContext, List<ModelPropertyWyInfo.WuyeBean> wyListData) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_homelist_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvTagName.setText(wyListData.get(position).getCategory_name());

        if (!TextUtils.isEmpty(wyListData.get(position).getChargeFrom()) &&
                !TextUtils.isEmpty(wyListData.get(position).getChargeEnd())) {
            holder. tvTimeInterval.setText(wyListData.get(position).getChargeFrom() + "/" + wyListData.get(position).getChargeEnd());
        } else {
            String createAt = wyListData.get(position).getCreateAt();

            createAt = StringUtils.getDateToString(createAt, "1");
            holder. tvTimeInterval.setText(createAt);
        }

        holder.tvTimePrice.setText(wyListData.get(position).getRealPrice() + "元");

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_tag_name)
        TextView mTvTagName;
        @BindView(R.id.view_hine)
        View mViewHine;
        @BindView(R.id.tv_timeInterval)
        TextView tvTimeInterval;
        @BindView(R.id.tv_timePrice)
        TextView tvTimePrice;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
