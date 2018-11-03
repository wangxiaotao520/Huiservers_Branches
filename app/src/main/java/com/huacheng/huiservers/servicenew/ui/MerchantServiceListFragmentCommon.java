package com.huacheng.huiservers.servicenew.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelMerchantList;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.servicenew.ui.adapter.MerchantServicexAdapter;
import com.huacheng.huiservers.servicenew.ui.search.ServiceClassifyActivity;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.view.ShadowLayout;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.SharePrefrenceUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/6 17:29
 */
public class MerchantServiceListFragmentCommon extends BaseFragment {

    @BindView(R.id.lin_searchAll_btn)
    LinearLayout linSearchAllBtn;
    @BindView(R.id.ll_choose_cate)
    LinearLayout llChooseCate;
    @BindView(R.id.lin_service_searchbar)
    LinearLayout linServiceSearchbar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;
    Unbinder unbinder;

    int type;
    @BindView(R.id.shadow_backtop)
    ShadowLayout toTopBtn;
    @BindView(R.id.tv_chooseCate)
    TextView tvChooseCate;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    private int page = 1;

    private boolean isInit = false;       //页面是否进行了初始化
    private boolean isRequesting = false; //是否正在刷新
    List<ModelMerchantList> mDatas = new ArrayList<>();
    List<ModelServiceItem> mDatas2 = new ArrayList<>();
    MerchantServicexAdapter adapter;
    String top_id = "";
    String sub_id = "";
    String i_key = "";
    String s_key = "";
    String typeName = "";
    private String store_id;

    SharePrefrenceUtil prefrenceUtil;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        store_id = arguments.getString("store_id");
    }

    public void searchRefresh() {
        if (!isInit) {
            isInit = true;
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    public void initView(View view) {
        /*rlContainer.setFocusable(true);
        rlContainer.setFocusableInTouchMode(true);
        rlContainer.requestFocus();*/
        prefrenceUtil=new SharePrefrenceUtil(mContext);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        adapter = new MerchantServicexAdapter(mDatas, mDatas2, mActivity, type);
        listview.setAdapter(adapter);
    }

    @Override
    public void initIntentData() {


    }


    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                isRequesting = true;
                /*listview.post(new Runnable() {
                    @Override
                    public void run() {
                        listview.setSelection(0);
                    }
                });*/
                if (type == 0) {
                    requestData();
                } else {
                    requestDataService();
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRequesting = true;
                if (type == 0) {
                    requestData();
                } else {
                    requestDataService();
                }
            }
        });

        listview.setOnScrollListener(mOnScrollListener);

        toTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview.smoothScrollToPosition(0);
                toTopBtn.setVisibility(View.GONE);
            }
        });

        Intent intent = mActivity.getIntent();
        if (intent != null) {
            tabType = intent.getStringExtra("tabType");
            if (!NullUtil.isStringEmpty(tabType)) {
                if ("service".equals(tabType)) {//服务
                    String subId = intent.getStringExtra("sub_id");
                    if (!NullUtil.isStringEmpty(subId)) {
                        sub_id = subId;
                        typeName = intent.getStringExtra("typeName");
                        if (!NullUtil.isStringEmpty(typeName)) {
                            tvChooseCate.setText(typeName);
                        } else {
                            tvChooseCate.setText("全部");
                        }
                    } else {
                        tvChooseCate.setText("全部");
                    }

                } else {//最受欢迎
                    tvChooseCate.setText("全部");
                }
                requestDataService();

            } else {
                tvChooseCate.setText("全部");
            }
        }


        if (intent != null) {
            String allCate = intent.getExtras().getString("allCate");
            if (!NullUtil.isStringEmpty(allCate)) {
                if ("all".equals(allCate)) {
                    top_id = intent.getExtras().getString("top_id");
                    sub_id = intent.getExtras().getString("sub_id");
                    typeName = intent.getExtras().getString("sub_name");
                    tvChooseCate.setText(typeName);
                }
            }
        }
    }

    String tabType = "";

    /**
     * 请求数据
     */
    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("c_id", prefrenceUtil.getXiaoQuId());
        if (!NullUtil.isStringEmpty(sub_id)) {
            params.put("category", sub_id);
        } else {
            if ("servicePop".equals(tabType)) {
                tvChooseCate.setText("全部");
            }
        }
        //       //从店铺进服务列表的时候传
        params.put("p", page + "");
        MyOkHttp.get().get(ApiHttpClient.GET_MERCHANTLIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                isRequesting = false;
//                hideDialog(smallDialog);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelMerchantList> list = (List<ModelMerchantList>) JsonUtil.getInstance().getDataArrayByName(response, "data", ModelMerchantList.class);
                    if (list != null && list.size() > 0) {
                        relNoData.setVisibility(View.GONE);
                        if (page == 1) {
                            mDatas.clear();
                            listview.post(new Runnable() {
                                @Override
                                public void run() {
                                    listview.setSelection(0);
                                    toTopBtn.setVisibility(View.GONE);
                                }
                            });
                        }
                        mDatas.addAll(list);
                        page++;
                        if (page <= list.get(0).getTotal_Pages()) {
                            refreshLayout.setEnableLoadMore(true);
                        } else {
                            refreshLayout.setEnableLoadMore(false);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            relNoData.setVisibility(View.VISIBLE);
                            mDatas.clear();
                        }
                        refreshLayout.setEnableLoadMore(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    ToastUtils.showShort(mContext.getApplicationContext(), msg);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                isRequesting = false;
//                hideDialog(smallDialog);
                ToastUtils.showShort(mContext.getApplicationContext(), "网络异常，请检查网络设置");
                if (refreshLayout!=null){
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    if (page == 1) {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }
        });
    }


    private void requestDataService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("c_id", prefrenceUtil.getXiaoQuId());
        if (!NullUtil.isStringEmpty(sub_id)) {
            params.put("category", sub_id);
        } else if ("servicePop".equals(tabType)) {
            Intent intent = mActivity.getIntent();
            if (intent != null) {
                String subId = intent.getStringExtra("sub_id");
                if (!NullUtil.isStringEmpty(subId)) {
                    params.put("category", subId);
                    String typeName = intent.getStringExtra("typeName");
                    if (!NullUtil.isStringEmpty(typeName)) {
                        tvChooseCate.setText(typeName);
                    }
                }
            }
        }

        if (!NullUtil.isStringEmpty(store_id) && type == 1) {
            params.put("i_id", store_id);
        }
        params.put("p", page + "");
        MyOkHttp.get().get(ApiHttpClient.GET_SERVICELIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                isRequesting = false;
//                hideDialog(smallDialog);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelServiceItem> list = (List<ModelServiceItem>) JsonUtil.getInstance().getDataArrayByName(response, "data", ModelServiceItem.class);
                    if (list != null && list.size() > 0) {
                        relNoData.setVisibility(View.GONE);
                        if (page == 1) {
                            mDatas2.clear();
                            listview.post(new Runnable() {
                                @Override
                                public void run() {
                                    listview.setSelection(0);
                                    toTopBtn.setVisibility(View.GONE);
                                }
                            });
                        }
                        mDatas2.addAll(list);
                        page++;
                        if (page <= list.get(0).getTotal_Pages()) {
                            refreshLayout.setEnableLoadMore(true);
                        } else {
                            refreshLayout.setEnableLoadMore(false);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            relNoData.setVisibility(View.VISIBLE);
                            mDatas2.clear();
                        }
                        refreshLayout.setEnableLoadMore(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    ToastUtils.showShort(mContext.getApplicationContext(), msg);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                isRequesting = false;
//                hideDialog(smallDialog);
                ToastUtils.showShort(mContext.getApplicationContext(), "网络异常，请检查网络设置");
                if (refreshLayout!=null){
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    if (page == 1) {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
            }
        });
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        if (type == 0) {
            isInit = true;
            refreshLayout.autoRefresh();
        }

    }

    /**
     * Listview滚动监听事件
     */
    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        private int lastVisibleItem;

        /**
         * 滚动状态改变时
         * @param absListView
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            int firstVisibleItemPosition = listview.getFirstVisiblePosition();
            //SCROLL_STATE_DRAGGING //正在被外部拖拽,一般为用户正在用手指滚动
            //SCROLL_STATE_SETTLING //自动滚动开始
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                // 判断是否滚动超过一屏
                if (firstVisibleItemPosition == 0) {
                    toTopBtn.setVisibility(View.GONE);
                } else {
                    toTopBtn.setVisibility(View.VISIBLE);
                }
            } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {// 滚动时
                if (firstVisibleItemPosition == 0) {
                    toTopBtn.setVisibility(View.GONE);
                } else {
                    toTopBtn.setVisibility(View.VISIBLE);
                }

            }

        }

        /**
         * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
         * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
         */
        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
            lastVisibleItemPosition = firstVisibleItem;

        }
    };
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置

    @Override
    public int getLayoutId() {
        return R.layout.fragement_servicex_merchat;
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

    Intent intent = new Intent();

    @OnClick({R.id.lin_searchAll_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_searchAll_btn:
                intent.setClass(mContext, ServiceClassifyActivity.class);
                startActivityForResult(intent, 11);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 11) {
            if (resultCode == ServiceClassifyActivity.REQUEST_CODE_CAT) {
                if (data != null) {

                    top_id = data.getExtras().getString("top_id");
                    sub_id = data.getExtras().getString("sub_id");
                    typeName = data.getExtras().getString("sub_name");
                    tvChooseCate.setText(typeName);

//                    LogUtils.d("top_id=" + top_id + ",sub_id=" + sub_id + ",sub_name=" + typeName);
                    page = 1;
                    if (type == 0) {
                        requestData();
                    } else {
                        requestDataService();
                    }

                }
            }
        }
    }

}
