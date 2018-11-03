package com.huacheng.huiservers.oldhome;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.oldhome.adapter.OldListQRecycleAdapter;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.huacheng.huiservers.R.id.recyclerview;
import static com.huacheng.huiservers.R.id.rel_no_data;

/**
 * 类：
 * 时间：2018/6/1 16:29
 * 功能描述:Huiservers
 */
public class OldHomeListActivity extends BaseUI {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(recyclerview)
    RecyclerView mRecyclerview;
    @BindView(rel_no_data)
    RelativeLayout mRelNoData;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    OldListQRecycleAdapter mQRecycleAdapter;

    String Murl, c_id, id;
    int totalPage = 0;// 总页数
    private int page = 1;// 当前页
    ShopProtocol protocol = new ShopProtocol();
    List<BannerBean> bean = new ArrayList<BannerBean>();
    List<BannerBean> beans2 = new ArrayList<BannerBean>();
    RecyclerViewNoBugLinearLayoutManager manager;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.old_list_item);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        mTitleName.setText("居家养老");
        Murl = this.getIntent().getExtras().getString("url");
        c_id = this.getIntent().getExtras().getString("c_id");
        id = this.getIntent().getExtras().getString("id");
        // mRecyclerview.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        manager = new RecyclerViewNoBugLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(manager);
        // 设置刷新控件颜色
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mLinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showDialog(smallDialog);
        getSocialList(page);

        // 设置下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                page = 1;
                getSocialList(page);
                // 延时1s关闭下拉刷新
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 500);

            }
        });
        mRecyclerview.addOnScrollListener(mOnScrollListener);
        // 设置加载更多监听
        mRecyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getSocialList(page);
                                    mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 600);


                } else {
                    // 显示加载到底的提示
                    mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING_END);
                }

            }
        });

    }

    private void getSocialList(int total_Page) {
        //Url_info info = new Url_info(this);
        String url = MyCookieStore.SERVERADDRESS + Murl + "/community_id/" + c_id + "/c_id/" + id + "/p/" + total_Page;
        new HttpHelper(url, OldHomeListActivity.this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getSocialList(json);
                if (bean.size() > 0) {
                    mRelNoData.setVisibility(View.GONE);
                    mRecyclerview.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans2.clear();
                        mQRecycleAdapter = new OldListQRecycleAdapter(OldHomeListActivity.this, beans2);
                        mRecyclerview.setAdapter(mQRecycleAdapter);
                    }

                    beans2.addAll(bean);
                    totalPage = beans2.get(0).getTotal_Pages();
                    mQRecycleAdapter.notifyDataSetChanged();

                } else {
                    mRecyclerview.setVisibility(View.GONE);
                    mRelNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");

            }
        };
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
             */
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mQRecycleAdapter.getItemCount()
                    && mQRecycleAdapter.isShowFooter())//
            {

                // 上拉加载更多
                initAData();
//                LogUtils.d(TAG, "loading more data");
//                mNewsPresenter.loadNews(mType, pageIndex + Urls.PAZE_SIZE);
            }
        }

        // 滑动位置
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 给lastVisibleItem赋值
            // findLastVisibleItemPosition()是返回最后一个item的位置
            lastVisibleItem = manager.findLastVisibleItemPosition();
        }
    };

    private void initAData() {
        mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING);
        if (page < totalPage) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            getSocialList(page);
                            mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING_COMPLETE);

                        }
                    });
                }
            }, 500);

        } else {
            // 显示加载到底的提示
            mQRecycleAdapter.setLoadState(mQRecycleAdapter.LOADING_END);
        }
    }


}

