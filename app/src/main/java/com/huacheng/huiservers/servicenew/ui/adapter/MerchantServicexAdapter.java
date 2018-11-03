package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelInfoCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelMerchantList;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.servicenew.ui.shop.ServiceStoreActivity;
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
 * Create: 2018/9/6 18:32
 */
public class MerchantServicexAdapter extends BaseAdapter {

    List<ModelMerchantList> mDatas;
    List<ModelServiceItem> mDatas2;
    public Context context;
    public int type;
    /*private static final int TYPE_MERCHANT = 0;
    private static final int TYPE_SERVICE = 1;*/

    public MerchantServicexAdapter(List<ModelMerchantList> mDatas, List<ModelServiceItem> mDatas2, Context context, int type) {
        this.mDatas = mDatas;
        this.mDatas2 = mDatas2;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        if (type == 0) {
            return mDatas.size();
        } else {
            return mDatas2.size();
        }

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    Intent intent = new Intent();

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
//        int type = getItemViewType(position);
        switch (type) {
            case 0:
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_merchantx_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final ModelMerchantList merchant = mDatas.get(position);
                FrescoUtils.getInstance().setImageUri(holder.sdv_merchantlogo, ApiHttpClient.IMG_SERVICE_URL + merchant.getLogo());

                holder.tv_organizationName.setText(merchant.getName());
                getMerchatServiceTag(holder, merchant.getCategory());
                holder.lin_merchant_card.removeAllViews();

                List<ModelServiceItem> services = merchant.getService();
                for (int i = 0; i < services.size(); i++) {
                    View v = LayoutInflater.from(context).inflate(R.layout.service_item_mostpopluar_item_card, null);
                    TextView tv_titleName = v.findViewById(R.id.tv_titleName);
                    SimpleDraweeView sdv_premium_merchat_bg = v.findViewById(R.id.sdv_premium_merchat_bg);
                    LinearLayout lin_most = v.findViewById(R.id.lin_most);

                    final ModelServiceItem item = services.get(i);
                    FrescoUtils.getInstance().setImageUri(sdv_premium_merchat_bg, ApiHttpClient.IMG_SERVICE_URL + item.getTitle_img());
                    tv_titleName.setText(item.getTitle());
                    holder.lin_merchant_card.addView(v);

                    lin_most.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent.setClass(context, ServiceDetailActivity.class);
                            intent.putExtra("service_id", item.getId());
                            context.startActivity(intent);
                        }
                    });

                }
                holder.lin_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String store_id = merchant.getId();
                        intent.setClass(context, ServiceStoreActivity.class);
                        intent.putExtra("store_id", store_id + "");
                        context.startActivity(intent);
                    }
                });
                break;
            case 1:
                ViewHolder holderService = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_featuredservice_item2, null);
                    holderService = new ViewHolder(convertView);
                    convertView.setTag(holderService);
                } else {
                    holderService = (ViewHolder) convertView.getTag();
                }

                final ModelServiceItem service = mDatas2.get(position);

                String imgSize =service.getTitle_img_size();
                int pxdip = StringUtils.dip2px(context, 30);//获取屏幕
                if (!NullUtil.isStringEmpty(imgSize)) {
                    holderService.sdv_service_bg.getLayoutParams().width = (ToolUtils.getScreenWidth(context)) - pxdip;//减去30dp像素
                    Double d = Double.valueOf(ToolUtils.getScreenWidth(context) - pxdip) / (Double.valueOf(imgSize));
                    holderService.sdv_service_bg.getLayoutParams().height = (new Double(d)).intValue();

                    holderService.rl_servicex_item.getLayoutParams().width=(ToolUtils.getScreenWidth(context)) - pxdip;//减去30dp像素
                } else {
                    holderService.sdv_service_bg.getLayoutParams().width = (ToolUtils.getScreenWidth(context) - pxdip);
                    Double d = Double.valueOf(ToolUtils.getScreenWidth(context) - pxdip) / 2.5;
                    holderService.sdv_service_bg.getLayoutParams().height = (new Double(d)).intValue();

                    holderService.rl_servicex_item.getLayoutParams().width=(ToolUtils.getScreenWidth(context)) - pxdip;//减去30dp像素
                }

                FrescoUtils.getInstance().setImageUri(holderService.sdv_service_bg, ApiHttpClient.IMG_SERVICE_URL + service.getTitle_img());

                holderService.tv_serviceName.setText(service.getTitle());
                String price = service.getPrice();
                if (!NullUtil.isStringEmpty(price)) {
                    holderService.tv_servicePrice.setText("¥" + price);
                } else {
                    holderService.tv_servicePrice.setText("");
                }

                holderService.lin_bottom_tags.setVisibility(View.GONE);
                holderService.ll_service_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.setClass(context, ServiceDetailActivity.class);
                        intent.putExtra("service_id", service.getId());
                        context.startActivity(intent);
                    }
                });

                break;
        }
        return convertView;
    }

    private void getMerchatServiceTag(final ViewHolder holder, final List<ModelInfoCategoryBean> cats) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        TagAdapter<ModelInfoCategoryBean> adapter = new TagAdapter<ModelInfoCategoryBean>(cats) {

            @Override
            public View getView(FlowLayout parent, int position, ModelInfoCategoryBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.service_merchat_tag_item,
                        holder.tfl_merchantTag, false);
                tv.setText(cats.get(position).getCategory_cn());
                return tv;
            }
        };
        holder.tfl_merchantTag.setAdapter(adapter);
    }

    class ViewHolder {
        LinearLayout lin_merchant_card, lin_bottom_tags, lin_container, ll_service_container;
        SimpleDraweeView sdv_merchantlogo;
        TextView tv_organizationName;
        TagFlowLayout tfl_merchantTag;
        RelativeLayout rl_servicex_item;

        SimpleDraweeView sdv_service_bg;
        TextView tv_serviceName,
                tv_servicePrice;

        public ViewHolder(View v) {
            //商家
            sdv_merchantlogo = v.findViewById(R.id.sdv_merchantlogo);
            tv_organizationName = v.findViewById(R.id.tv_organizationName);
            tfl_merchantTag = v.findViewById(R.id.tfl_merchantTag);
            lin_container = v.findViewById(R.id.lin_container);

            ll_service_container = v.findViewById(R.id.ll_service_container);

            //服务
            sdv_service_bg = v.findViewById(R.id.sdv_service_bg);
            tv_serviceName = v.findViewById(R.id.tv_serviceName);
            tv_servicePrice = v.findViewById(R.id.tv_servicePrice);
            lin_merchant_card = v.findViewById(R.id.lin_merchant_card);

            lin_bottom_tags = v.findViewById(R.id.lin_bottom_tags);
            rl_servicex_item= v.findViewById(R.id.rl_servicex_item);
        }
    }
}
