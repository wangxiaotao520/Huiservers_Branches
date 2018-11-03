package com.huacheng.huiservers.shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.fragment.shop.ShopListFragment;
import com.huacheng.huiservers.fragment.shop.ShopListFragmentAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.LongPagerAdapter;
import com.huacheng.huiservers.shop.bean.CateBean;
import com.huacheng.huiservers.utils.CommonMethod;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.huiservers.view.ShadowLayout;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.http.RequestParams;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/24.
 */

public class ShopListActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.ly_search)
    LinearLayout lySearch;
    @BindView(R.id.lin_gouwuche)
    RelativeLayout linGouwuche;
    @BindView(R.id.txt_shop_num)
    TextView txtShopNum;
    @BindView(R.id.lin_car)
    LinearLayout linCar;
    @BindView(R.id.tablayout_shop)
    TabLayout tablayoutShop;
    @BindView(R.id.lin_shopall)
    LinearLayout linShopall;
    @BindView(R.id.vp_shop)
    ViewPager viewpagerShop;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.lin_all)
    LinearLayout linAll;
    @BindView(R.id.rel_no_data)
    RelativeLayout rel_no_data;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lin_shop_list_nohead)
    LinearLayout linShopListNohead;
    @BindView(R.id.lin_shop_list_hashead)
    LinearLayout linShopListHashead;
    @BindView(R.id.shadow_backtop)
    ShadowLayout shadowBacktop;

    private List<Fragment> list;
    TagFlowLayout mFlowLayout;
    String[] tabs;
    private static int mSelectPosition = 0;
    private static int flag = 0;
    String cateID;
    SharePrefrenceUtil sharedPreferenceUtil;
    ShopProtocol shopProtocol = new ShopProtocol();
    List<CateBean> cates;
    Intent intent = new Intent();
    String login_type;
    SharedPreferences preferencesLogin;
    //    private boolean showWaitDialog = true;
    RecyclerViewNoBugLinearLayoutManager noBugManager;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.shop_list);
 //       SetTransStatus.GetStatus(this);// 系统栏默认为黑色
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        sharedPreferenceUtil = new SharePrefrenceUtil(this);
        cateID = getIntent().getStringExtra("cateID");

        getLinshi();
        if (!login_type.equals("")) {// 登陆之后获取数量
            new CommonMethod(txtShopNum, this).getCartNum();
        }
        getProductCate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyCookieStore.shopcar_notify == 1) {
            new CommonMethod(txtShopNum, this).getCartNum();
            MyCookieStore.shopcar_notify = 0;
        }

    }

    private void getProductCate() {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams p = new RequestParams();
        p.addBodyParameter("c_id", sharedPreferenceUtil.getXiaoQuId());
        p.addBodyParameter("cate_id", cateID);
        new HttpHelper(urlInfo.pro_list_cate, p, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    if (!StringUtils.isEmpty(data)) {
                        cates = new ShopProtocol().getlistCateTwo(json);
                        if (cates != null) {
                            if (cates.size() > 0) {
                                linShopListHashead.setVisibility(View.VISIBLE);
                                linShopListNohead.setVisibility(View.GONE);
                                setTabLayout(cates);

                            }
                        }

                    } else {
                        showDialog(smallDialog);
                        getData();
                    }
                } catch (JSONException e) {
                    hideDialog(smallDialog);
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

    ShopProtocol protocol = new ShopProtocol();


    private void getData() {
        if (!StringUtils.isEmpty(cateID)) {
            linShopListHashead.setVisibility(View.GONE);
            linShopListNohead.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
            noBugManager = new RecyclerViewNoBugLinearLayoutManager(ShopListActivity.this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(noBugManager);
            fragmentShopListAdapter = new ShopListFragmentAdapter(proListAll, txtShopNum, page, ShopListActivity.this);//, dataSource
            mRecyclerView.setAdapter(fragmentShopListAdapter);
            page = 1;
            showDialog(smallDialog);
            getData(cateID, page);

            // 设置下拉刷新
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // 刷新数据
                    page = 1;
                    getData(cateID, page);

//                    // 延时1s关闭下拉刷新
//                    swipeRefreshLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                                swipeRefreshLayout.setRefreshing(false);
//                            }
//                        }
//                    }, 500);

                }
            });
            mRecyclerView.setOnScrollListener(mOnScrollListener);

        }

    }

    public RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        // 滑动状态改变
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /**
             * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
             * SCROLL_STATE_IDLE是当屏幕停止滚动时
             * SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
             * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
             *
             * SCROLL_STATE_DRAGGING //拖动中
             */
            int firstVisibleItemPosition = noBugManager.findFirstVisibleItemPosition();
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == fragmentShopListAdapter.getItemCount()
                    && fragmentShopListAdapter.isShowFooter() && !swipeRefreshLayout.isRefreshing())//
            {
                loadMore();
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 判断是否滚动超过一屏
                if (firstVisibleItemPosition == 0) {
                    shadowBacktop.setVisibility(View.GONE);
//                    shadowShowpager.setVisibility(View.GONE);
                } else {
                    shadowBacktop.setVisibility(View.VISIBLE);
//                    shadowShowpager.setVisibility(View.VISIBLE);
                }
            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//自动滚动开始
                shadowBacktop.setVisibility(View.VISIBLE);
//                shadowShowpager.setVisibility(View.VISIBLE);
//                tvTotalpageNum.setText(totalPage + "");

            }

        }

        // 滑动位置
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 给lastVisibleItem赋值
            // findLastVisibleItemPosition()是返回最后一个item的位置
            lastVisibleItem = noBugManager.findLastVisibleItemPosition();
        }
    };

    private void loadMore() {
        if (page<=totalPage){
            fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING);

            getData(cateID, page);
        }
//        if (page < totalPage) {
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            page++;
//                            getData(cateID, page);
//                            fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_COMPLETE);
//
//                        }
//                    });
//                }
//            }, 600);
//        } else {
//            // 显示加载到底的提示
//            fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_END);
//        }

    }

    private ShopListFragmentAdapter fragmentShopListAdapter;

    private int totalPage = 0;// 总页数
    private int page = 1;// 当前页
    List<ShopIndexBean> proLists = new ArrayList<>();
    List<ShopIndexBean> proListAll = new ArrayList<>();


    private void getData(String mPid, final int total_Page) {

        Url_info urlInfo = new Url_info(this);
        if (!StringUtils.isEmpty(mPid)) {
            String url = urlInfo.pro_list + "id/" + mPid + "/c_id/" + sharedPreferenceUtil.getXiaoQuId() + "/p/" + total_Page;
            new HttpHelper(url, this) {
                @Override
                protected void setData(String json) {
                    hideDialog(smallDialog);
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                    proLists = new ShopProtocol().getProList(json);
                    if (proLists != null) {
                        if (proLists.size() > 0) {
                            rel_no_data.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            if (page == 1) {
                                proListAll.clear();

                            }
                            proListAll.addAll(proLists);
                            totalPage = Integer.valueOf(proListAll.get(0).getTotal_Pages());

                            fragmentShopListAdapter.setItemClickListener(new ShopListFragmentAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                    Intent intent = new Intent(ShopListActivity.this, ShopDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("shop_id", proListAll.get(position).getId());
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });
                            page++;
                            if (page>totalPage){
                                fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_END);
                            }else {
                                fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_COMPLETE);
                            }
                            fragmentShopListAdapter.notifyDataSetChanged();

                        } else {
                            if (page==1){
                                mRecyclerView.setVisibility(View.GONE);
                                rel_no_data.setVisibility(View.VISIBLE);
                            }else {
                                fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_END);
                            }
                        }
                    } else {
                        if (page==1){
                            mRecyclerView.setVisibility(View.GONE);
                            rel_no_data.setVisibility(View.VISIBLE);
                        }else {
                            fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_END);
                        }
                    }
                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (fragmentShopListAdapter!=null){
                        fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_COMPLETE);
                    }
                    hideDialog(smallDialog);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                }
            };
        } else {
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            hideDialog(smallDialog);
            rel_no_data.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 字体加粗
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabSelect.setTextSize(20);
            tabSelect.setText(tab.getText());
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(getResources().getColor(R.color.black_jain_87));
            tabUnSelect.setTextSize(16);
            tabUnSelect.setText(tab.getText());
        }
    }

    public void setTabLayout(List<CateBean> cates) {
        for (int i = 0; i < cates.size(); i++) {
            tablayoutShop.addTab(tablayoutShop.newTab().setText(cates.get(i).getCate_name()));
        }
        for (int i = 0; i < tablayoutShop.getTabCount(); i++) {
            TabLayout.Tab tab = tablayoutShop.getTabAt(i);
            if (tab != null) {
                if (!StringUtils.isEmpty(cates.get(i).getCate_name())) {
                    tab.setCustomView(getTabView(cates.get(i).getCate_name()));
                }
            }
        }

        updateTabTextView(tablayoutShop.getTabAt(tablayoutShop.getSelectedTabPosition()), true);

        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        for (int i = 0; i < cates.size(); i++) {
            ShopListFragment tabFragment = new ShopListFragment(txtShopNum);
            Bundle bundle = new Bundle();
            bundle.putString("id", cates.get(i).getId());
            bundle.putString("type", i + "");
            tabFragment.setArguments(bundle);
            mFragmentList.add(tabFragment);
        }
        currentFragment = mFragmentList.get(0);
        LongPagerAdapter pager = new LongPagerAdapter(cates, txtShopNum, mFragmentList, getSupportFragmentManager());
        viewpagerShop.setAdapter(pager);
        viewpagerShop.setOffscreenPageLimit(cates.size());
        tablayoutShop.setupWithViewPager(viewpagerShop);
        tablayoutShop.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewpagerShop.setCurrentItem(tab.getPosition());
                mSelectPosition = tab.getPosition();

//                updateTabTextView(tab, true);
                currentFragment = mFragmentList.get(tab.getPosition());
                if (currentFragment != null) {
                    currentFragment.selected_init();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                updateTabTextView(tab, false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private ArrayList<ShopListFragment> mFragmentList = new ArrayList<ShopListFragment>();
    private ShopListFragment currentFragment;

    /**
     * 引用tab item
     *
     * @return
     */
    private View getTabView(String cateName) {
        View view = LayoutInflater.from(this).inflate(R.layout.circle5_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(cateName);
        return view;
    }

    private void getLinshi() {
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    @OnClick({R.id.lin_left, R.id.ly_search, R.id.lin_car, R.id.shadow_backtop})
    //, R.id.lin_shopall
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.ly_search:
                intent.setClass(this, SearchShopActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_car:
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                    preferencesLogin = getSharedPreferences("login", 0);
                    SharedPreferences.Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();
                } else if (login_type.equals("1")) {
                    intent = new Intent(this, ShopCartActivityTwo.class);
                    startActivityForResult(intent, 1);
                } else {
                    XToast.makeText(this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.shadow_backtop:
                mRecyclerView.smoothScrollToPosition(0);
                shadowBacktop.setVisibility(View.GONE);
                break;
        }
    }


}
