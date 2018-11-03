package com.huacheng.huiservers.fragment.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.ServiceLifeOrderAdapter2;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.center.bean.PropertyRepair;
import com.huacheng.huiservers.fragment.BaseFragmentOld;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ServiceFragmentPage extends BaseFragmentOld {

    private ServiceLifeOrderAdapter2 adapter;
    private String statusProtocol = "0";
    private String isFirst, typeLink, tid;

    int totalPage = 0;// 总页数
    private int page = 1;// 当前页

    ListBean mTab;
    List<PropertyRepair> participates;
    private CenterProtocol protocol = new CenterProtocol();
    private List<PropertyRepair> participateAll = new ArrayList<PropertyRepair>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout rel_no_data;
    private boolean isInit=false;

    public ServiceFragmentPage() {
        super();
    }
    private String type="0";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = this.getArguments();
        this.mTab = (ListBean) arguments.get("tab");
        typeLink = MyCookieStore.SERVERADDRESS + mTab.getLink() ;
        tid = mTab.getId();
        if (tid.equals("")) {
            statusProtocol = "0";
        } else {
            statusProtocol = "1";
        }
        type=arguments.getString("type");
    }

//    public ServiceFragmentPage(ListBean tab, String activityID, String str_md5) {
//        super();
//        this.mTab = tab;
//        typeLink = MyCookieStore.SERVERADDRESS + mTab.getLink() ;
//        tid = mTab.getId();
//        if (tid.equals("")) {
//            statusProtocol = "0";
//        } else {
//            statusProtocol = "1";
//        }
//
//    }

    RecyclerViewNoBugLinearLayoutManager recyclerLayoutManager;

    @Override
    protected void initView() {
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        recyclerLayoutManager = new RecyclerViewNoBugLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);
        isFirst = "1";
        // 获取数据
//        if (type.equals("0")){
////            showDialog(smallDialog);
////            getdataRepair(typeLink, "1");
////            isInit=true;
//        }
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(refreshListener);


        mRecyclerView.setOnScrollListener(mOnScrollListener);
        if (type.equals("0")){
            isInit=true;
            swipeRefreshLayout.setRefreshing(true);
            refreshListener.onRefresh();
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
            if (adapter!=null&&newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isShowFooter() && !swipeRefreshLayout.isRefreshing())//
            {
                // 上拉加载更多
                loadMore();
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
        adapter.setLoadState(adapter.LOADING);
        if (page < totalPage) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            getdataRepair(typeLink, page + "");
                            adapter.setLoadState(adapter.LOADING_COMPLETE);
                        }
                    });
                }
            }, 500);

        } else {
            // 显示加载到底的提示
            adapter.setLoadState(adapter.LOADING_END);
        }
    }

    private void getdataRepair(String mLink, String total_Page) {
        new HttpHelper(mLink + "/p/" + total_Page, new RequestParams(), getActivity()) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                if (statusProtocol.equals("0")) {
                    participates = protocol.propertyRepairs(json);
                } else {
                    participates = protocol.getJiaoyu(json);
                }
                if (participates != null) {
                    if (participates.size() > 0) {
                        rel_no_data.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            participateAll.clear();
                            adapter = new ServiceLifeOrderAdapter2(participateAll, statusProtocol, getActivity());
                            mRecyclerView.setAdapter(adapter);
                        }

                        participateAll.addAll(participates);
                        totalPage = Integer.valueOf(participateAll.get(0).getTotal_Pages());// 设置总页数
                        adapter.notifyDataSetChanged();

                        if (isFirst.equals("1")) {
                            isFirst = "0";
                        }
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        rel_no_data.setVisibility(View.VISIBLE);
                        if (isFirst.equals("1")) {
                            isFirst = "0";
                        }
                    }
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_servicepage_list;
    }
SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 刷新数据
            page = 1;
            getdataRepair(typeLink, "1");

            // 延时0.5s关闭下拉刷新
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 500);
        }
    };
    /**
     * 选中刷新
     */
    public void selectInit() {
        if (isInit){
            return;
        }
        isInit=true;
        swipeRefreshLayout.setRefreshing(true);
        refreshListener.onRefresh();
    }
}
