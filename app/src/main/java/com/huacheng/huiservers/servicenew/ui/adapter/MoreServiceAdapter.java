package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelInfoCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelItemBean;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/6 11:16
 */
public class MoreServiceAdapter extends BaseAdapter {

    List<ModelItemBean> datas;
    Context context;
    TagFlowLayout mFlowlayoutService;

    public MoreServiceAdapter(List<ModelItemBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.service_item_featuredservice_item1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ModelItemBean data = datas.get(position);

        holder.tv_serviceName.setText(data.getTitle());

        String imgSize =data.getTitle_img_size();
        int pxdip = StringUtils.dip2px(context, 30);//获取屏幕
        if (!NullUtil.isStringEmpty(imgSize)) {
            int screenWidth = ToolUtils.getScreenWidth(context);
//                            LogUtils.e("screenWidth=" + screenWidth);
            holder.sdvService_bg.getLayoutParams().width = screenWidth - pxdip;//屏幕边距减去30dp像素
            Double d = Double.valueOf(ToolUtils.getScreenWidth(context) - pxdip) / (Double.valueOf(imgSize));
            holder.sdvService_bg.getLayoutParams().height = (new Double(d)).intValue();

        } else {
            holder.sdvService_bg.getLayoutParams().width = (ToolUtils.getScreenWidth(context) - pxdip);
            Double d = Double.valueOf(ToolUtils.getScreenWidth(context) - pxdip) / 2.5;
            holder.sdvService_bg.getLayoutParams().height = (new Double(d)).intValue();
        }


        if(!NullUtil.isStringEmpty(data.getTitle_img())){
            FrescoUtils.getInstance().setImageUri(holder.sdvService_bg, ApiHttpClient.IMG_SERVICE_URL + data.getTitle_img());
        }


        holder.tv_organizationName.setText(data.getI_name());
        String price = data.getPrice();
        if (!NullUtil.isStringEmpty(price)) {
            holder.tv_servicePrice.setText("¥" + price);
        } else {
            holder.tv_servicePrice.setText("");
        }
        if (!NullUtil.isStringEmpty(data.getLogo())) {
            FrescoUtils.getInstance().setImageUri(holder.sdv_servicelogo, ApiHttpClient.IMG_SERVICE_URL + data.getLogo());
        }

        List<ModelInfoCategoryBean> cats = data.getCategory();

        getMerchatServiceTag(cats);

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView sdvService_bg, sdv_servicelogo;
        TextView tv_serviceName, tv_servicePrice, tv_organizationName;

        public ViewHolder(View v) {
            sdvService_bg = v.findViewById(R.id.sdv_service_bg);
            tv_serviceName = v.findViewById(R.id.tv_serviceName);
            tv_servicePrice = v.findViewById(R.id.tv_servicePrice);
            sdv_servicelogo = v.findViewById(R.id.sdv_servicelogo);
            tv_organizationName = v.findViewById(R.id.tv_organizationName);
            mFlowlayoutService = v.findViewById(R.id.flowlayout_service);

        }
    }

    private void getMerchatServiceTag(final List<ModelInfoCategoryBean> cats) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        TagAdapter<ModelInfoCategoryBean> adapter = new TagAdapter<ModelInfoCategoryBean>(cats) {

            @Override
            public View getView(FlowLayout parent, int position, ModelInfoCategoryBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.service_merchat_tag_item,
                        mFlowlayoutService, false);
                tv.setText(cats.get(position).getCategory_cn());
                return tv;
            }
        };
        mFlowlayoutService.setAdapter(adapter);
    }
}
