package com.huacheng.huiservers.fragment.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.BaseFragmentOld;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.huiservers.view.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LW on 2017/4/21.
 */
public class ShopListFragment extends BaseFragmentOld {

    @BindView(R.id.shadow_backtop)
    ShadowLayout shadowBacktop;
    @BindView(R.id.shadow_showpager)
    ShadowLayout shadowShowpager;

    Unbinder unbinder;
    @BindView(R.id.tv_page_num)
    TextView tvPageNum;
    @BindView(R.id.tv_totalpage_num)
    TextView tvTotalpageNum;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout rel_no_data;
    private ShopListFragmentAdapter fragmentShopListAdapter;

    private List<ShopIndexBean> beans;
    private ShopIndexBean detail;
    private List<String> dataSource;
    private String mPid;
    private int totalPage = 0;// 总页数
    private int page = 1;// 当前页
    SharePrefrenceUtil sharePrefrenceUtil;
    List<ShopIndexBean> proLists = new ArrayList<>();
    List<ShopIndexBean> proListAll = new ArrayList<>();
    RecyclerViewNoBugLinearLayoutManager recyclerLayoutManager;
    TextView mtvShopCar;
    private String type = "0";
    private boolean isLoad = false;

    public ShopListFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ShopListFragment(TextView tvShopCar) {
        super();
        this.mtvShopCar = tvShopCar;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        mPid = arguments.getString("id");
        type = arguments.getString("type");
    }

    private void getData(String mPid, int total_Page) {

        Url_info urlInfo = new Url_info(getActivity());
        if (!StringUtils.isEmpty(mPid)) {
            String url = urlInfo.pro_list + "/id/" + mPid + "/c_id/" + sharePrefrenceUtil.getXiaoQuId() + "/p/" + total_Page;
            new HttpHelper(url, getActivity()) {
                @Override
                protected void setData(String json) {
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
                        mRecyclerView.setVisibility(View.GONE);
                        rel_no_data.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (fragmentShopListAdapter!=null){
                        fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING_COMPLETE);
                    }
                }
            };
        } else {
            rel_no_data.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


    }

    public boolean isInit = false;

    @Override
    protected void initView() {
        sharePrefrenceUtil = new SharePrefrenceUtil(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);

        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        recyclerLayoutManager = new RecyclerViewNoBugLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);
        page = 1;

        fragmentShopListAdapter = new ShopListFragmentAdapter(proListAll, mtvShopCar, page, getActivity());
        mRecyclerView.setAdapter(fragmentShopListAdapter);
        if (type.equals("0")) {//第一页刷新
//            getData(mPid, page);
//            isInit = true;
            selected_init();
        }

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(refreshlistener);
        // 设置加载更多监听
        mRecyclerView.setOnScrollListener(mOnScrollListener);

    }

    /**
     * 选中后自动刷新
     */
    public void selected_init() {
        if (isInit == false) {
            //初始化
            isInit = true;
            swipeRefreshLayout.setRefreshing(true);
            refreshlistener.onRefresh();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener refreshlistener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            page = 1;
            getData(mPid, page);
//            // 延时1s关闭下拉刷新
////            swipeRefreshLayout.postDelayed(new Runnable() {
////                @Override
////                public void run() {
//////                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//////                        swipeRefreshLayout.setRefreshing(false);
//////                    }
////                }
////            }, 500);

        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.shop_list_fragment;
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
            int firstVisibleItemPosition = recyclerLayoutManager.findFirstVisibleItemPosition();
            //SCROLL_STATE_DRAGGING //正在被外部拖拽,一般为用户正在用手指滚动
            //SCROLL_STATE_SETTLING //自动滚动开始

            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == fragmentShopListAdapter.getItemCount()
                    && fragmentShopListAdapter.isShowFooter() && !swipeRefreshLayout.isRefreshing())//
            {
                // 上拉加载更多
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
            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//
                if (firstVisibleItemPosition == 0) {
                    shadowBacktop.setVisibility(View.GONE);
//                    shadowShowpager.setVisibility(View.GONE);
                } else {
                    shadowBacktop.setVisibility(View.VISIBLE);
//                    shadowShowpager.setVisibility(View.VISIBLE);
                }
//                shadowShowpager.setVisibility(View.VISIBLE);
//                tvPageNum.setText(page + "");
//                tvTotalpageNum.setText(totalPage + "");

            }

        }

        // 滑动位置
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 给lastVisibleItem赋值
            // findLastVisibleItemPosition()是返回最后一个item的位置
            lastVisibleItem = recyclerLayoutManager.findLastVisibleItemPosition();

        }
    };

    private void loadMore() {
//        fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING);
//        if (page < totalPage) {
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            page++;
//                            getData(mPid, page);
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

        if (page<=totalPage){
            fragmentShopListAdapter.setLoadState(fragmentShopListAdapter.LOADING);

            getData(mPid, page);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.shadow_backtop, R.id.shadow_showpager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shadow_backtop:
                mRecyclerView.smoothScrollToPosition(0);
                shadowBacktop.setVisibility(View.GONE);
//                shadowShowpager.setVisibility(View.GONE);
                break;
            case R.id.shadow_showpager:
                break;
        }
    }
}
