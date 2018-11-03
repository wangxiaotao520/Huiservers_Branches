package com.huacheng.huiservers.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.fragment.shop.ShopListSearchAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends BaseUI implements OnClickListener {
    private ImageView search_back;
    private EditText et_search;
    TextView txt_search;

    SharePrefrenceUtil prefrenceUtil;
    private List<ShopIndexBean> beans = new ArrayList<ShopIndexBean>();
    private List<ShopIndexBean> bean = new ArrayList<ShopIndexBean>();
    private ShopProtocol protocol = new ShopProtocol();

    private int page = 1;// 当前页
    int totalPage = 0;// 总页数

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerview;
    private RelativeLayout rel_no_data;

    ShopListSearchAdapter searchAdapter;

    // 1. 初始化搜索框变量
    String keywords = "";
    RecyclerViewNoBugLinearLayoutManager manager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {
        super.init();
//        SetTransStatus.GetStatus(this);
        setContentView(R.layout.search_result);
        prefrenceUtil = new SharePrefrenceUtil(this);
        keywords = getIntent().getStringExtra("keystr");
        // 3. 绑定组件
        LinearLayout lin_search_block = (LinearLayout) findViewById(R.id.lin_search_block);
        search_back = (ImageView) findViewById(R.id.search_back);
        et_search = (EditText) findViewById(R.id.et_search);
        txt_search = (TextView) findViewById(R.id.txt_search);
        et_search.setText(keywords);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
        manager = new RecyclerViewNoBugLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        searchAdapter = new ShopListSearchAdapter(beans, SearchResultActivity.this, "inv");
        recyclerview.setAdapter(searchAdapter);
        //默认失去焦点
//        et_search.clearFocus();//
        lin_search_block.setFocusable(true);
        lin_search_block.setFocusableInTouchMode(true);
        /*et_search.setSelection(keywords.length());*/
        showDialog(smallDialog);
        getdata();

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
//                beans.clear();
                page = 1;
                getdata();
//                // 延时1s关闭下拉刷新
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 500);

            }
        });

        recyclerview.addOnScrollListener(mOnScrollListener);
        // 设置加载更多监听
        search_back.setOnClickListener(this);
        txt_search.setOnClickListener(this);

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
                    && lastVisibleItem + 1 == searchAdapter.getItemCount()
                    && searchAdapter.isShowFooter() && !swipeRefreshLayout.isRefreshing())//
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
        if (page<=totalPage){
            searchAdapter.setLoadState(searchAdapter.LOADING);
            getdata();
        }
//        if (page < totalPage) {
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            page++;
//                            getdata();
//                            searchAdapter.setLoadState(searchAdapter.LOADING_COMPLETE);
//
//                        }
//                    });
//                }
//            }, 500);
//
//        } else {
//            // 显示加载到底的提示
//            searchAdapter.setLoadState(searchAdapter.LOADING_END);
//        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.txt_search:
                if (StringUtils.isEmpty(et_search.getText().toString())) {
                    XToast.makeText(this, "请输入关键字", XToast.LENGTH_SHORT).show();
                } else {
                    page = 1;
                    beans.clear();
                    showDialog(smallDialog);
                    getdata();
                }
                break;
            case R.id.search_back:
                Intent intent = new Intent(this, SearchShopActivity.class);
                intent.putExtra("keywords", keywords);
                setResult(11, intent);
                finish();
                break;
            default:
                break;
        }

    }

    private void getdata() {// 搜索界面接口
        Url_info info = new Url_info(this);
        String url = info.goods_search + "key/" + et_search.getText().toString() + "/m_id/" + prefrenceUtil.getXiaoQuId() + "/p/" + page;
        new HttpHelper(url, SearchResultActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                bean = protocol.getProSearchList(json);
                if (bean != null) {
                    if (bean.size() > 0) {
                        // count = bean.size();
                        rel_no_data.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            beans.clear();
                        }
                        beans.addAll(bean);
                        totalPage = Integer.valueOf(beans.get(0).getTotal_Pages());// 设置总页数
                        page++;
                        if (page>totalPage){
                            searchAdapter.setLoadState(searchAdapter.LOADING_END);
                        }else {
                            searchAdapter.setLoadState(searchAdapter.LOADING_COMPLETE);
                        }
                        searchAdapter.notifyDataSetChanged();
                    } else {
                        if (page==1){
                            recyclerview.setVisibility(View.GONE);
                            rel_no_data.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (searchAdapter!=null){
                    searchAdapter.setLoadState(searchAdapter.LOADING_COMPLETE);
                }
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, SearchShopActivity.class);
            intent.putExtra("keywords", keywords);
            setResult(11, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
