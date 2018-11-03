package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceCat;

import java.util.List;

/**
 * Description:标签
 * created by badge
 */
public class GridViewTagAdapter<T> extends BaseAdapter {
    private List<T> mDatas;
    private Context mContext;
    private OnGridItemClickListener listener;

    public GridViewTagAdapter(List<T> mDatas, Context mContext,OnGridItemClickListener listener) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.listener=listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.service_tag_grid_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_tag_name=convertView.findViewById(R.id.tv_grid_tag_name);
            viewHolder.view_bottom_line=convertView.findViewById(R.id.view_bottom_line);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        ModelServiceCat.GridBean t = (ModelServiceCat.GridBean) mDatas.get(position);
        viewHolder.tv_tag_name.setText(t.getName()+"");
        viewHolder.tv_tag_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onGridItemClick(position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView tv_tag_name;
        View view_bottom_line;
    }

    public interface OnGridItemClickListener{
        void onGridItemClick(int position);
    }
}
