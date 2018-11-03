package com.huacheng.huiservers.facepay;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.facepay.adapter.FacepayHistoryAdapter;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
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

public class FacepayPaymentHistoryActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;

    private RecyclerView mRecyclerView;
    private RelativeLayout rel_no_data;

    List<FacePayBean> bean = new ArrayList<FacePayBean>();
    List<FacePayBean> beans2 = new ArrayList<FacePayBean>();
    FacePayProtocol protocol = new FacePayProtocol();
    private FacepayHistoryAdapter mFacepayHistoryAdapter;

    int totalPage = 0;// 总页数
    private int page = 1;// 当前页

    private SwipeRefreshLayout swipe_refresh_layout1;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.facepay_payment_history);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        titleName.setText("当面付历史记录");

        swipe_refresh_layout1 = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);

        // 设置刷新控件颜色
        swipe_refresh_layout1.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 获取数据
        showDialog(smallDialog);
        getFacepayHistoryList("1");
        // 设置下拉刷新
        swipe_refresh_layout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                page = 1;
                getFacepayHistoryList("1");

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
                mFacepayHistoryAdapter.setLoadState(mFacepayHistoryAdapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getFacepayHistoryList(page + "");
                                    mFacepayHistoryAdapter.setLoadState(mFacepayHistoryAdapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 500);

                } else {
                    // 显示加载到底的提示
                    mFacepayHistoryAdapter.setLoadState(mFacepayHistoryAdapter.LOADING_END);
                }

            }
        });

    }

    private void getFacepayHistoryList(String total_Page) {
        Url_info info = new Url_info(this);
        String url = info.face_order_list + "p/" + total_Page;
        new HttpHelper(url, new RequestParams(), this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getFacepayHistoryList(json);
                if (bean.size() > 0) {
                    rel_no_data.setVisibility(View.GONE);
                    swipe_refresh_layout1.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans2.clear();
                        mFacepayHistoryAdapter = new FacepayHistoryAdapter(beans2, FacepayPaymentHistoryActivity.this);
                        mRecyclerView.setAdapter(mFacepayHistoryAdapter);

                    }
                    beans2.addAll(bean);
                    totalPage = beans2.get(0).getTotal_Pages();
                    mFacepayHistoryAdapter.notifyDataSetChanged();
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
