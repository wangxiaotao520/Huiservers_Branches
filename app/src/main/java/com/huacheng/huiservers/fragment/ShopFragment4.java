package com.huacheng.huiservers.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.adapter.ShopCardPagerGgAdapter;
import com.huacheng.huiservers.fragment.adapter.ShopMyGirdCateAdapter;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.fragment.card.IndexShadowTransformer;
import com.huacheng.huiservers.fragment.shop.ShopBannerAdapter;
import com.huacheng.huiservers.fragment.shop.ShopLimitAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.SearchShopActivity;
import com.huacheng.huiservers.shop.ShopCartActivityTwo;
import com.huacheng.huiservers.shop.ShopCateActivity;
import com.huacheng.huiservers.shop.ShopXS4TimeListActivity;
import com.huacheng.huiservers.shop.adapter.ShopIndexLodingRecycleCateAdapter;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.ImageCycleView;
import com.huacheng.huiservers.view.MyGridview;
import com.huacheng.huiservers.view.RecycleViewDivider;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.huiservers.view.VpSwipeRefreshLayout;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.DeviceUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.huacheng.libraryservice.widget.FunctionAdvertise;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 类：
 * 时间：2018/5/24 08:20
 * 功能描述:Huiservers
 */
public class ShopFragment4 extends BaseFragmentOld implements View.OnClickListener {

    MyGridview my_grid_cate;
    private ViewPager viewPager_cate;
    private ImageView iv_limit_banner;
    IndexShadowTransformer mIndexFragmentCardShadowTransformer;
    SharePrefrenceUtil prefrenceUtil;
    private ImageCycleView mAdView;
    RelativeLayout ry_cate, ry_gouwu;
    LinearLayout ly_serch, ly_top;
    VpSwipeRefreshLayout swipeRefreshLayout;

    TextView tv_zhanwei, tv_zhanwei2, txt_shop_num, tv_discount_more;
    Jump jump;
    ShopIndexBean shopindex = new ShopIndexBean();
    ShopDetailBean cartnum = new ShopDetailBean();
    ShopProtocol protocol = new ShopProtocol();
    List<BannerBean> beans = new ArrayList<BannerBean>();//广告数据
    List<BannerBean> beansCenter = new ArrayList<BannerBean>();//广告数据
    private int page = 1;//当前页
    SharedPreferences preferencesLogin;
    String login_type;
    List<ShopIndexBean> proList;
    ShopIndexBean proDiscountList;

    ShopIndexLodingRecycleCateAdapter myBottomCateAdapter;

    //顶部广告
    private FunctionAdvertise fc_ads;

    @Override
    public void onResume() {
        super.onResume();
        getLinshi();

        if (!login_type.equals("")) {// 登陆之后获取数量
            getCartNum();
            System.out.println("---------");
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.shop_frament;
    }

    RecyclerView mRecyclerView;
    LinearLayout lin_shop_limit, lin_shop_more;

    NestedScrollView scrollView;
    GridView gridview_horizontal_list, gridview_horizontal_more1;
    HorizontalScrollView horizontalSV,horizontal_ad_middle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        //  SetTransStatus.GetStatus(getActivity());
        prefrenceUtil = new SharePrefrenceUtil(getActivity());
        MyCookieStore.Shop_notify = 0;

        scrollView = findViewById(R.id.scrollView);
        my_grid_cate = findViewById(R.id.my_grid_cate);
        ry_cate = findViewById(R.id.ry_cate);
        ry_gouwu = findViewById(R.id.ry_gouwu);
        ly_serch = findViewById(R.id.ly_serch);
        txt_shop_num = findViewById(R.id.txt_shop_num);
        ly_top = findViewById(R.id.ly_top);
        //   tv_zhanwei = findViewById(R.id.tv_zhanwei);
        tv_zhanwei2 = findViewById(R.id.tv_zhanwei2);

        mAdView = findViewById(R.id.ad_view);
        viewPager_cate = findViewById(R.id.viewPager_cate);
        //下拉控件
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        lin_shop_limit = findViewById(R.id.lin_shop_limit);
        tv_discount_more = findViewById(R.id.tv_discount_more);
        horizontalSV = findViewById(R.id.horizontalSV);
        horizontal_ad_middle = findViewById(R.id.horizontal_ad_middle);

        iv_limit_banner = findViewById(R.id.iv_limit_banner);

        gridview_horizontal_list = findViewById(R.id.gridview_horizontal_list);
        gridview_horizontal_more1 = findViewById(R.id.gridview_horizontal_more1);

        lin_shop_more = findViewById(R.id.lin_shop_more);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, 22, getResources().getColor(R.color.all_gray)));
        initdata();

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                isRefresh = true;
                page = 1;
                //initdata();
                getLinshi();
                if (!login_type.equals("")) {// 登陆之后获取数量
                    getCartNum();
                    System.out.println("---------");
                }
                getbanner();//获取商城首页顶部广告信息
                //getbannerH5();//获取商城首页中部软文广告信息
                getbannerCenter();
                getindex();//商城首页
                // 延时1s关闭下拉刷新
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int i = StringUtils.dip2px(getActivity(), scrollY);

                int dp = StringUtils.px2dip(getActivity(), scrollY);
                //   int adBottom = mAdView.getBottom();
                int adBottom = DeviceUtils.dip2px(mActivity, 180);
                int topBottom = ly_top.getBottom();
                int addp = StringUtils.px2dip(getActivity(), adBottom);
                int topdp = StringUtils.px2dip(getActivity(), topBottom);

                Log.d("ShopFragment4", adBottom + "");
                if (dp > (addp - topdp)) {//150
                    ly_top.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    ly_top.setBackgroundColor(getResources().getColor(R.color.trans_title));
                }
            }
        });
       /* scrollView.setOnScrollListener(new ScrollViewColor.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {

            }
        });*/
        ry_gouwu.setOnClickListener(this);
        ry_cate.setOnClickListener(this);
        ly_serch.setOnClickListener(this);
        iv_limit_banner.setOnClickListener(this);
        tv_discount_more.setOnClickListener(this);

        fc_ads = (FunctionAdvertise) findViewById(R.id.fc_ads);
        //广告点击跳转
        fc_ads.setListener(new FunctionAdvertise.OnClickAdsListener() {
            @Override
            public void OnClickAds(ModelAds ads) {
                if (TextUtils.isEmpty(ads.getUrl())) {
                    if (ads.getUrl_type().equals("0") || TextUtils.isEmpty(ads.getUrl_type())) {
                        jump = new Jump(getActivity(), ads.getType_name(), ads.getAdv_inside_url());
                    } else {
                        jump = new Jump(getActivity(), ads.getUrl_type(), ads.getType_name(), "", ads.getUrl_type_cn());
                    }
                } else {//URL不为空时外链
                    jump = new Jump(getActivity(), ads.getUrl());

                }
            }
        });
    }


    boolean isRefresh = false;

    private void getLinshi() {
        preferencesLogin = getActivity().getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    private void initdata() {
        showDialog(smallDialog);
        getLinshi();
        getbanner();//获取商城首页顶部广告信息
        getbannerCenter();
        // getbannerH5();//获取商城首页中部软文广告信息
        getindex();//商城首页
        if (!login_type.equals("")) {// 登陆之后获取数量
            getCartNum();
            System.out.println("---------");
        }
    }


    private void getbanner() {//获取商城首页顶部广告信息
        //   showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_shopindex");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, getActivity()) {

            @Override
            protected void setData(String json) {
                //        hideDialog(smallDialog);
//                beans = protocol.bannerInfo(json);
//                if (beans != null) {
//                    MyCookieStore.Shop_notify = 0;
//                    mAdView.setVisibility(View.VISIBLE);
//                    tv_zhanwei.setVisibility(View.GONE);
//                    //获取图片的宽高--start
//                    mAdView.getLayoutParams().width = ToolUtils.getScreenWidth(getActivity());
//                    Double d = Double.valueOf(ToolUtils.getScreenWidth(getActivity())) / (Double.valueOf(beans.get(0).getImg_size()));
//                    mAdView.getLayoutParams().height = (new Double(d)).intValue();
//                    //获取图片的宽高--end
//                    System.out.println("777777777777---------" + beans.size());
//                    mAdView.setImageResources(beans, mAdCycleViewListener);
//                } else {
//                    mAdView.setVisibility(View.GONE);
//                    tv_zhanwei.setVisibility(View.VISIBLE);
//                }
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        List<ModelAds> modelAds_list = JsonUtil.getInstance().getDataArrayByName(jsonObject, "data", ModelAds.class);
                        if (modelAds_list != null && modelAds_list.size() > 0) {
                            fc_ads.initAds(modelAds_list);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //         hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getbannerH5() {//获取商城首页中部软文广告信息
        showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_shop_center");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, getActivity()) {

            @Override
            protected void setData(String json) {
//                LogJson.iJsonFormat("get_Advertising",json);
                MyCookieStore.Shop_notify = 0;
                beansCenter = protocol.bannerInfo(json);
                if (beansCenter != null) {
                    //实例Viewpager适配器
                    ShopCardPagerGgAdapter mIndexFragmentCardAdapter = new ShopCardPagerGgAdapter(beansCenter, getFragmentManager(),
                            StringUtils.dpToPixels(0, getActivity()));
                    mIndexFragmentCardShadowTransformer = new IndexShadowTransformer(viewPager_cate, mIndexFragmentCardAdapter);
                    viewPager_cate.setAdapter(mIndexFragmentCardAdapter);
                    viewPager_cate.setPageTransformer(false, mIndexFragmentCardShadowTransformer);
                    viewPager_cate.setVisibility(View.VISIBLE);
                    tv_zhanwei2.setVisibility(View.GONE);


                } else {
                    viewPager_cate.setVisibility(View.GONE);
                    tv_zhanwei2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private void getindex() {//获取商城分类以及底部商品数据
        //    showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.shop_index_new, params, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                MyCookieStore.Shop_notify = 0;
                shopindex = protocol.getShop3_2_index(json);
                if (shopindex!=null){
                    if (shopindex.getCate_list() != null) {
                        ShopMyGirdCateAdapter myGirdCateAdapter = new ShopMyGirdCateAdapter(getActivity(), shopindex.getCate_list());
                        my_grid_cate.setAdapter(myGirdCateAdapter);

                        if (isRefresh) {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            isRefresh = false;
                        }
                    }
                    proDiscountList = shopindex.getPro_discount_list();
                    if (proDiscountList != null) {
                        List<ShopIndexBean> limits = proDiscountList.getList();
                        if (limits != null && limits.size() > 0) {
                            lin_shop_limit.setVisibility(View.VISIBLE);
                            if (!StringUtils.isEmpty(proDiscountList.getCate_img())) {
                                Glide.with(getActivity())
                                        .load(MyCookieStore.URL + proDiscountList.getCate_img())
                                        .skipMemoryCache(false)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(iv_limit_banner);
                            }

                            tv_discount_more.setVisibility(View.VISIBLE);
                            horizontalSV.setVisibility(View.VISIBLE);
                            setGridView(limits, limits.size());//
                        } else {
                            lin_shop_limit.setVisibility(View.GONE);
                            horizontalSV.setVisibility(View.GONE);
                        }

                    } else {
                        lin_shop_limit.setVisibility(View.GONE);
                        tv_discount_more.setVisibility(View.GONE);
                    }

                    proList = shopindex.getPro_list();
                    if (proList != null && proList.size() > 0) {
                        lin_shop_more.setVisibility(View.VISIBLE);
                        myBottomCateAdapter = new ShopIndexLodingRecycleCateAdapter(getActivity(), txt_shop_num, proList);
                        manager = new RecyclerViewNoBugLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                        mRecyclerView.setNestedScrollingEnabled(false);
                        mRecyclerView.setLayoutManager(manager);
                        mRecyclerView.setAdapter(myBottomCateAdapter);
                    } else {
                        lin_shop_more.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                isRefresh=false;
            }
        };
    }


    private void getbannerCenter() {//获取中部广告信息(限时抢购下面的广告)
        //     showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_shop_center");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, getActivity()) {

            @Override
            protected void setData(String json) {
                //     hideDialog(smallDialog);
                beansCenter = protocol.bannerInfo(json);
                if (beansCenter != null) {
                    horizontal_ad_middle.setVisibility(View.VISIBLE);
                    setGridViewMore(beansCenter, beansCenter.size());
                } else {
                    horizontal_ad_middle.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //        hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void setGridViewMore(List<BannerBean> beansCenter, int size_) {
        int size = size_;
        int length = 180;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview_horizontal_more1.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridview_horizontal_more1.setColumnWidth(itemWidth); // 设置列表项宽
        gridview_horizontal_more1.setHorizontalSpacing(5); // 设置列表项水平间距//5
        gridview_horizontal_more1.setStretchMode(GridView.NO_STRETCH);
        gridview_horizontal_more1.setNumColumns(size); // 设置列数量=列表集合数

        ShopBannerAdapter bannerAdapter = new ShopBannerAdapter(beansCenter, getActivity());
        gridview_horizontal_more1.setAdapter(bannerAdapter);
    }

    ShopLimitAdapter limitAdapter;

    private void setGridView(List<ShopIndexBean> mIndex, int size_) {
        int size = size_;
        int length = 145;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview_horizontal_list.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridview_horizontal_list.setColumnWidth(itemWidth); // 设置列表项宽
        gridview_horizontal_list.setHorizontalSpacing(5); // 设置列表项水平间距//5
        gridview_horizontal_list.setStretchMode(GridView.NO_STRETCH);
        gridview_horizontal_list.setNumColumns(size); // 设置列数量=列表集合数

        limitAdapter = new ShopLimitAdapter(mIndex, getActivity());
        gridview_horizontal_list.setAdapter(limitAdapter);
    }

    LinearLayoutManager manager;

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(BannerBean info, int position, View imageView) {//点击图片事件
            if (TextUtils.isEmpty(info.getUrl())) {
                if (beans.get(position).getUrl_type().equals("0") || TextUtils.isEmpty(beans.get(position).getUrl_type())) {
                    jump = new Jump(getActivity(), info.getType_name(), info.getAdv_inside_url());
                } else {
                    jump = new Jump(getActivity(), info.getUrl_type(), info.getType_name(), "", "");
                }
            } else {//URL不为空时外链
                jump = new Jump(getActivity(), info.getUrl());

            }
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };


    Intent intent = new Intent();
    Bundle bundle = new Bundle();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ry_cate://分类
                startActivity(new Intent(getActivity(), ShopCateActivity.class));
                break;
            case R.id.ry_gouwu://购物车
                getLinshi();
                if (!login_type.equals("")) {// 登陆之后获取数量
                    startActivity(new Intent(getActivity(), ShopCartActivityTwo.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginVerifyCode1Activity.class));
                }
                break;
            case R.id.ly_serch://搜索
                startActivity(new Intent(getActivity(), SearchShopActivity.class));
                break;
            case R.id.tv_discount_more:
                intent.setClass(getActivity(), ShopXS4TimeListActivity.class);
                bundle.putString("cateID", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_limit_banner:
                intent.setClass(getActivity(), ShopXS4TimeListActivity.class);
                bundle.putString("cateID", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }

    }

    /*//Fragment中的onHiddenChanged方法在这里可以更新界面数据
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        } else {
            if (MyCookieStore.Shop_notify == 1) {
                // getLinshi();
                getbanner();//顶部广告
                getindex();
               // getbannerH5();//中部广告
            }
            getLinshi();
            if (!login_type.equals("")) {// 登陆之后获取数量
                getCartNum();
                System.out.println("---------");
            }

        }
    }*/

    private void getCartNum() {// 购物车商品数量
        Url_info info = new Url_info(getActivity());
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.send(HttpRequest.HttpMethod.POST, info.cart_num, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cartnum = protocol.getCartNum(responseInfo.result);
                if (cartnum != null) {
                    if (cartnum.getCart_num().equals("0")) {
                        txt_shop_num.setVisibility(View.GONE);
                    } else {
                        txt_shop_num.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (limitAdapter != null) {
            limitAdapter.cancelAllTimers();
        }
    }
}
