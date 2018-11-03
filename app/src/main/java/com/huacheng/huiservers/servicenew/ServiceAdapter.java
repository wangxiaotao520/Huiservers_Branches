package com.huacheng.huiservers.servicenew;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.adapter.ServiceGirdCateAdapter;
import com.huacheng.huiservers.fragment.bean.ModelServiceIndex;
import com.huacheng.huiservers.servicenew.model.ModelCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelCategoryServiceBean;
import com.huacheng.huiservers.servicenew.model.ModelInfoBean;
import com.huacheng.huiservers.servicenew.model.ModelInfoCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelItemBean;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.servicenew.ui.adapter.item.MostPopluarAdapter;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.view.MyGridview;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.huacheng.libraryservice.widget.FunctionAdvertise;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/3 8:52
 */
public class ServiceAdapter extends BaseAdapter {

    ModelServiceIndex mData;
    List<ModelAds> mAds;
    Context mContext;


    public ServiceAdapter(ModelServiceIndex service, List<ModelAds> modelAds_list, Context context) {
        this.mData = service;
        this.mAds = modelAds_list;
        this.mContext = context;

    }

    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static final int TYPE_BANNERTOP = 0;//顶部广告
    public static final int TYPE_ALL_CATEGORY = 1;//全部分类
    public static final int TYPE_HOT_ACTIVITY = 2;//热门活动
    public static final int TYPE_MOST_POPULAR = 3;//最受欢迎
    public static final int TYPE_PREMIUM_MERCHANT = 4;//优质商家
    public static final int TYPE_FEATUREED_SERVICE = 5;//精选服务
    public static final int TYPE_MORE_SERVICE = 6;//更多服务
    public static final int TYPE_HOT_TELL = 7;//底部热线广告图

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNERTOP;
        } else if (position == 1) {
            return TYPE_ALL_CATEGORY;
        } else if (position == 2) {
            return TYPE_HOT_ACTIVITY;
        } else if (position == 3) {
            return TYPE_MOST_POPULAR;
        } else if (position == 4) {
            return TYPE_PREMIUM_MERCHANT;
        } else if (position == 5) {
            return TYPE_FEATUREED_SERVICE;
        } else if (position == 6) {
            return TYPE_MORE_SERVICE;
        } else {
            return TYPE_HOT_TELL;
        }


    }

    @Override
    public int getViewTypeCount() {
        return 8;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = new ViewHolder();

        switch (type) {
            case TYPE_BANNERTOP:
                //   ViewHolder banTopHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_bannertop, null);
                    //    banTopHolder = new ViewHolder(convertView);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.fc_ads.initAds(mAds);
                //广告点击跳转
                holder.fc_ads.setListener(new FunctionAdvertise.OnClickAdsListener() {
                    @Override
                    public void OnClickAds(ModelAds ads) {
                        if (TextUtils.isEmpty(ads.getUrl())) {
                            if (ads.getUrl_type().equals("0") || TextUtils.isEmpty(ads.getUrl_type())) {
                                new Jump(mContext, ads.getType_name(), ads.getAdv_inside_url());
                            } else {
                                new Jump(mContext, ads.getUrl_type(), ads.getType_name(), "", "");
                            }
                        } else {//URL不为空时外链
                            new Jump(mContext, ads.getUrl());
                        }
                    }
                });
                return convertView;

            case TYPE_ALL_CATEGORY:

                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_allcategory, null);
                    //  allCatHolder = new ViewHolder(convertView);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                List<ModelCategoryBean> mCate = mData.getCategory();
                if (mCate != null && mCate.size() > 0) {
                    ServiceGirdCateAdapter mCateAdapter = new ServiceGirdCateAdapter(mCate, mContext);
                    holder.my_grid_cate.setAdapter(mCateAdapter);
                }
                return convertView;

            case TYPE_HOT_ACTIVITY:
                // ViewHolder hotActHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_hotactivity, null);
                    //   holder = new ViewHolder(convertView);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.lin_hotactivity.setVisibility(View.GONE);
                return convertView;

            case TYPE_MOST_POPULAR:
                //   ViewHolder mostPopHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_mostpopluar, null);
                    //   mostPopHolder = new ViewHolder(convertView);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                //
                List<ModelCategoryServiceBean> mCateService = mData.getCategory_service();
                if (mCateService != null && mCateService.size() > 0) {
                    MostPopluarAdapter mostPopAdapter = new MostPopluarAdapter(mCateService, mContext);
                    holder.lv_mostpopluar.setAdapter(mostPopAdapter);
                }
                return convertView;

            case TYPE_PREMIUM_MERCHANT:
                //  ViewHolder premiumMerchatHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_premium_merchant, null);
                    //    premiumMerchatHolder = new ViewHolder(convertView);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.tv_merchant_more1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.setClass(mContext, MerchantServiceListActivity.class);
                        mContext.startActivity(intent);
                    }
                });

                List<ModelInfoBean> infos = mData.getInfo();
                holder.lin_premium.removeAllViews();
                if (infos != null && infos.size() > 0) {
                    for (int i = 0; i < infos.size(); i++) {

                        ModelInfoBean info = infos.get(i);
                        View v = LayoutInflater.from(mContext).inflate(R.layout.service_item_premium_merchant_card, null);

                        SimpleDraweeView sdvBg = v.findViewById(R.id.sdv_premium_merchat_bg);
                   //     SimpleDraweeView sdv_premium_merchat = v.findViewById(R.id.sdv_premium_merchat);
                        TextView tv_titleName = v.findViewById(R.id.tv_titleName);
                        mFlowlayout = v.findViewById(R.id.flowlayout);
                        TextView tv_btnGoStore = v.findViewById(R.id.tv_btnGoStore);

                        FrescoUtils.getInstance().setImageUri(sdvBg, ApiHttpClient.IMG_SERVICE_URL + info.getIndex_img());
                        tv_titleName.setText(info.getName());

                    //    FrescoUtils.getInstance().setImageUri(sdv_premium_merchat, ApiHttpClient.IMG_SERVICE_URL + info.getLogo());

                        List<ModelInfoCategoryBean> cats = info.getCategory();
                        getMerchatTag(cats);

                        final int finalI = i;
                        tv_btnGoStore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtils.showShort(mContext, "this is " + finalI);
                            }
                        });
                        holder.lin_premium.addView(v);

                    }
                }
                return convertView;

            case TYPE_FEATUREED_SERVICE:

                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_featuredservice, null);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                List<ModelItemBean> items = mData.getItem();

                holder.lin_featureService.removeAllViews();
                if (items != null && items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        ModelItemBean info = items.get(i);
                        View v = LayoutInflater.from(mContext).inflate(R.layout.service_item_featuredservice_item1, null);
                        SimpleDraweeView sdvService_bg = v.findViewById(R.id.sdv_service_bg);
                        TextView tv_serviceName = v.findViewById(R.id.tv_serviceName);
                        TextView tv_servicePrice = v.findViewById(R.id.tv_servicePrice);
                        SimpleDraweeView sdv_servicelogo = v.findViewById(R.id.sdv_merchantlogo);
                        TextView tv_organizationName = v.findViewById(R.id.tv_organizationName);
                        mFlowlayoutService = v.findViewById(R.id.flowlayout_service);

                        tv_serviceName.setText(info.getTitle());
                        FrescoUtils.getInstance().setImageUri(sdvService_bg, ApiHttpClient.IMG_SERVICE_URL + info.getTitle_img());

                        tv_organizationName.setText(info.getI_name());
                        String price = info.getPrice();
                        if (!NullUtil.isStringEmpty(price)) {
                            tv_servicePrice.setText("¥" + price);
                        } else {
                            tv_servicePrice.setText("");
                        }
                        FrescoUtils.getInstance().setImageUri(sdv_servicelogo, ApiHttpClient.IMG_SERVICE_URL + info.getLogo());

                        List<ModelInfoCategoryBean> cats = info.getCategory();
                        getMerchatServiceTag(cats);
                        holder.lin_featureService.addView(v);

                    }
                }
                return convertView;
//
            case TYPE_MORE_SERVICE://更多服务

                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_more_service, null);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final ViewHolder finalHolder = holder;
                holder.lin_btn_more_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMoreServiceListener.onItemMoreService(finalHolder);
                    }
                });

                return convertView;
            case TYPE_HOT_TELL://底部广告
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.service_item_bottomad, null);
                    initViewHolder(convertView, holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                return convertView;
        }

        return convertView;
    }

    Intent intent = new Intent();

    //回调
    public interface IMoreServiceListener {

        void onItemMoreService(ViewHolder finalHolder);
    }

    private IMoreServiceListener mMoreServiceListener;

    public void setOnItemMoreServiceListener(IMoreServiceListener mMoreServiceListener) {
        this.mMoreServiceListener = mMoreServiceListener;

    }

    TagFlowLayout mFlowlayout;
    TagFlowLayout mFlowlayoutService;

    /**
     * 获取店铺所有标签
     *
     * @param cats
     */
    private void getMerchatTag(final List<ModelInfoCategoryBean> cats) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        TagAdapter<ModelInfoCategoryBean> adapter = new TagAdapter<ModelInfoCategoryBean>(cats) {

            @Override
            public View getView(FlowLayout parent, int position, ModelInfoCategoryBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.service_merchat_tag_item,
                        mFlowlayout, false);
                tv.setText(cats.get(position).getCategory_cn());
                return tv;
            }
        };
        mFlowlayout.setAdapter(adapter);
    }

    private void getMerchatServiceTag(final List<ModelInfoCategoryBean> cats) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
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


    class ViewHolder {
        LinearLayout lin_hotactivity;
        FunctionAdvertise fc_ads;
        MyGridview my_grid_cate;
        MyListView lv_mostpopluar;
        LinearLayout lin_premium;
        LinearLayout lin_featureService;

        LinearLayout lin_btn_more_service;
        MyListView mlv_more_service;

        TextView tv_merchant_more1;
        TextView tv_featureService_more;

        public ViewHolder(View v) {
            fc_ads = v.findViewById(R.id.fc_ads);
            my_grid_cate = v.findViewById(R.id.my_grid_cate);
            lin_hotactivity = v.findViewById(R.id.lin_hotactivity);
            lv_mostpopluar = v.findViewById(R.id.lv_mostpopluar);
            lin_premium = v.findViewById(R.id.lin_premium);
            lin_featureService = v.findViewById(R.id.lin_featureService);

            lin_btn_more_service = v.findViewById(R.id.lin_btn_more_service);
            mlv_more_service = v.findViewById(R.id.mlv_more_service);

            tv_merchant_more1 = v.findViewById(R.id.tv_merchant_more1);
            tv_featureService_more = v.findViewById(R.id.tv_featureService_more);
        }

        public ViewHolder() {
        }

    }

    private void initViewHolder(View v, ViewHolder viewHolder) {
        viewHolder.fc_ads = v.findViewById(R.id.fc_ads);
        viewHolder.my_grid_cate = v.findViewById(R.id.my_grid_cate);
        viewHolder.lin_hotactivity = v.findViewById(R.id.lin_hotactivity);
        viewHolder.lin_premium = v.findViewById(R.id.lin_premium);
        viewHolder.lin_featureService = v.findViewById(R.id.lin_featureService);
        viewHolder.lv_mostpopluar = v.findViewById(R.id.lv_mostpopluar);

        viewHolder.lin_btn_more_service = v.findViewById(R.id.lin_btn_more_service);
        viewHolder.mlv_more_service = v.findViewById(R.id.mlv_more_service);

        viewHolder.tv_merchant_more1 = v.findViewById(R.id.tv_merchant_more1);
        viewHolder.tv_featureService_more = v.findViewById(R.id.tv_featureService_more);

    }


}
