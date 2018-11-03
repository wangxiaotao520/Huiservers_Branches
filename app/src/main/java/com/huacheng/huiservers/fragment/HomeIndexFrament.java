package com.huacheng.huiservers.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.XiaoquActivity;
import com.huacheng.huiservers.center.bean.PayInfoBean;
import com.huacheng.huiservers.dialog.DownLoadDialog;
import com.huacheng.huiservers.dialog.SignOnDialog;
import com.huacheng.huiservers.dialog.UpdateDialog;
import com.huacheng.huiservers.fragment.activity.HomeListFragmentAdapter;
import com.huacheng.huiservers.fragment.adapter.Home2Article12GridAdapter;
import com.huacheng.huiservers.fragment.adapter.Home2Article3ListAdapter;
import com.huacheng.huiservers.fragment.adapter.HomeCenterGirdAdapter;
import com.huacheng.huiservers.fragment.bean.HomeIndexBean;
import com.huacheng.huiservers.fragment.bean.IndexBean;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.oldhome.adapter.OldListQzAdapter;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.MyGridViewAdpter;
import com.huacheng.huiservers.shop.adapter.MyViewPagerAdapter;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.AppUpdate;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.ImageCycleView;
import com.huacheng.huiservers.view.MyGridview;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.huiservers.view.ScrollViewColor;
import com.huacheng.huiservers.view.VpSwipeRefreshLayout;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.huacheng.libraryservice.widget.FunctionAdvertise;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import cn.com.chinatelecom.account.lib.auth.CtAuth;
import cn.com.chinatelecom.account.lib.model.AuthResultModel;
import cn.jpush.android.api.JPushInterface;

/**
 * 类：
 * 时间：2018/6/2 15:08
 * 功能描述:Huiservers
 */
public class HomeIndexFrament extends BaseFragmentOld {

    private ImageCycleView mAdView;
    SharePrefrenceUtil prefrenceUtil;
    private Jump jump;
    ShopProtocol protocol = new ShopProtocol();
    List<BannerBean> beans = new ArrayList<BannerBean>();//广告数据
    List<BannerBean> beansCenter = new ArrayList<BannerBean>();//广告数据
    CenterProtocol centerProtocol = new CenterProtocol();
    private String apkpath;
    PayInfoBean infoBean = new PayInfoBean();
    private ViewPager viewPager;
    private LinearLayout group;//圆点指示器
    private ImageView[] ivPoints;//小圆点图片的集合
    private int catePage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数量
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中

    private MyListView list_quanzi;
    private MyGridview grid_GG;
    List<HomeIndexBean> mcatelist = new ArrayList<>();
    ImageView iv_background, scanCode;
    RelativeLayout ry_onclick, ry_daohang;
    LinearLayout ly_center_banner, ly_quanzi;
    RecyclerView recyclerview;

    SharedPreferences preferencesLogin;
    private String login_type, login_uid, is_wuye, xiaoqu_id;
    TextView tv_xiaoqu, tv_zhanwei;
    private CtAuth ctAuth;
    public String accessToken;
    private TextView showResultTv, showAccessTokenTv;
    private ArrayList<String> accessTokenList = new ArrayList<>();
    private Handler myHandler = new myHandler();
    LinearLayout ly_community_agreement;
    VpSwipeRefreshLayout swipe_refresh_layout;

    List<IndexBean> mArticle_list = new ArrayList<>();
    List<BannerBean> mSociallist = new ArrayList<>();
    List<ShopIndexBean> mProlist = new ArrayList<>();

    ScrollViewColor scrollView;
    MyGridview gridViewProtocol;
    MyListView listviewArticle;
    //顶部广告
    private FunctionAdvertise fc_ads;

    class myHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    //安装Apk
                    System.out.println("========");
                    Intent intent = new Intent();
                    //执行动作
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    //执行的数据类型
                    intent.setDataAndType(Uri.fromFile((File) msg.obj), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了
                    startActivity(intent);
                    break;
                case 1:
                    AuthResultModel successAuthResult = (AuthResultModel) msg.obj;
                    if (successAuthResult != null) {
                        accessToken = successAuthResult.accessToken;
                    }

                    showResultTv.setText("登录成功：" + successAuthResult.toString());
                    showAccessTokenTv.setText(successAuthResult.accessToken);
                    Log.e("accessToken", successAuthResult.toString());
                    break;
                case 2:
                    AuthResultModel failAuthResult = (AuthResultModel) msg.obj;
                    showResultTv.setText("登录失败：" + failAuthResult.toString());
                    Log.e("accessToken", "ip-" + getIPAddress(getActivity()));
                    break;
                case 0:
                    Toast.makeText(getActivity(), "自定义账号入口", Toast.LENGTH_SHORT).show();
                    showResultTv.setText("自定义处理方式");
                    break;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        fc_ads.stopAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!fc_ads.isCycling()) {
//			fc_ads.moveToFirstPosition(true);
            fc_ads.startCycle();
        }
    }

    /*
     * 从服务器中下载APK
     */
    @SuppressLint("HandlerLeak")
    protected void downLoadApk() {
        final ProgressDialog pds;    //进度条对话框
        pds = new ProgressDialog(getActivity());
        pds.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pds.setCancelable(false);
        final Handler tipHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                double mLengTh = msg.arg1 / 1024f / 1024f;
                System.out.println("-----00000000000");
                DecimalFormat df = new DecimalFormat("#0.00");
                System.out.println("aok_size------" + mLengTh);
                pds.setMessage("正在下载更新-文件大小" + df.format(mLengTh) + "M");
                pds.show();
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("apkpath=======" + apkpath);
                    File file = AppUpdate.getFileFromServer(apkpath, pds, tipHandler);
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = file;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {

                } finally {
                    pds.dismiss();//结束掉进度条对话框
                }
            }
        }.start();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.home_index_3_2;
    }

    @Override
    protected void initView() {
        //    SetTransStatus.GetStatus(getActivity());
        MyCookieStore.Index_notify = 0;
        MyCookieStore.home_notify = 1;
        prefrenceUtil = new SharePrefrenceUtil(getActivity());

        scanCode = (ImageView) findViewById(R.id.iv_scancode);

        mAdView = (ImageCycleView) findViewById(R.id.ad_view);
        viewPager = findViewById(R.id.viewpager);
        group = (LinearLayout) findViewById(R.id.points);
        ly_quanzi = (LinearLayout) findViewById(R.id.ly_quanzi);
        list_quanzi = (MyListView) findViewById(R.id.list_quanzi);
        iv_background = findViewById(R.id.iv_background);
        tv_xiaoqu = findViewById(R.id.tv_xiaoqu);
        //   tv_zhanwei = findViewById(R.id.tv_zhanwei);
        ry_onclick = findViewById(R.id.ry_onclick);
        recyclerview = findViewById(R.id.recyclerview);
        ry_daohang = findViewById(R.id.ry_daohang);
        ly_community_agreement = findViewById(R.id.ly_community_agreement);
        gridViewProtocol = findViewById(R.id.gridView_protocol);
        listviewArticle = findViewById(R.id.listview_article);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        scrollView = findViewById(R.id.scrollView);

        recyclerview.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        ry_onclick.setFocusable(true);
        ry_onclick.setFocusableInTouchMode(true);
        ry_onclick.requestFocus();
        ry_onclick.setOnClickListener(new View.OnClickListener() {//选择小区
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), XiaoquActivity.class);
                intent.putExtra("type", "home");
                startActivityForResult(intent, 3);
                // startActivity(new Intent(getActivity(), ServiceDetailActivity.class));
            }
        });


        grid_GG = findViewById(R.id.grid_GG);
        ly_center_banner = findViewById(R.id.ly_center_banner);


        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(getActivity());
            MyReceiver.setBadgeOfHuaWei(getActivity(), 0);
        }
        initDate();
        xiaoqu_id = prefrenceUtil.getXiaoQuId();
        tv_xiaoqu.setText(prefrenceUtil.getXiaoQuNanme());

        swipe_refresh_layout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        // 设置下拉刷新
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getLinshi();
//                ivPoints = null;
//                if (mcatelist != null) {
//                    group.removeAllViews();
//                    mcatelist.clear();
//                }
//                mSociallist.clear();
//                mProlist.clear();
//                mArticle_list.clear();

                getbanner();
                gethomeIndex();
                getbannerCenter();
                // 延时1s关闭下拉刷新
                swipe_refresh_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing()) {
                            swipe_refresh_layout.setRefreshing(false);
                        }
                    }
                }, 500);

            }
        });
//测试
        scrollView.setOnScrollListener(new ScrollViewColor.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                int i = StringUtils.dip2px(getActivity(), scrollY);
                int dp = StringUtils.px2dip(getActivity(), scrollY);
                if (dp > 150) {
                    ry_onclick.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    ry_onclick.setBackgroundColor(getResources().getColor(R.color.trans_title));
                }
            }
        });
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

        int p_height = StringUtils.dip2px(190);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ry_daohang.getLayoutParams();
        params.height = p_height;
        ry_daohang.setLayoutParams(params);
    }


    private void initDate() {
        showDialog(smallDialog);
        getLinshi();
        getbanner();
        gethomeIndex();
        getbannerCenter();
        verifyStoragePermissions(getActivity());
    }

    private void gethomeIndex() {//获取首页信息
        //    showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.index_32, params, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject obj = new JSONObject(json);
                    String data = obj.getString("data");
                    //mHomeBean = com.alibaba.fastjson.JSONObject.parseObject(data, HomeIndexBean.class);
                    JSONObject objcate = new JSONObject(data);
                    String str_cate = objcate.getString("menu_list");
                    String str_social = objcate.getString("social_list");
                    String bg_img = objcate.getString("bg_img");
                    String str_pro_list = objcate.getString("pro_list");
                    String article_list = objcate.getString("article_list");

                    if (!TextUtils.isEmpty(str_cate)) {
                        ry_daohang.setVisibility(View.VISIBLE);
                        List<HomeIndexBean> homeIndexBeans = JSONArray.parseArray(str_cate, HomeIndexBean.class);
                        mcatelist.clear();
                        mcatelist.addAll(homeIndexBeans);

                        if (mcatelist.size()<4){
                            int p_height = StringUtils.dip2px(190);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ry_daohang.getLayoutParams();
                            params.height = p_height/2;
                            ry_daohang.setLayoutParams(params);
                        }

                        gettype();
                    } else {
                        ry_daohang.setVisibility(View.GONE);
                    }
                    //圈子
                    if (!TextUtils.isEmpty(str_social)) {
                        if (!TextUtils.isEmpty(bg_img)) {
                            Glide
                                    .with(getActivity())
                                    .load(MyCookieStore.URL + bg_img)
                                    .skipMemoryCache(false)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(R.color.all_gray)
                                    .into(iv_background);
                            iv_background.setVisibility(View.VISIBLE);
                        } else {
                            iv_background.setVisibility(View.GONE);
                        }
                        List<BannerBean> bannerBeans = JSONArray.parseArray(str_social, BannerBean.class);
                        mSociallist.clear();
                        mSociallist.addAll(bannerBeans);
                        OldListQzAdapter OldListQzAdapter = new OldListQzAdapter(getActivity(), mSociallist);
                        list_quanzi.setAdapter(OldListQzAdapter);
                        ly_quanzi.setVisibility(View.VISIBLE);
                    } else {
                        ly_quanzi.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(str_pro_list)) {
                        List<ShopIndexBean> shopIndexBeans = JSONArray.parseArray(str_pro_list, ShopIndexBean.class);
                        mProlist.clear();
                        mProlist.addAll(shopIndexBeans);
                        HomeListFragmentAdapter homeAdapter = new HomeListFragmentAdapter(mProlist, getActivity());//, dataSource
                        recyclerview.setAdapter(homeAdapter);
                        recyclerview.setVisibility(View.VISIBLE);
//                        fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_ENDING);
                    } else {
                        recyclerview.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(article_list)) {
                        List<IndexBean> indexBeans = JSONArray.parseArray(article_list, IndexBean.class);
                        mArticle_list.clear();
                        mArticle_list.addAll(indexBeans);
                        if (mArticle_list != null) {
                            BitmapUtils bitmap = new BitmapUtils(getActivity());
                            ly_community_agreement.setVisibility(View.VISIBLE);

                            if (mArticle_list.size() > 0) {
                                Home2Article12GridAdapter article12GridAdapter = new Home2Article12GridAdapter(mArticle_list, getActivity());
                                gridViewProtocol.setAdapter(article12GridAdapter);

                                if (mArticle_list.size() > 2) {//列排协议
                                    listviewArticle.setVisibility(View.VISIBLE);
                                    Home2Article3ListAdapter articleAdapter = new Home2Article3ListAdapter(mArticle_list, getActivity());
                                    listviewArticle.setAdapter(articleAdapter);
                                } else {
                                    listviewArticle.setVisibility(View.GONE);
                                }
                            } else {
                                gridViewProtocol.setVisibility(View.GONE);
                            }

                        } else {
                            ly_community_agreement.setVisibility(View.GONE);
                        }

                    } else {
                        ly_community_agreement.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getbanner() {//获取首页顶部广告信息
        //      showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_index_top");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, getActivity()) {

            @Override
            protected void setData(String json) {
                //              hideDialog(smallDialog);
                //                beans = protocol.bannerInfo(json);
//                if (beans != null) {
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
                //            hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getbannerCenter() {//获取首页中部广告信息
        //    showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_index_center");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, getActivity()) {

            @Override
            protected void setData(String json) {
                //    hideDialog(smallDialog);
                beansCenter = protocol.bannerInfo(json);
                if (beansCenter != null) {
                    ly_center_banner.setVisibility(View.VISIBLE);
                    HomeCenterGirdAdapter mCenterAdapter = new HomeCenterGirdAdapter(getActivity(), beansCenter);
                    grid_GG.setAdapter(mCenterAdapter);

                } else {
                    ly_center_banner.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //              hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(BannerBean info, int position, View imageView) {//点击图片事件
            if (TextUtils.isEmpty(info.getUrl())) {
                if (beans.get(position).getUrl_type().equals("0") || TextUtils.isEmpty(beans.get(position).getUrl_type())) {
                    jump = new Jump(getActivity(), info.getType_name(), info.getAdv_inside_url());
                } else {
                    jump = new Jump(getActivity(), info.getUrl_type(), info.getType_name(), "", info.getUrl_type_cn());
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

    private void getLinshi() {
        preferencesLogin = getActivity().getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        login_uid = preferencesLogin.getString("login_uid", "");
        is_wuye = preferencesLogin.getString("is_wuye", "");
    }

    private void gettype() {
        //总的页数向上取整
        //catePage=Integer.valueOf(catebeans.get(0).getTotal_Pages());
        catePage = (int) Math.ceil(mcatelist.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < catePage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(mActivity, R.layout.item_gridview, null);
            gridView.setAdapter(new MyGridViewAdpter(getActivity(), mcatelist, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    Object obj = gridView.getItemAtPosition(position);
                    if (obj != null && obj instanceof HomeIndexBean) {
                        System.out.println(obj);
                        String type = ((HomeIndexBean) obj).getUrl_type();
                        String[] typeStr = new String[]{"14", "15", "16", "17", "18", "19", "20", "21", "26"};
                        if (Arrays.asList(typeStr).contains(type)) {
                            jump = new Jump(getActivity(), ((HomeIndexBean) obj).getUrl_type(), ((HomeIndexBean) obj).getUrl_id(),
                                    ((HomeIndexBean) obj).getUrl_type_cn());
                        } else {
                            jump = new Jump(getActivity(), ((HomeIndexBean) obj).getUrl_type(), ((HomeIndexBean) obj).getUrl_id(), "",
                                    ((HomeIndexBean) obj).getUrl_type_cn());
                        }
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //添加小圆点

        if (catePage == 1) {
            group.setVisibility(View.GONE);
        } else {
            ivPoints = new ImageView[catePage];
            group.removeAllViews();
            for (int i = 0; i < catePage; i++) {
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(getActivity());
                if (i == 0) {
                    ivPoints[i].setImageResource(R.drawable.page_focuese);
                } else {
                    ivPoints[i].setImageResource(R.drawable.page_unfocused);
                }
                ivPoints[i].setPadding(8, 8, 8, 8);
                group.addView(ivPoints[i]);
            }
        }

        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()

        {
            @Override
            public void onPageSelected(int position) {
                //currentPage = position;
                for (int i = 0; i < catePage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.page_unfocused);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*    if (resultCode == -1) {
            if (!TextUtils.isEmpty(data.getExtras().getString("result"))) {
                if (!isNumeric(data.getExtras().getString("result"))) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(data.getExtras().getString("result"));
                    intent.setData(content_url);
                    startActivity(intent);
                } else {
                    XToast.makeText(getActivity(), data.getExtras().getString("result"), XToast.LENGTH_SHORT).show();
                }

            }
        }*/
        switch (resultCode) {
            case 0:
                break;

            case 1111:

                break;

            case 100:
                String name = data.getExtras().getString("name");
                String id = data.getExtras().getString("id");
                System.out.println("allid====" + id);
                System.out.println("allname====" + name);
                prefrenceUtil.setXiaoQuId(id);
                prefrenceUtil.setXiaoQuName(name);
                xiaoqu_id = id;
                tv_xiaoqu.setText(name);
                break;
            case 200://小区返回值
                String name1 = data.getExtras().getString("name");
                String id1 = data.getExtras().getString("id");
               /* scrollView.fullScroll(ScrollView.FOCUS_UP);
                scrollView.fullScroll(View.FOCUS_UP);*/
                prefrenceUtil.setXiaoQuId(id1);
                prefrenceUtil.setXiaoQuName(name1);
                xiaoqu_id = id1;
                tv_xiaoqu.setText(name1);
                initDate();
               /* if (!login_type.equals("")) {
                    getYh();//获取优惠券接口
                }*/
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    UpdateDialog dialog;

    private void getResult() {//版本更新接口
        //  showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("version", "v" + AppUpdate.getVersionName(getActivity()));
        System.out.println("version-------" + "v" + AppUpdate.getVersionName(getActivity()));
        params.addBodyParameter("type", "1");
        HttpHelper hh = new HttpHelper(info.version_update, params, getActivity()) {

            @Override
            protected void setData(String json) {
                //     hideDialog(smallDialog);
                infoBean = centerProtocol.getApk(json);
                System.out.println("info-------" + infoBean);
                System.out.println("infos-------" + infoBean.getPath());
                if (infoBean.getPath() != null) {
                    apkpath = infoBean.getPath();
                    if (infoBean.getCompel().equals("1")) {//强制更新
                        dialog = new UpdateDialog(getActivity(), 1, infoBean.getMgs(), new UpdateDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String tag) {
                                if (tag.equals("1")) {
                                    if (!isWifi(getActivity())) {
                                        SignOnDialog d = new SignOnDialog(getActivity(), apkpath, "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                                        d.show();
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra("file_name", "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                                        intent.putExtra("download_src", apkpath);
                                        intent.setClass(context, DownLoadDialog.class);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
                        dialog.show();
                        // 将对话框的大小按屏幕大小的百分比设置
                        WindowManager windowManager = getActivity().getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
                        dialog.getWindow().setAttributes(lp);
                        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {//强制更新直接退出

                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (event.getAction() == KeyEvent.ACTION_UP
                                        && keyCode == KeyEvent.KEYCODE_BACK
                                        && event.getRepeatCount() == 0) {
                                    dialog.dismiss();
                                    System.exit(0);
                                }
                                return false;
                            }
                        });

                       /* if (!isWifi(getActivity())) {
                            SignOnDialog d = new SignOnDialog(getActivity(), apkpath, "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                            d.show();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("file_name", "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                            intent.putExtra("download_src", apkpath);
                            intent.setClass(context, DownLoadDialog.class);
                            context.startActivity(intent);
                        }*/
                    } else {
                        dialog = new UpdateDialog(getActivity(), 2, infoBean.getMgs(), new UpdateDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String tag) {
                                if (tag.equals("1")) {
                                    if (!isWifi(getActivity())) {
                                        SignOnDialog d = new SignOnDialog(getActivity(), apkpath, "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                                        d.show();
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra("file_name", "v" + AppUpdate.getVersionName(getActivity()) + ".apk");
                                        intent.putExtra("download_src", apkpath);
                                        intent.setClass(context, DownLoadDialog.class);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                } else {
                                    dialog.dismiss();
                                }

                            }
                        });
                        dialog.show();
                        // 将对话框的大小按屏幕大小的百分比设置
                        WindowManager windowManager = getActivity().getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
                        dialog.getWindow().setAttributes(lp);

                    }
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //      hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
