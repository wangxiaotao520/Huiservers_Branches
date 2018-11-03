package com.huacheng.huiservers.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.AddShopDialog;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.ShopDetailListAdapter;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.shop.bean.ShopMainBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.ImageCycleView.ImageCycleViewListener;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.huiservers.view.ScrollChangedScrollView;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.sharesdk.PopupWindowShare;
import com.huacheng.libraryservice.utils.AppConstant;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.linkme.LinkedMeUtils;
import com.huacheng.libraryservice.utils.timer.CountDownTimer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.microquation.linkedme.android.referral.LMError;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.mlink.annotation.MLinkRouter;

@MLinkRouter(keys = "goods_details")//second为mLink的key
public class ShopDetailActivity extends BaseUI implements OnClickListener {
    // /
    private TabLayout tab_tagContainer;
    private ScrollChangedScrollView sv_bodyContainer;
    LinearLayout ll_shop_head, ll_shop_detail, ll_shop_tuijian, ll_shop_pingjia;
    // 头部导航标签

    private String[] navigationTag1 = {"商品", "详情", "评价"};
    private String[] navigationTag2 = {"商品", "详情"};

    int istag;
    /**
     * 是否是ScrollView主动动作 false:是ScrollView主动动作 true:是TabLayout 主动动作
     */
    private boolean tagFlag = false;
    /**
     * 用于切换内容模块，相应的改变导航标签，表示当一个所处的位置
     */
    private int lastTagIndex = 0;
    /**
     * 用于在同一个内容模块内滑动，锁定导航标签，防止重复刷新标签 true: 锁定 false ; 没有锁定
     */
    private boolean content2NavigateFlagInnerLock = false;

    // /
    private LinearLayout lin_left, lin_add_ss, lin_img, lin_bottom, lin_title, lin_goumai, lin_yixuanze, title_rel, lin_XS_bottom, lin_goodsTag;
    private RelativeLayout rel_pingjia, rel_gouwuche, rel_cancel;
    private TextView title_name, txt_name, txt_content, txt_price, txt_num, tag_name, txt_tag1, txt_tag2, right, txt_tag3, txt_tag4, txt_market, txt_shop_num,
            txt_paisong, txt_yuan_price, txt_tuijian, tag_guige, tv_XS_type;
    private GridView grid_view;
    private MyListView list_pingjia;
    private String shop_id, login_type, url;
    private ImageView img_title, iv_top, iv_img, img_1, img_2, img_3, img_4;
    List<ShopMainBean> beans = new ArrayList<ShopMainBean>();
    ShopProtocol protocol = new ShopProtocol();
    ShopDetailBean detailBean = new ShopDetailBean();
    List<ShopDetailBean> beanTag = new ArrayList<ShopDetailBean>();
    ShopDetailBean cartnum = new ShopDetailBean();
    SharedPreferences preferencesLogin;
    //BitmapUtils bitmapUtils;
    SharePrefrenceUtil prefrenceUtil;
    private String URL, txt_time_type;
    int height;
    private View contentView, view1, view2, view_pingjia;
    private int scrollY = 0;// 标记上次滑动位置
    private String start, end;
    private Date data_start, data_now, data_end;
    private boolean isRun = true;
    View view_tag;

    LinearLayout lin_downcount;
    TextView tv_downcount_day, tv_downcount_hour, tv_downcount_minute, tv_downcount_second, tv_time_tag, tv_time_type;

    RelativeLayout rel_shop_limit_bg;
    private SparseArray<CountDownTimer> countDownCounters;

    private LinearLayout ly_share;//分享按钮
    private String share_url;
    private String share_title;
    private String share_desc;
    private String share_icon;
    long mDay, mHour, mMin, mSecond, mTotalHour;


    @Override
    public void onResume() {
        super.onResume();

        title_name.setFocusable(true);
        title_name.setFocusableInTouchMode(true);
        title_name.requestFocus();
        getLinshi();
        //initData();
        if (!login_type.equals("")) {// 登陆之后获取数量
            getCartNum();
            System.out.println("---------");
        }
        //清除角标（华为）
        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(this);
            MyReceiver.setBadgeOfHuaWei(this, 0);
        }
        // 暂时先隐藏轮播 //
        // mAdView.startImageCycle();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.shop_detail);
//        SetTransStatus.GetStatus(this);// 系统栏默认为黑色

        initializeView();
        installListener();
        //bitmapUtils = new BitmapUtils(this);
        // 获取登陆值来判断是否登陆
        getLinshi();
        prefrenceUtil = new SharePrefrenceUtil(this);
        lin_bottom = (LinearLayout) findViewById(R.id.lin_bottom);//底部栏
        lin_XS_bottom = (LinearLayout) findViewById(R.id.lin_XS_bottom);//底部栏
        tv_XS_type = (TextView) findViewById(R.id.tv_XS_type);
        view_tag = (View) findViewById(R.id.view_tag);
        // 初始化ids
        if (!TextUtils.isEmpty(this.getIntent().getExtras().getString("shop_id"))) {
            shop_id = this.getIntent().getExtras().getString("shop_id");
        } else if (!TextUtils.isEmpty(this.getIntent().getExtras().getString("url"))) {
            url = this.getIntent().getExtras().getString("url");
            shop_id = url.substring(url.lastIndexOf("/") + 1, url.length());
        } else {
            url = this.getIntent().getExtras().getString("params_url");
            shop_id = url.substring(url.lastIndexOf("/") + 1, url.length());
            Log.e("URL", url);
        }

        // 标题栏
        // setTitleLayout(findViewById(R.id.lin_title));
        lin_title = (LinearLayout) findViewById(R.id.lin_title);
        title_rel = (LinearLayout) findViewById(R.id.title_rel);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setTextColor(getResources().getColor(R.color.orange_backg));

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view_pingjia = findViewById(R.id.view_pingjia);
        // 为您推荐id 与 评价列表id
        grid_view = (GridView) findViewById(R.id.grid_view);
        list_pingjia = (MyListView) findViewById(R.id.list_pingjia);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        iv_top.setOnClickListener(this);
        // 商品详情id
        img_title = (ImageView) findViewById(R.id.img_title);
        // 底部按钮栏
        rel_gouwuche = (RelativeLayout) findViewById(R.id.rel_gouwuche);// 购物车
        lin_add_ss = (LinearLayout) findViewById(R.id.lin_add_ss);// 加入购物车
        lin_goumai = (LinearLayout) findViewById(R.id.lin_goumai);//立即够买
        rel_cancel = (RelativeLayout) findViewById(R.id.rel_cancel);// 选择商品类型
        rel_pingjia = (RelativeLayout) findViewById(R.id.rel_pingjia);// 评价
        rel_pingjia.setOnClickListener(this);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_content = (TextView) findViewById(R.id.txt_content);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_yuan_price = (TextView) findViewById(R.id.txt_yuan_price);
        txt_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
        //商品规格
        lin_yixuanze = (LinearLayout) findViewById(R.id.lin_yixuanze);
        tag_guige = (TextView) findViewById(R.id.tag_guige);
        txt_num = (TextView) findViewById(R.id.txt_num);
        tag_name = (TextView) findViewById(R.id.tag_name);
        lin_goodsTag = (LinearLayout) findViewById(R.id.lin_goodsTag);

        txt_market = (TextView) findViewById(R.id.txt_market);
        txt_paisong = (TextView) findViewById(R.id.txt_paisong);
        txt_shop_num = (TextView) findViewById(R.id.txt_shop_num); // 商品图片买描述 //
        txt_tuijian = (TextView) findViewById(R.id.txt_tuijian); // 商品描述图片列表id
        lin_img = (LinearLayout) findViewById(R.id.lin_img);
        tv_time_tag = (TextView) findViewById(R.id.tv_time_tag);

        rel_shop_limit_bg = (RelativeLayout) findViewById(R.id.rel_shop_limit_bg);
        tv_time_type = (TextView) findViewById(R.id.tv_time_type); // 商品描述图片列表id
        lin_downcount = (LinearLayout) findViewById(R.id.lin_downcount); // 商品描述图片列表id

        tv_downcount_day = (TextView) findViewById(R.id.tv_downcount_day); // 商品描述图片列表id
        tv_downcount_hour = (TextView) findViewById(R.id.tv_downcount_hour); // 商品描述图片列表id
        tv_downcount_minute = (TextView) findViewById(R.id.tv_downcount_minute); // 商品描述图片列表id
        tv_downcount_second = (TextView) findViewById(R.id.tv_downcount_second); // 商品描述图片列表id

        countDownCounters = new SparseArray<>();

        /*final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ImageView iv_img= (ImageView) findViewById(R.id.iv_img);
                        iv_img.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });*/

        title_rel.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        title_name.setTextColor(Color.argb((int) 0, 0, 0, 0));
        view1.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        view2.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        tab_tagContainer.setSelectedTabIndicatorColor(Color.argb((int) 0, 0, 0, 0));
        tab_tagContainer.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        tab_tagContainer.setTabTextColors(Color.argb((int) 0, 0, 0, 0), Color.argb((int) 0, 0, 0, 0));
        rel_cancel.setOnClickListener(this);
        // rel_cancel.setOnClickListener(this);
        rel_gouwuche.setOnClickListener(this);
        lin_add_ss.setOnClickListener(this);
        lin_goumai.setOnClickListener(this);

        initListeners();
        ly_share = findViewById(R.id.ly_share);
        ly_share.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        getDetail();

        //getpingjia();
        if (!login_type.equals("")) {// 登陆之后获取数量
            getCartNum();
            System.out.println("---------");
        }
    }

    private void initListeners() {
        ViewTreeObserver vto = img_title.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lin_title.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = img_title.getHeight() / 3;
                //sv_bodyContainer.setScrollViewListener(GoodsDetailActivity.this);
            }
        });
    }

    private void initializeView() {
        tab_tagContainer = (TabLayout) findViewById(R.id.anchor_tagContainer);
        sv_bodyContainer = (ScrollChangedScrollView) findViewById(R.id.anchor_bodyContainer);

        ll_shop_head = (LinearLayout) findViewById(R.id.ll_shop_head);
        ll_shop_detail = (LinearLayout) findViewById(R.id.ll_shop_detail);
        //ll_shop_tuijian = (LinearLayout) findViewById(R.id.ll_shop_tuijian);
        ll_shop_pingjia = (LinearLayout) findViewById(R.id.ll_shop_pingjia);
    }

    private void refreshView() {
        if (istag == 1) {
            ll_shop_head.setTag(navigationTag1[0]);
            ll_shop_detail.setTag(navigationTag1[1]);
            ///ll_shop_tuijian.setTag(navigationTag1[2]);
            ll_shop_pingjia.setTag(navigationTag1[2]);
            // 添加页内导航标签
            for (String item : navigationTag1) {
                tab_tagContainer.addTab(tab_tagContainer.newTab().setText(item));
            }

        } else if (istag == 2) {
            ll_shop_head.setTag(navigationTag2[0]);
            ll_shop_detail.setTag(navigationTag2[1]);
            //ll_shop_tuijian.setTag(navigationTag2[2]);
            //ll_shop_pingjia.setTag(navigationTag1[3]);
            // 添加页内导航标签
            for (String item : navigationTag2) {
                tab_tagContainer.addTab(tab_tagContainer.newTab().setText(item));
            }

        }
    }

    private void installListener() {
        sv_bodyContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 表明当前的动作是由 ScrollView 触发和主导
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tagFlag = true;
                }
                return false;
            }
        });
        sv_bodyContainer.setScrollViewListener(new ScrollChangedScrollView.ScrollViewListener() {

            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                scrollRefreshNavigationTag(scrollView);
                if (y <= 0) {   //设置标题的背景颜色
                    lin_title.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    title_rel.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    title_name.setTextColor(Color.argb((int) 0, 0, 0, 0));
                    view1.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    view2.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    tab_tagContainer.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    tab_tagContainer.setTabTextColors(Color.argb((int) 0, 0, 0, 0), Color.argb((int) 0, 0, 0, 0));
                    tab_tagContainer.setSelectedTabIndicatorColor(Color.argb((int) 0, 0, 0, 0));
                } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    //  textView.setTextColor(Color.argb((int) alpha, 255,255,255));
                    lin_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    title_rel.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    //title_name.setTextColor(Color.argb((int) alpha, 255,128,0));
                    title_name.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    view1.setBackgroundColor(Color.argb((int) alpha, 220, 220, 220));
                    view2.setBackgroundColor(Color.argb((int) alpha, 220, 220, 220));
                    /* 1.非选中标签的字体颜色
                     * 2. 选中标签的字体颜色
                     * */
                    tab_tagContainer.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));

                    tab_tagContainer.setTabTextColors(getResources().getColor(R.color.gray2),
                            getResources().getColor(R.color.colorPrimary));
                    //设置选中标签下方导航条的颜色
                    tab_tagContainer.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

                }
            }

            @Override
            public void onScrollStop(boolean isStop) {
                doOnBorderListener();
            }


        });

        tab_tagContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 表明当前的动作是由 TabLayout 触发和主导
                tagFlag = false;
                // 根据点击的位置，使ScrollView 滑动到对应区域
                int position = tab.getPosition();
                // 计算点击的导航标签所对应内容区域的高度
                int targetY = 0;
                switch (position) {
                    case 0:
                        targetY = ll_shop_head.getTop();
                        break;
                    case 1:
                        title_rel.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                        lin_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                        targetY = ll_shop_detail.getTop() - lin_title.getHeight();
                        break;
                    /*case 2:
                    title_rel.setBackgroundColor(Color.argb((int) 255, 255,255,255));
					lin_title.setBackgroundColor(Color.argb((int) 255, 255,255,255));
					targetY = ll_shop_tuijian.getTop()-lin_title.getHeight();
					break;*/
                    case 2:
                        title_rel.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                        lin_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                        targetY = ll_shop_pingjia.getTop() - lin_title.getHeight();
                        break;
                    default:
                        break;
                }
                // 移动到对应的内容区域
                sv_bodyContainer.smoothScrollTo(0, targetY);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 内容区域滑动刷新导航标签
     *
     * @param scrollView 内容模块容器
     */
    protected void scrollRefreshNavigationTag(ScrollView scrollView) {
        if (scrollView == null) {
            return;
        }
        // 获得ScrollView滑动距离
        int scrollY = scrollView.getScrollY();
        // 确定ScrollView当前展示的顶部内容属于哪个内容模块
        if (scrollY > ll_shop_pingjia.getTop() - lin_title.getHeight()) {
            refreshContent2NavigationFlag(2);

        } /*else if (scrollY > ll_shop_tuijian.getTop()-lin_title.getHeight()) {
            refreshContent2NavigationFlag(2);

		} */ else if (scrollY > ll_shop_detail.getTop() - lin_title.getHeight()) {
            refreshContent2NavigationFlag(1);

        } else if (scrollY >= ll_shop_head.getTop()) {
            refreshContent2NavigationFlag(0);
        }
    }

    /**
     * 刷新标签
     *
     * @param currentTagIndex 当前模块位置
     */
    private void refreshContent2NavigationFlag(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
                tab_tagContainer.setScrollPosition(currentTagIndex, 0, true);
            }
        }
        lastTagIndex = currentTagIndex;
    }

    private void getLinshi() {
        preferencesLogin = ShopDetailActivity.this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    private void getDetail() {// 获取商品详情
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        if (!TextUtils.isEmpty(shop_id)) {
            url = info.goods_details + "id/" + shop_id;
        }
        HttpHelper hh = new HttpHelper(url, ShopDetailActivity.this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                detailBean = protocol.getDetail(json);
                title_name.setText(detailBean.getTitle());

                Glide.with(ShopDetailActivity.this).load(MyCookieStore.URL + detailBean.getTitle_img()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_goods_bg).error(R.drawable.ic_goods_bg).into(img_title);
                txt_name.setText(detailBean.getTitle());
                txt_content.setText(detailBean.getDescription());
                txt_price.setText("¥" + detailBean.getPrice());
                txt_yuan_price.setText("¥" + detailBean.getOriginal());
                txt_num.setText("剩余：" + detailBean.getInventory() + " " + detailBean.getUnit());
                //tag_name.setText(detailBean.getTagname());//默认选择已选择的商品规格
                if (detailBean.getExist_hours().equals("2")) {// 判断是否打烊
                    txt_paisong.setVisibility(View.VISIBLE);
                } else {
                    txt_paisong.setVisibility(View.GONE);
                }
                txt_market.setText(detailBean.getSend_shop());
                if (detailBean.getCart_num().equals("") || detailBean.getCart_num().equals("0")) {
                    txt_shop_num.setVisibility(View.GONE);
                } else {
                    txt_shop_num.setVisibility(View.VISIBLE);
                    // txt_shop_num.setText(detailBean.getCart_num());
                }

                getAddGoodsTagView();

                //暂时隐藏掉数据改为静态图片显示
                /*img_1.setBackground(getResources().getDrawable(R.drawable.mianfeituihuan_08));
                img_2.setBackground(getResources().getDrawable(R.drawable.huodaofukuan_01));
				img_3.setBackground(getResources().getDrawable(R.drawable.jishisong_02));
				img_4.setBackground(getResources().getDrawable(R.drawable.jieritehui_10));*/
                /*txt_tag1.setText(detailBean.getGoods_tag().get(0).getC_name());
                txt_tag2.setText(detailBean.getGoods_tag().get(1).getC_name());
				txt_tag3.setText(detailBean.getGoods_tag().get(2).getC_name());
				txt_tag4.setText(detailBean.getGoods_tag().get(3).getC_name());*/
                System.out.println("detailBean.getImgs()===========" + detailBean.getImgs());
                getAddview();// 动态添加商品图片描述
                getpingjia();// 获取商品详情中的评价数据

                if (detailBean.getDiscount().equals("1")) {//限购为1  否则为0
                    rel_shop_limit_bg.setVisibility(View.VISIBLE);
                    lin_bottom.setVisibility(View.VISIBLE);//购物车
                    gettime(detailBean);
                } else {
                    rel_shop_limit_bg.setVisibility(View.GONE);
                    lin_XS_bottom.setVisibility(View.GONE);//底部限时抢购
                    lin_bottom.setVisibility(View.VISIBLE);//购物车
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private void gettime(final ShopDetailBean data) {//限购时间
        String distanceStart = data.getDistance_start();
        String distanceEnd = data.getDistance_end();
        String distance_str = "";
        long distance_int = 0;
        int distance_tag = 0;
        long longDistanceStart;
        long longDistanceEnd;

        if (!NullUtil.isStringEmpty(distanceStart) && !NullUtil.isStringEmpty(distanceEnd)) {
            long distance_long = Long.parseLong(distanceStart);
            if (distance_long > 0) {
                distance_str = "距开始";
                distance_tag = 1;
                distance_int = Long.parseLong(distanceStart);
                lin_XS_bottom.setVisibility(View.VISIBLE);//底部限时抢购
                lin_bottom.setVisibility(View.GONE);//购物车
            } else {
                distance_str = "距结束";
                distance_tag = 0;
                distance_int = Long.parseLong(distanceEnd);
                lin_XS_bottom.setVisibility(View.GONE);//底部限时抢购
                lin_bottom.setVisibility(View.VISIBLE);//购物车
            }
            txt_time_type = distance_str;
            tv_time_tag.setText(txt_time_type + "还剩");

            if (data.getDiscount().equals("1")) {
                //结束时间-开始时间
                longDistanceStart = Long.parseLong(distanceStart);
                longDistanceEnd = Long.parseLong(distanceEnd);
                long longDistanceInterval = longDistanceEnd - longDistanceStart;

                long timer = 0;
                if (data.getCurrent_times() == 0) {
                    timer = distance_int;
                    data.setCurrent_times(System.currentTimeMillis());
                } else {
                    timer = distance_int - (System.currentTimeMillis() - data.getCurrent_times());
                }
                handlerTime(timer, distance_tag, longDistanceInterval);
            }
        } else {
            lin_XS_bottom.setVisibility(View.GONE);//底部限时抢购
            lin_bottom.setVisibility(View.VISIBLE);//购物车
        }

    }


    private void handlerTime(long timeTmp, final int dicountTag, final long longDistanceEnd) {
        CountDownTimer countDownTimer = countDownCounters.get(tv_downcount_hour.hashCode());
        if (countDownTimer != null) {
            //将复用的倒计时清除
            countDownTimer.cancel();
        }
        // 数据
        long timer = timeTmp * 1000;//

        //long timer = data.expirationTime;
        //timer = timer - System.currentTimeMillis();
        //expirationTime 与系统时间做比较，timer 小于零，则此时倒计时已经结束。
        if (timer > 0) {
            final CountDownTimer finalCountDownTimer = countDownTimer;
            countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] times = SetTime(millisUntilFinished);
                    mDay = Integer.parseInt(times[0]);

                    tv_XS_type.setText("距开始 " + (times[0]) + "天" + (times[1]) + "时" + (times[2]) + "分" + (times[3]) + "秒");
                    lin_downcount.setVisibility(View.VISIBLE);

                    tv_downcount_day.setText(fillZero(times[0]));
                    tv_downcount_hour.setText(fillZero(times[1]));
                    tv_downcount_minute.setText(fillZero(times[2]));
                    tv_downcount_second.setText(fillZero(times[3]));
                }

                public void onFinish(String redpackage_id) {
                    //结束了该轮倒计时

                    //1表示活动已开始，0表示活动已结束
                    if (dicountTag == 1) {
                        lin_bottom.setVisibility(View.VISIBLE);//购物车
                        lin_XS_bottom.setVisibility(View.GONE);//底部限时抢购
                        txt_time_type = "距结束";
                        tv_time_tag.setText("距结束还剩");

                        handlerEndTime(longDistanceEnd);

                    } else {
                        lin_bottom.setVisibility(View.GONE);//购物车
                        lin_XS_bottom.setVisibility(View.VISIBLE);//底部限时抢购
                        txt_time_type = "已结束";
                        tv_XS_type.setText("活动已结束");

                        tv_downcount_day.setText("00");
                        tv_downcount_hour.setText("00");
                        tv_downcount_minute.setText("00");
                        tv_downcount_second.setText("00");
                    }

//                    holder.statusTv.setText(data.name + ":结束");
                }
            }.start();
            //将此 countDownTimer 放入list.
            countDownCounters.put(tv_downcount_hour.hashCode(), countDownTimer);
        } else {
            //结束
        }
    }

    private void handlerEndTime(long timeTmp) {
        CountDownTimer countDownTimer = countDownCounters.get(tv_downcount_hour.hashCode());
        if (countDownTimer != null) {
            //将复用的倒计时清除
            countDownTimer.cancel();
        }
        long timer = timeTmp * 1000;
        if (timer > 0) {
            final CountDownTimer finalCountDownTimer = countDownTimer;
            countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {

                    lin_downcount.setVisibility(View.VISIBLE);
                    String[] times = SetTime(millisUntilFinished);
                    tv_downcount_day.setText(fillZero(times[0]));
                    tv_downcount_hour.setText(fillZero(times[1]));
                    tv_downcount_minute.setText(fillZero(times[2]));
                    tv_downcount_second.setText(fillZero(times[3]));
                }

                public void onFinish(String redpackage_id) {
                    //结束了该轮倒计时
                    lin_bottom.setVisibility(View.GONE);//购物车
                    lin_XS_bottom.setVisibility(View.VISIBLE);//底部限时抢购

                    txt_time_type = "已结束";
                    tv_time_tag.setText("已结束");
                    tv_XS_type.setText("活动已结束");

                    tv_downcount_day.setText("00");
                    tv_downcount_hour.setText("00");
                    tv_downcount_minute.setText("00");
                    tv_downcount_second.setText("00");
                }
            }.start();
            //将此 countDownTimer 放入list.
            countDownCounters.put(tv_downcount_hour.hashCode(), countDownTimer);
        } else {
            //结束
            lin_bottom.setVisibility(View.GONE);//购物车
            lin_XS_bottom.setVisibility(View.VISIBLE);//底部限时抢购

            txt_time_type = "已结束";
            tv_time_tag.setText("已结束");
            tv_XS_type.setText("活动已结束");

            tv_downcount_day.setText("00");
            tv_downcount_hour.setText("00");
            tv_downcount_minute.setText("00");
            tv_downcount_second.setText("00");
        }
    }


    private String[] SetTime(long time) {
        mDay = time / (1000 * 60 * 60 * 24);
        mHour = (time - mDay * (1000 * 60 * 60 * 24))
                / (1000 * 60 * 60);
        mMin = (time - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60))
                / (1000 * 60);
        mSecond = (time - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60) - mMin * (1000 * 60))
                / 1000;
        String[] str = new String[4];
        str[0] = mDay + "";
        str[1] = mHour + "";
        str[2] = mMin + "";
        str[3] = mSecond + "";
        return str;
    }

    private String fillZero(String time) {
        if (time.length() == 1)
            return "0" + time;
        else
            return time;
    }

    private void getpingjia() {
        System.out.println("&&&&&&&&&&" + detailBean.getScore());
        if (detailBean.getScore() != null) {
            list_pingjia.setVisibility(View.VISIBLE);
            System.out.println("$$$$$$$$$$$" + detailBean.getScore().size());
            ShopDetailListAdapter listAdapter = new ShopDetailListAdapter(ShopDetailActivity.this, detailBean.getScore());
            list_pingjia.setAdapter(listAdapter);
            istag = 1;
            System.out.println("===========");
        } else {
            System.out.println("%%%%%%%%%%%");
            istag = 2;
            rel_pingjia.setVisibility(View.GONE);
            list_pingjia.setVisibility(View.GONE);
            view_pingjia.setVisibility(View.GONE);
        }
        refreshView();
    }

    // 评价列表
    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected void getAddGoodsTagView() {
        if (detailBean.getGoods_tag() != null) {
            lin_goodsTag.setVisibility(View.VISIBLE);
            view_tag.setVisibility(View.VISIBLE);
            for (int i = 0; i < detailBean.getGoods_tag().size(); i++) {

                if (!StringUtils.isEmpty(detailBean.getGoods_tag().get(i).getC_img()) && !StringUtils.isEmpty(detailBean.getGoods_tag().get(i).getC_name())) {
                    View view = LinearLayout.inflate(ShopDetailActivity.this, R.layout.shop_detail_goodstag_item, null);
                    ImageView img1 = (ImageView) view.findViewById(R.id.img_1);
                    TextView tag1 = (TextView) view.findViewById(R.id.txt_tag1);
                    Glide.with(ShopDetailActivity.this).load(detailBean.getGoods_tag().get(i).getC_img()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.icon_px).error(R.drawable.icon_px).into(img1);
                    //bitmapUtils.display(img1, detailBean.getGoods_tag().get(i).getC_img());
                    tag1.setText(detailBean.getGoods_tag().get(i).getC_name());
                    lin_goodsTag.addView(view);
                }

            }
            detailBean.getGoods_tag().clear();
        } else {
            lin_goodsTag.setVisibility(View.GONE);
            view_tag.setVisibility(View.GONE);
        }
    }

    protected void getAddview() {
        if (detailBean.getImgs() != null) {
            for (int i = 0; i < detailBean.getImgs().size(); i++) {
                View view = LinearLayout.inflate(ShopDetailActivity.this, R.layout.shop_detail_imgdesc_item, null);
                ImageView img = (ImageView) view.findViewById(R.id.img);
                Glide.with(ShopDetailActivity.this).load(MyCookieStore.URL + detailBean.getImgs().get(i).getImg()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_goods_bg).error(R.drawable.ic_goods_bg).into(img);
                lin_img.addView(view);
            }
            detailBean.getImgs().clear();
        }
    }

    int isClickOne = 0;

    @Override
    public void onClick(View arg0) {
        Intent intent;
        switch (arg0.getId()) {

            case R.id.lin_goumai:///立即购买
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(ShopDetailActivity.this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                    preferencesLogin = ShopDetailActivity.this.getSharedPreferences("login", 0);
                    Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();

                } else if (login_type.equals("1")) {
                    if (detailBean.getExist_hours().equals("2")) {// 判断是否打烊
                        XToast.makeText(this, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                    } else {

                        getTag("1");
                    }

                } else {
                    XToast.makeText(this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_left:// 返回
                finish();
                break;
            case R.id.rel_gouwuche:// 购物车列表
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(ShopDetailActivity.this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                    preferencesLogin = ShopDetailActivity.this.getSharedPreferences("login", 0);
                    Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();
                } else if (login_type.equals("1")) {
                    intent = new Intent(this, ShopCartActivityTwo.class);
                    startActivity(intent);
                } else {
                    XToast.makeText(this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_add_ss:// 加入购物车
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(ShopDetailActivity.this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                    preferencesLogin = ShopDetailActivity.this.getSharedPreferences("login", 0);
                    Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();
                }else if (login_type.equals("1")) {
                    if (detailBean.getExist_hours().equals("2")) {// 判断是否打烊
                        XToast.makeText(this, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                    } else {
                        getTag("2");
                    }

                } else {
                    XToast.makeText(this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rel_pingjia:// 查看全部评价、
                intent = new Intent(this, SeeAllPingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", shop_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_top:
                sv_bodyContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        // scrollView.fullScroll(ScrollView.FOCUS_DOWN);滚动到底部
                        // scrollView.fullScroll(ScrollView.FOCUS_UP);滚动到顶部
                        //
                        // 需要注意的是，该方法不能直接被调用
                        // 因为Android很多函数都是基于消息队列来同步，所以需要一部操作，
                        // addView完之后，不等于马上就会显示，而是在队列中等待处理，虽然很快，但是如果立即调用fullScroll，
                        // view可能还没有显示出来，所以会失败
                        // 应该通过handler在新线程中更新
                        sv_bodyContainer.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                iv_top.setVisibility(View.GONE);
                break;
            case R.id.rel_cancel://已选 点击事件

                if (login_type.equals("1")) {
                /*AddShopDialog dialog = new AddShopDialog(this, shop_id, detailBean.getTitle(),
                        detailBean.getTitle_img(), tag_name, txt_shop_num,beanstag);
				dialog.show();*/
                    if (!TextUtils.isEmpty(txt_time_type)) {
                        if (txt_time_type.equals("距开始")) {
                            XToast.makeText(this, "活动即将开始", XToast.LENGTH_SHORT).show();

                        } else if (txt_time_type.equals("已结束")) {
                            XToast.makeText(this, "活动已结束", XToast.LENGTH_SHORT).show();

                        } else {
                            if (detailBean.getExist_hours().equals("2")) {// 判断是否打烊
                                XToast.makeText(this, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                            } else {
                                getTag("2");
                            }
                        }
                    } else {
                        if (detailBean.getExist_hours().equals("2")) {// 判断是否打烊
                            XToast.makeText(this, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                        } else {

                            getTag("2");
                        }
                    }


                } else if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(ShopDetailActivity.this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                    preferencesLogin = ShopDetailActivity.this.getSharedPreferences("login", 0);
                    Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();
                } else {
                    XToast.makeText(this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ly_share://分享
                if (detailBean == null) {
                    return;
                }
                if (NullUtil.isStringEmpty(detailBean.getId())) {
                    return;
                }
                share_title = detailBean.getTitle() + "";
                share_desc = detailBean.getDescription() + "";
                share_icon = MyCookieStore.URL + detailBean.getTitle_img();
                share_url = ApiHttpClient.API_URL_SHARE+"home/shop/goods_details/id/" + detailBean.getId();
                HashMap<String, String> params = new HashMap<>();
                params.put("type", "goods_details");
                params.put("id", detailBean.getId());
                showDialog(smallDialog);
                LinkedMeUtils.getInstance().getLinkedUrl(this, share_url, share_title, params, new LinkedMeUtils.OnGetLinkedmeUrlListener() {
                    @Override
                    public void onGetUrl(String url, LMError error) {
                        hideDialog(smallDialog);
                        if (error == null) {
                            String share_url_new = share_url + "?linkedme=" + url;
                            showSharePop(share_title, share_desc, share_icon, share_url_new);
                        } else {
                            //可以看报错
                           // XToast.makeText(ShopDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            String share_url_new = share_url + "?linkedme=" + "";
                            showSharePop(share_title, share_desc, share_icon, share_url_new);
                        }
                    }
                });
                break;
            default:
                break;
        }

    }

    /**
     * 显示分享弹窗
     *
     * @param share_title
     * @param share_desc
     * @param share_icon
     * @param share_url_new
     */
    private void showSharePop(String share_title, String share_desc, String share_icon, String share_url_new) {
        PopupWindowShare popup = new PopupWindowShare(this, share_title, share_desc, share_icon, share_url_new, AppConstant.SHARE_COMMON);
        popup.showBottom(ly_share);
    }

    ShopProtocol protocoltag = new ShopProtocol();
    List<ShopDetailBean> beanstag = new ArrayList<ShopDetailBean>();
    AddShopDialog dialog;

    private void getTag(final String isbooltag) {//获取商品标签
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", shop_id);
        HttpHelper hh = new HttpHelper(info.goods_tags, params, ShopDetailActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                beanstag = protocoltag.getTag(json);
                //判断是否快速点击
                if (ToolUtils.isFastClick()) {
                    AddShopDialog dialog = new AddShopDialog(ShopDetailActivity.this, shop_id
                            , txt_shop_num, beanstag, detailBean, isbooltag, new AddShopDialog.PriorityListener() {
                        @Override
                        public void refreshPriorityUI(String string) {
                            lin_yixuanze.setVisibility(View.VISIBLE);
                            tag_guige.setVisibility(View.GONE);
                            tag_name.setVisibility(View.VISIBLE);
                            tag_name.setText(string);
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private void getCartNum() {// 购物车商品数量
       /* Url_info info=new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper( info.cart_num, params, this) {

            @Override
            protected void setData(String json) {
                cartnum = protocol.getCartNum(json);
                if (cartnum.getCart_num().equals("0")) {
                    txt_shop_num.setVisibility(View.GONE);
                } else {
                    txt_shop_num.setVisibility(View.VISIBLE);
                }
            }
        };*/
        Url_info info = new Url_info(this);
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
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        });

    }

    // /=============商品图片轮播图
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(BannerBean info, int position, View imageView) {// 点击图片事件
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    /**
     * ScrollView 的顶部，底部判断： http://blog.csdn.net/qq_21376985
     * <p/>
     * 其中getChildAt表示得到ScrollView的child View， 因为ScrollView只允许一个child
     * view，所以contentView.getMeasuredHeight()表示得到子View的高度,
     * getScrollY()表示得到y轴的滚动距离，getHeight()为scrollView的高度。
     * 当getScrollY()达到最大时加上scrollView的高度就的就等于它内容的高度了啊~
     *
     * @param
     */
    private void doOnBorderListener() {
        // 底部判断
        if (contentView != null && contentView.getMeasuredHeight() <= sv_bodyContainer.getScrollY() + sv_bodyContainer.getHeight()) {
            iv_top.setVisibility(View.VISIBLE);

        }
        // 顶部判断
        else if (sv_bodyContainer.getScrollY() == 0) {
            iv_top.setVisibility(View.GONE);

        } else if (sv_bodyContainer.getScrollY() > 300) {
            iv_top.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 销毁倒计时
     */
    private void cannelAllTimers() {
        if (countDownCounters == null) {
            return;
        }
        for (int i = 0, length = countDownCounters.size(); i < length; i++) {
            CountDownTimer cdt = countDownCounters.get(countDownCounters.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.cannelAllTimers();
    }
}
