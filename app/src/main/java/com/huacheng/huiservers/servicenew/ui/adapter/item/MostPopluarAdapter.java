package com.huacheng.huiservers.servicenew.ui.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelCategoryServiceBean;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.util.List;


/**
 * Description:
 * Author: badge
 * Create: 2018/9/4 8:36
 */
public class MostPopluarAdapter extends BaseAdapter {

    List<ModelCategoryServiceBean> mCateService;
    Context mContext;
    ViewHolder holder = null;

    public MostPopluarAdapter(List<ModelCategoryServiceBean> cateService, Context context) {
        this.mCateService = cateService;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCateService.size();
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

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_mostpopluar_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ModelCategoryServiceBean cateService = mCateService.get(position);
        holder.tv_popular.setText(cateService.getName() + " >");
        holder.lin_most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MerchantServiceListActivity.class);
                intent.putExtra("tabType", "servicePop");
                intent.putExtra("sub_id", cateService.getId());
                intent.putExtra("typeName", cateService.getName());
                mContext.startActivity(intent);
            }
        });


        final List<ModelServiceItem> services = cateService.getService();
        holder.lin_mostpopluar.removeAllViews();
        for (int i = 0; i < services.size(); i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.service_item_mostpopluar_item_card, null);
            TextView tv_titleName = v.findViewById(R.id.tv_titleName);
            SimpleDraweeView sdv_premium_merchat_bg = v.findViewById(R.id.sdv_premium_merchat_bg);
            LinearLayout lin_most = v.findViewById(R.id.lin_most);

            final ModelServiceItem item = services.get(i);

            FrescoUtils.getInstance().setImageUri(sdv_premium_merchat_bg, ApiHttpClient.IMG_SERVICE_URL + item.getTitle_img());
            tv_titleName.setText(item.getTitle());
            holder.lin_mostpopluar.addView(v);
            lin_most.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ServiceDetailActivity.class);
                    intent.putExtra("service_id", item.getId());
                    mContext.startActivity(intent);

                }
            });
        }

        return convertView;
    }

    private HorizontalPopluarAdapter mPopAapter;

    class ViewHolder {
        TextView tv_popular;
        LinearLayout lin_mostpopluar;
        //        GridView gv_mostpopluar;
        LinearLayout lin_most;

        public ViewHolder(View v) {
            tv_popular = v.findViewById(R.id.tv_popular);
            lin_most = v.findViewById(R.id.lin_most);
            lin_mostpopluar = v.findViewById(R.id.lin_mostpopluar);
//            gv_mostpopluar = v.findViewById(R.id.gv_mostpopluar);

        }
    }

}
