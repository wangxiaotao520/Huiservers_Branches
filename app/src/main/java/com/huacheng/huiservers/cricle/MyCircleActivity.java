package com.huacheng.huiservers.cricle;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.bean.CircleDetail4Bean;
import com.huacheng.huiservers.fragment.adapter.Circle5Adapter;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CommonProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MyCircleActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.rel_no_data)
    RelativeLayout rel_no_data;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lin_top)
    LinearLayout linTop;

    private SharePrefrenceUtil sharePrefrenceUtil;

    List<BannerBean> bean = new ArrayList<BannerBean>();
    List<BannerBean> beans2 = new ArrayList<BannerBean>();
    ShopProtocol protocol = new ShopProtocol();
    private SharedPreferences preferencesLogin;

    int totalPage = 0;// 总页数
    private int page = 1;// 当前页
    private String login_type;
    private Circle5Adapter mcircle5Adapter;
    private Dialog waitDialog;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.circle5_wo);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
//        MyCookieStore.MyCircle_notify = 0;
        MyCookieStore.Circle_notify = 0;
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        linTop.setFocusable(true);
        linTop.setFocusableInTouchMode(true);
        linTop.requestFocus();

        titleName.setText("我的邻里");
        sharePrefrenceUtil = new SharePrefrenceUtil(this);
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
   //     waitDialog = WaitDIalog.createLoadingDialog(this, "正在加载...");
        showDialog(smallDialog);
        getMySocialList("1");
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linTop.setFocusable(true);
                linTop.setFocusableInTouchMode(true);
                linTop.requestFocus();
                // 刷新数据
                getMySocialList("1");
                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 500);

            }
        });

        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                mcircle5Adapter.setLoadState(mcircle5Adapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getMySocialList(page + "");
                                    mcircle5Adapter.setLoadState(mcircle5Adapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 500);

                } else {
                    // 显示加载到底的提示
                    mcircle5Adapter.setLoadState(mcircle5Adapter.LOADING_END);
                }

            }
        });

        linLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyCookieStore.MyCircle_notify = 0;
                finish();
            }
        });
    }

    String isFirst = "";
    private Dialog waitDialog2;


    // 我的邻里
    private void getMySocialList(String total_Pages) {
        if (isFirst.equals("2")) {
         //   waitDialog2 = WaitDIalog.createLoadingDialog(this, "正在加载...");
           showDialog(smallDialog);
        }
        Url_info info = new Url_info(this);
        String url = info.get_user_Social + "community_id/" + sharePrefrenceUtil.getXiaoQuId();
        new HttpHelper(url, new RequestParams(), this, waitDialog) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getWoSocial(json);
                if (bean.size() > 0) {
                    rel_no_data.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans2.clear();
                        mcircle5Adapter = new Circle5Adapter(beans2, MyCircleActivity.this);
                        mRecyclerView.setAdapter(mcircle5Adapter);
                    }
                    beans2.addAll(bean);
                    totalPage = beans2.get(0).getTotal_Pages();

                    mcircle5Adapter.setmOnItemDeleteListener(new Circle5Adapter.onItemDeleteListener() {
                        @Override
                        public void onDeleteClick(final int i) {
                            new CommomDialog(MyCircleActivity.this, R.style.my_dialog_DimEnabled, "删除此信息吗？", new CommomDialog.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm) {
                                        SocialDel(beans2.get(i).getId());
                                        dialog.dismiss();
                                    }

                                }
                            }).show();//.setTitle("提示")
                        }
                    });
                    mcircle5Adapter.setItemClickListener(new Circle5Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            Intent intent = new Intent();
                            intent.setClass(MyCircleActivity.this, Circle2DetailsActivity.class);
                            intent.putExtra("id", beans2.get(position).getId());
                            startActivity(intent);
                        }
                    });

                    mcircle5Adapter.notifyDataSetChanged();
                    if (isFirst.equals("2")) {
                        isFirst = "0";
                       // WaitDIalog.closeDialog(waitDialog2);
                    } else {
                    //    WaitDIalog.closeDialog(waitDialog);
                    }

                } else {
                    rel_no_data.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                if (isFirst.equals("2")) {
                    isFirst = "0";
                 //   WaitDIalog.closeDialog(waitDialog2);
                } else {
                 //   WaitDIalog.closeDialog(waitDialog);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                if (isFirst.equals("2")) {
                    isFirst = "0";
                  //  WaitDIalog.closeDialog(waitDialog2);
                } else {
                 //   WaitDIalog.closeDialog(waitDialog);
                }
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    /**
     * 删除邻里
     *
     * @param socialID
     */
    private void SocialDel(final String socialID) {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("social_id", socialID);

        new HttpHelper(urlInfo.SocialDel, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String result = new CommonProtocol().getResult(json);
                if (result.equals("1")) {
                    beans2.clear();
                    isFirst = "2";
                    getMySocialList("1");
//                    CircleDetail4Bean circleDetail4Bean = new CircleDetail4Bean();
//                    circleDetail4Bean.setType(1);
//                    circleDetail4Bean.setId(socialID);
//                    EventBus.getDefault().post(circleDetail4Bean);
                } else {
                    XToast.makeText(MyCircleActivity.this, "删除失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 11) {
                page = 1;
                beans2.clear();
                getMySocialList("1");
                mcircle5Adapter.notifyDataSetChanged();
            }
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    /**
     * 添加和删除评论的Eventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateItem(CircleDetail4Bean mCirclebean) {
        try {
            if (mCirclebean != null) {
                int type = mCirclebean.getType();
                if (type == 0) {
                    //添加评论
                    for (int i = 0; i < beans2.size(); i++) {
                        BannerBean bannerBean = beans2.get(i);
                        if (bannerBean.getId().equals(mCirclebean.getId())) {
                            int i1 = Integer.parseInt(bannerBean.getReply_num());
                            bannerBean.setReply_num((i1 + 1) + "");
                        }
                    }
                    if (mcircle5Adapter != null) {
                        mcircle5Adapter.notifyDataSetChanged();
                    }
                } else if (type == 1) {
                    //删除评论
                    for (int i = 0; i < beans2.size(); i++) {
                        BannerBean bannerBean = beans2.get(i);
                        if (bannerBean.getId().equals(mCirclebean.getId())) {
                            int i1 = Integer.parseInt(bannerBean.getReply_num());
                            bannerBean.setReply_num((i1 - 1) + "");
                        }
                    }
                    if (mcircle5Adapter != null) {
                        mcircle5Adapter.notifyDataSetChanged();
                    }
                } else if (type == 2) {//阅读数
                    for (int i = 0; i < beans2.size(); i++) {
                        BannerBean bannerBean = beans2.get(i);
                        if (bannerBean.getId().equals(mCirclebean.getId())) {
                            int i1 = Integer.parseInt(bannerBean.getClick());
                            bannerBean.setClick((i1 + 1) + "");
                        }
                    }
                    if (mcircle5Adapter != null) {
                        mcircle5Adapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
