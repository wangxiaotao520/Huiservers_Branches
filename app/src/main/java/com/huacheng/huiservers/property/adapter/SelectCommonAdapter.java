package com.huacheng.huiservers.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.bean.ModelSelectCommon;

import java.util.List;

/**
 * Description: 选择通用adapter
 * created by wangxiaotao
 * 2018/8/27 0027 下午 7:07
 */
public class SelectCommonAdapter <T>extends BaseAdapter {
    Context mContext;
    List<T> mDatas;
    int type;
    public SelectCommonAdapter(Context mContext, List<T> mDatas, int type) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.type=type;
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.select_common_item,null);
            viewHolder=new ViewHolder();
            viewHolder.name=convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ModelSelectCommon bean = (ModelSelectCommon) mDatas.get(position);
        if (type==0){
            //住宅类型
            viewHolder.name.setText(bean.getType_name());
        }else if (type==1){
            //抄表类型
            viewHolder.name.setText(bean.getName());
        }else if (type==2){
            //楼号
            if ("-1".equals(bean.getBuildsing_id())) {
                viewHolder.name.setText("全部");
            }else {
                viewHolder.name.setText(bean.getBuildsing_id()+"号楼");
            }
        }else if (type==3){
            //单元号
            if ("-1".equals(bean.getUnit())) {
                viewHolder.name.setText("全部");
            }else {
                viewHolder.name.setText(bean.getUnit()+"单元");
            }
        }else if (type==4){
            //房间号
            if ("-1".equals(bean.getCode())) {
                viewHolder.name.setText("全部");
            }else {
                viewHolder.name.setText(bean.getCode()+"");
            }
        }else if (type==5){
            //商铺号
            if ("-1".equals(bean.getCode())) {
                viewHolder.name.setText("全部");
            }else {
                viewHolder.name.setText(bean.getCode()+"");
            }
        }else if (type==6){
            //抄表费项
                viewHolder.name.setText(bean.getType_name()+" 单价"+bean.getPrice()+"元");
        }
        return convertView;
    }

    static class ViewHolder{
        TextView name;
    }
}