package com.huacheng.huiservers.center;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.PersonMoneyAdapter;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterMoneyActivity extends BaseUI implements OnClickListener {

    private int page = 1;//当前页
    int totalPage = 0;//总页数
    private int count = 0;
    private TextView title_name;
    private LinearLayout lin_left;
    private CenterProtocol protocol = new CenterProtocol();
    private List<ListBean> beans = new ArrayList<ListBean>();
    private List<ListBean> bean;
    PersonMoneyAdapter adapter;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.person_my_money);
        ButterKnife.bind(this);
    //    SetTransStatus.GetStatus(this);//系统栏默认为黑色
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("消费记录");
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        showDialog(smallDialog);
        getdata();
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                page = 1;
                getdata();
                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 300);

            }
        });

        // 设置加载更多监听
        recyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getdata();
                                    adapter.setLoadState(adapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 300);

                } else {
                    // 显示加载到底的提示
                    adapter.setLoadState(adapter.LOADING_END);
                }

            }
        });
    }

    private void getdata() {
        Url_info info = new Url_info(this);
        String url = info.my_wallet + "p/" + page + "/";
        new HttpHelper(url, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.myMoney(json, "1");//1是个人中心账户
                if (bean.size() != 0) {
                    relNoData.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans.clear();
                        adapter = new PersonMoneyAdapter(CenterMoneyActivity.this, beans);
                        recyclerview.setAdapter(adapter);
                    }
                    beans.addAll(bean);
                    totalPage = Integer.valueOf(beans.get(0).getTotal_Pages());// 设置总页数
                    adapter.notifyDataSetChanged();

                } else {
                    recyclerview.setVisibility(View.GONE);
                    relNoData.setVisibility(View.VISIBLE);
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
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;

            default:
                break;
        }

    }

}
