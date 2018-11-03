package com.huacheng.huiservers.geren;

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
import com.huacheng.huiservers.geren.adapter.CableTelHistoryAdapter;
import com.huacheng.huiservers.geren.bean.WiredBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.FacePayProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/23.
 */

public class CabletelPaymentHistoryActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;

    List<WiredBean> bean = new ArrayList<WiredBean>();
    List<WiredBean> beans = new ArrayList<WiredBean>();
    FacePayProtocol protocol = new FacePayProtocol();
    private RecyclerView mRecyclerView;
    private RelativeLayout rel_no_data;

    int totalPage = 0;// 总页数

    private int page = 1;// 当前页

    private SwipeRefreshLayout swipe_refresh_layout1;

    private CableTelHistoryAdapter mCableTelHistoryAdapter;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.cabletel_payment_history);
        ButterKnife.bind(this);
   //     SetTransStatus.GetStatus(this);
        titleName.setText("有线缴费历史记录");

        swipe_refresh_layout1 = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);

        // 设置刷新控件颜色
        swipe_refresh_layout1.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 获取数据
        showDialog(smallDialog);
        getCableTelHistoryList("1");
        // 设置下拉刷新
        swipe_refresh_layout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                page = 1;
                getCableTelHistoryList("1");

                // 延时1s关闭下拉刷新
                swipe_refresh_layout1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipe_refresh_layout1 != null && swipe_refresh_layout1.isRefreshing()) {
                            swipe_refresh_layout1.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });

        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                mCableTelHistoryAdapter.setLoadState(mCableTelHistoryAdapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getCableTelHistoryList(page + "");
                                    mCableTelHistoryAdapter.setLoadState(mCableTelHistoryAdapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 500);

                } else {
                    // 显示加载到底的提示
                    mCableTelHistoryAdapter.setLoadState(mCableTelHistoryAdapter.LOADING_END);
                }

            }
        });

    }

    private void getCableTelHistoryList(String total_Page) {
        Url_info info = new Url_info(this);
        String url = info.wired_order_list + "p/" + total_Page;
        new HttpHelper(url, new RequestParams(), this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getWiredHistoryList(json);
                if (bean.size() > 0) {
                    rel_no_data.setVisibility(View.GONE);
                    swipe_refresh_layout1.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans.clear();
                        mCableTelHistoryAdapter = new CableTelHistoryAdapter(CabletelPaymentHistoryActivity.this, beans);
                        mRecyclerView.setAdapter(mCableTelHistoryAdapter);

                    }
                    beans.addAll(bean);
                    totalPage = beans.get(0).getTotal_Pages();
                    mCableTelHistoryAdapter.notifyDataSetChanged();
                } else {
                    rel_no_data.setVisibility(View.VISIBLE);
                    swipe_refresh_layout1.setVisibility(View.GONE);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };


    }

    @OnClick(R.id.lin_left)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;

        }
    }

}
