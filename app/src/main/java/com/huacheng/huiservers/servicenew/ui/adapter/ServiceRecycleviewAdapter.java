package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ModelServiceIndex;
import com.huacheng.huiservers.servicenew.model.ModelCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelCategoryServiceBean;
import com.huacheng.huiservers.servicenew.model.ModelInfoBean;
import com.huacheng.huiservers.servicenew.model.ModelInfoCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelItemBean;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.servicenew.ui.adapter.item.MostPopluarAdapter;
import com.huacheng.huiservers.servicenew.ui.search.ServiceClassifyActivity;
import com.huacheng.huiservers.servicenew.ui.shop.ServiceStoreActivity;
import com.huacheng.huiservers.shop.adapter.MyViewPagerAdapter;
import com.huacheng.huiservers.shop.adapter.ServiceMyGridViewAdpter;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.huacheng.libraryservice.widget.FunctionAdvertise;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 服务首页 广告尺寸dp(242/97)，px(484,194)，比例2.5
 * created by wangxiaotao
 * 2018/9/7 0007 上午 9:28
 */
public class ServiceRecycleviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelCategoryBean> cateAll = new ArrayList<>();
    ModelServiceIndex mData;
    List<ModelAds> mAds;
    Context mContext;
    public static final int TYPE_BANNERTOP = 0;//顶部广告
    public static final int TYPE_ALL_CATEGORY = 1;//全部分类
    public static final int TYPE_HOT_ACTIVITY = 2;//热门活动
    public static final int TYPE_MOST_POPULAR = 3;//最受欢迎
    public static final int TYPE_PREMIUM_MERCHANT = 4;//优质商家
    public static final int TYPE_FEATUREED_SERVICE = 5;//精选服务
    public static final int TYPE_MORE_SERVICE = 6;//更多服务
    public static final int TYPE_HOT_TELL = 7;//底部热线广告图


    public ServiceRecycleviewAdapter(ModelServiceIndex service, List<ModelAds> modelAds_list, Context context) {
        this.mData = service;
        this.mAds = modelAds_list;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder myViewHolder = null;
        switch (viewType) {
            case TYPE_BANNERTOP:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_bannertop, null));

                return myViewHolder;

            case TYPE_ALL_CATEGORY:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_allcategory, null));

                return myViewHolder;

            case TYPE_HOT_ACTIVITY:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_hotactivity, null));

                return myViewHolder;

            case TYPE_MOST_POPULAR:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_mostpopluar, null));
                return myViewHolder;

            case TYPE_PREMIUM_MERCHANT:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_premium_merchant, null));
                return myViewHolder;

            case TYPE_FEATUREED_SERVICE:
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_featuredservice, null));
                return myViewHolder;
            case TYPE_MORE_SERVICE://更多服务
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_more_service, null));
                return myViewHolder;

            case TYPE_HOT_TELL://底部广告
                myViewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_bottomad, null));
                return myViewHolder;
        }
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        switch (holder.getItemViewType()) {
            case TYPE_BANNERTOP:
                if (mAds != null && mAds.size() > 0) {
                    viewHolder.fc_ads.initAds(mAds);
                    //广告点击跳转
                    viewHolder.fc_ads.setListener(new FunctionAdvertise.OnClickAdsListener() {
                        @Override
                        public void OnClickAds(ModelAds ads) {

                            if (TextUtils.isEmpty(ads.getUrl())) {
                                if (ads.getUrl_type().equals("0") || TextUtils.isEmpty(ads.getUrl_type())) {
                                    new Jump(mContext, ads.getType_name(), ads.getAdv_inside_url());
                                } else {
                                    new Jump(mContext, ads.getUrl_type(), ads.getType_name(), "", ads.getUrl_type_cn());
                                }
                            } else {//URL不为空时外链
                                new Jump(mContext, ads.getUrl());

                            }

                        }
                    });
                }
                break;

            case TYPE_ALL_CATEGORY:
                if (mData == null) {
                    viewHolder.rl_service_cate.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.rl_service_cate.getLayoutParams();
                    int cateHeight = StringUtils.dip2px(190);
                    params.height = cateHeight;
                    viewHolder.rl_service_cate.setLayoutParams(params);

                } else {
                    List<ModelCategoryBean> mCate = mData.getCategory();
                    if (mCate != null && mCate.size() > 0) {
                        viewHolder.rl_service_cate.setVisibility(View.VISIBLE);
                        cateAll.clear();
                        cateAll.addAll(mCate);

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.rl_service_cate.getLayoutParams();
                        int p_height = StringUtils.dip2px(190);
                        if (cateAll.size() > 4) {
                            params.height = p_height;
                        } else {
                            params.height = p_height / 2;
                        }
                        viewHolder.rl_service_cate.setLayoutParams(params);

                        getServiceCategory(cateAll, viewHolder);
                    } else {
                        viewHolder.rl_service_cate.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.rl_service_cate.getLayoutParams();
                        int cateHeight = StringUtils.dip2px(190);
                        params.height = cateHeight;
                        viewHolder.rl_service_cate.setLayoutParams(params);
                    }

                }

                break;
            case TYPE_HOT_ACTIVITY:
                viewHolder.lin_hotactivity.setVisibility(View.GONE);
                break;
            case TYPE_MOST_POPULAR:
                if (mData == null) {
                    viewHolder.lin_container_mostPop.setVisibility(View.GONE);

                } else {
                    if (mData != null) {
                        List<ModelCategoryServiceBean> mCateService = mData.getCategory_service();
                        if (mCateService != null && mCateService.size() > 0) {
                            viewHolder.lin_container_mostPop.setVisibility(View.VISIBLE);
                            MostPopluarAdapter mostPopAdapter = new MostPopluarAdapter(mCateService, mContext);
                            viewHolder.lv_mostpopluar.setAdapter(mostPopAdapter);
                        } else {
                            viewHolder.lin_container_mostPop.setVisibility(View.GONE);
                        }
                    }
                }


                break;
            case TYPE_PREMIUM_MERCHANT:
                viewHolder.tv_merchant_more1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.setClass(mContext, MerchantServiceListActivity.class);
                        intent.putExtra("tabType", "");
                        mContext.startActivity(intent);
                    }
                });

                if (mData == null) {
                    viewHolder.lin_container_merchant.setVisibility(View.GONE);
                } else {
                    final List<ModelInfoBean> infos = mData.getInfo();
                    viewHolder.lin_premium.removeAllViews();

                    if (infos != null && infos.size() > 0) {
                        viewHolder.lin_container_merchant.setVisibility(View.VISIBLE);
                        for (int i = 0; i < infos.size(); i++) {

                            final ModelInfoBean info = infos.get(i);
                            View v = LayoutInflater.from(mContext).inflate(R.layout.service_item_premium_merchant_card, null);

                            SimpleDraweeView sdvBg = v.findViewById(R.id.sdv_premium_merchat_bg);
                            SimpleDraweeView sdv_premium_merchat = v.findViewById(R.id.sdv_premium_merchat);
//                        ImageView iv_premium_merchat = v.findViewById(R.id.iv_premium_merchant);
                            TextView tv_titleName = v.findViewById(R.id.tv_titleName);
                            mFlowlayout = v.findViewById(R.id.flowlayout);

                            RelativeLayout rel_merchant_card = v.findViewById(R.id.rel_merchant_card);
                            TextView tv_btnGoStore = v.findViewById(R.id.tv_btnGoStore);

                            FrescoUtils.getInstance().setImageUri(sdvBg, ApiHttpClient.IMG_SERVICE_URL + info.getIndex_img());
                            tv_titleName.setText(info.getName());

                            FrescoUtils.getInstance().setImageUri(sdv_premium_merchat, ApiHttpClient.IMG_SERVICE_URL + info.getLogo());
//                        GlideUtils.getInstance().glideLoadWithCorner(mContext, ApiHttpClient.IMG_SERVICE_URL + info.getLogo(),iv_premium_merchat,R.drawable.icon_girdview);

                            List<ModelInfoCategoryBean> cats = info.getCategory();
                            getMerchatTag(cats);

                            final int finalI = i;
                            rel_merchant_card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //  ToastUtils.showShort(mContext, "this is " + finalI);
                                    String store_id = info.getId();
                                    Intent intent = new Intent(mContext, ServiceStoreActivity.class);
                                    intent.putExtra("store_id", store_id + "");
                                    mContext.startActivity(intent);
                                }
                            });
                            viewHolder.lin_premium.addView(v);

                        }
                    } else {
                        viewHolder.lin_container_merchant.setVisibility(View.GONE);
                    }
                }

                break;
            case TYPE_FEATUREED_SERVICE:
                if (mData == null) {
                    viewHolder.lin_container_service.setVisibility(View.GONE);

                } else {
                    List<ModelItemBean> items = mData.getItem();
                    //服务更多
                    viewHolder.tv_featureService_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent();
                            intent.setClass(mContext, MerchantServiceListActivity.class);
                            intent.putExtra("tabType", "service");
                            mContext.startActivity(intent);
                        }
                    });

                    viewHolder.lin_featureService.removeAllViews();
                    if (items != null && items.size() > 0) {
                        viewHolder.lin_container_service.setVisibility(View.VISIBLE);
                        for (int i = 0; i < items.size(); i++) {
                            final ModelItemBean info = items.get(i);
                            View v = LayoutInflater.from(mContext).inflate(R.layout.service_item_featuredservice_item1, null);
                            SimpleDraweeView sdvService_bg = v.findViewById(R.id.sdv_service_bg);
                            TextView tv_serviceName = v.findViewById(R.id.tv_serviceName);
                            TextView tv_servicePrice = v.findViewById(R.id.tv_servicePrice);
                            SimpleDraweeView sdv_servicelogo = v.findViewById(R.id.sdv_servicelogo);
                            TextView tv_organizationName = v.findViewById(R.id.tv_organizationName);
                            mFlowlayoutService = v.findViewById(R.id.flowlayout_service);
                            LinearLayout ll_service_container = v.findViewById(R.id.ll_service_container);

                            tv_serviceName.setText(info.getTitle());

                            String imgSize = info.getTitle_img_size();
                            int pxdip = StringUtils.dip2px(mContext, 30);//获取屏幕
                            if (!NullUtil.isStringEmpty(imgSize)) {
                                int screenWidth = ToolUtils.getScreenWidth(mContext);
//                            LogUtils.e("screenWidth=" + screenWidth);
                                sdvService_bg.getLayoutParams().width = screenWidth - pxdip;//屏幕边距减去30dp像素
                                Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) - pxdip) / (Double.valueOf(imgSize));
                                sdvService_bg.getLayoutParams().height = (new Double(d)).intValue();

                            } else {
                                sdvService_bg.getLayoutParams().width = (ToolUtils.getScreenWidth(mContext) - pxdip);
                                Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) - pxdip) / 2.5;
                                sdvService_bg.getLayoutParams().height = (new Double(d)).intValue();
                            }

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
                            viewHolder.lin_featureService.addView(v);

                            ll_service_container.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, ServiceDetailActivity.class);
                                    intent.putExtra("service_id", info.getId());
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    } else {
                        viewHolder.lin_container_service.setVisibility(View.GONE);
                    }
                }

                break;
            case TYPE_MORE_SERVICE://更多服务
                if (mData == null) {
                    viewHolder.lin_btn_more_service.setVisibility(View.GONE);
                } else {
                    List<ModelItemBean> items2 = mData.getItem();
                    if (items2 != null && items2.size() > 0) {
                        if (items2.get(0).getTotal_Pages() > 1) {

                            viewHolder.lin_btn_more_service.setVisibility(View.VISIBLE);
                            ViewHolder finalHolder = viewHolder;
                            viewHolder.lin_btn_more_service.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMoreServiceListener.onItemMoreService(viewHolder);
                                }
                            });
                        } else {
                            viewHolder.lin_btn_more_service.setVisibility(View.GONE);
                        }

                    } else {
                        viewHolder.lin_btn_more_service.setVisibility(View.GONE);
                    }
                }

                break;

            case TYPE_HOT_TELL://底部广告
                break;
        }
    }

    private int catePage; //总的页数
    private int mPageSize = 10; //每页显示的最大的数量
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中

    private void getServiceCategory(final List<ModelCategoryBean> cateAll, ViewHolder viewHolder) {
        //总的页数向上取整
        //catePage=Integer.valueOf(catebeans.get(0).getTotal_Pages());
        catePage = (int) Math.ceil(cateAll.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < catePage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(mContext, R.layout.item_gridview_5, null);
            gridView.setAdapter(new ServiceMyGridViewAdpter(mContext, cateAll, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

                    if (position == 0 && arg3 == 1) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ServiceClassifyActivity.class);
                        intent.putExtra("allCate", "0");
                        intent.putExtra("tabType", "service");
                        /*
                        intent.putExtra("sub_id", "0");
                        intent.putExtra("typeName", "全部");*/
                        mContext.startActivity(intent);
                    } else {
                        int posi = (int) arg3;

                        Intent intent = new Intent();
                        intent.setClass(mContext, MerchantServiceListActivity.class);
                        intent.putExtra("tabType", "service");
                        intent.putExtra("sub_id", cateAll.get(posi - 2).getId());
                        intent.putExtra("typeName", cateAll.get(posi - 2).getName());
                        mContext.startActivity(intent);

                    }


                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewHolder.viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //添加小圆点
        if (catePage == 1) {
            viewHolder.group.setVisibility(View.GONE);
        } else {
            ivPoints = new ImageView[catePage];
            viewHolder.group.removeAllViews();
            for (int i = 0; i < catePage; i++) {
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(mContext);
                if (i == 0) {
                    ivPoints[i].setImageResource(R.drawable.page_focuese_scate);
                } else {
                    ivPoints[i].setImageResource(R.drawable.page_unfocused_scate);
                }
                ivPoints[i].setPadding(8, 8, 8, 8);
                viewHolder.group.addView(ivPoints[i]);
            }
        }

        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewHolder.viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()

        {
            @Override
            public void onPageSelected(int position) {
                //currentPage = position;
                for (int i = 0; i < catePage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.page_focuese_scate);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.page_unfocused_scate);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    //回调
    public interface IMoreServiceListener {

        void onItemMoreService(ViewHolder viewHolder);
    }

    private IMoreServiceListener mMoreServiceListener;

    public void setOnItemMoreServiceListener(IMoreServiceListener mMoreServiceListener) {
        this.mMoreServiceListener = mMoreServiceListener;

    }

    TagFlowLayout mFlowlayout;
    TagFlowLayout mFlowlayoutService;

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

    private ImageView[] ivPoints;//小圆点图片的集合

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_hotactivity;
        FunctionAdvertise fc_ads;
        MyListView lv_mostpopluar;
        LinearLayout lin_premium;
        LinearLayout lin_featureService;
        RelativeLayout rl_service_cate;

        public LinearLayout lin_btn_more_service;
        public MyListView mlv_more_service;
        public TextView tv_merchant_more1;
        TextView tv_featureService_more;

        ViewPager viewPager;
        LinearLayout group;

        LinearLayout lin_container_mostPop,
                lin_container_merchant,
                lin_container_service;

        public ViewHolder(View itemView) {
            super(itemView);
            initViewHolder(itemView);
        }

        private void initViewHolder(View v) {
            fc_ads = v.findViewById(R.id.fc_ads);

            rl_service_cate = v.findViewById(R.id.rl_service_cate);
            viewPager = v.findViewById(R.id.viewpager);
            group = v.findViewById(R.id.points);

            lin_hotactivity = v.findViewById(R.id.lin_hotactivity);
            lin_premium = v.findViewById(R.id.lin_premium);
            lin_featureService = v.findViewById(R.id.lin_featureService);
            lv_mostpopluar = v.findViewById(R.id.lv_mostpopluar);

            tv_merchant_more1 = v.findViewById(R.id.tv_merchant_more1);
            lin_btn_more_service = v.findViewById(R.id.lin_btn_more_service);
            mlv_more_service = v.findViewById(R.id.mlv_more_service);

            tv_featureService_more = v.findViewById(R.id.tv_featureService_more);
            lin_container_mostPop = v.findViewById(R.id.lin_container_mostPop);
            lin_container_merchant = v.findViewById(R.id.lin_container_merchant);
            lin_container_service = v.findViewById(R.id.lin_container_service);

        }

    }

}
