package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceCat;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 服务分类adapter
 * created by badge
 */
public class ServiceCatAdapter<T> extends BaseAdapter {

    private List<T> mDatas;
    private Context mContext;
    private OnServiceCatClickListener listener;

    public ServiceCatAdapter(List<T> mDatas, Context mContext, OnServiceCatClickListener listener) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        if (mDatas.size() > 0) {
            return mDatas.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.service_cat_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_center = convertView.findViewById(R.id.tv_center);
            viewHolder.gridview_tags = convertView.findViewById(R.id.gridview_tags);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int catPositon = position;
        ModelServiceCat bean;
        if (catPositon == 0) {
            bean = new ModelServiceCat();
            bean.setId("0");
            bean.setName("全部");
            bean.setP_id("");

            List<ModelServiceCat.GridBean> gridBeans = new ArrayList<ModelServiceCat.GridBean>();
            ModelServiceCat.GridBean gridBean = new ModelServiceCat.GridBean();
            gridBean.setId("0");
            gridBean.setName("全部");
            gridBean.setP_id("");
            gridBeans.add(gridBean);

            bean.setList(gridBeans);
        } else {

            bean = (ModelServiceCat) mDatas.get(position - 1);
        }

        GridViewTagAdapter<ModelServiceCat.GridBean> gridAdapter = new GridViewTagAdapter<>(bean.getList(), mContext, new GridViewTagAdapter.OnGridItemClickListener() {
            @Override
            public void onGridItemClick(int position) {
                if (listener != null) {
                    listener.onServiceCatClick(catPositon, position);
                   /* if(position==0){

                    }else{
                        listener.onServiceCatClick(catPositon, position+1);

                    }*/
                }
            }
        });

        viewHolder.gridview_tags.setAdapter(gridAdapter);
        viewHolder.tv_center.setText(bean.getName() + "");
        return convertView;
    }

    static class ViewHolder {
        TextView tv_center;
        GridView gridview_tags;
    }

    public interface OnServiceCatClickListener {
        void onServiceCatClick(int catPosition, int gridPositon);
    }
}
