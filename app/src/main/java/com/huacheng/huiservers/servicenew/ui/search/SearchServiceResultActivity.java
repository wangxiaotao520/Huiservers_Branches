package com.huacheng.huiservers.servicenew.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.servicenew.model.ModelMerchantList;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.servicenew.ui.adapter.MerchantServicexAdapter;
import com.huacheng.huiservers.shop.SearchShopActivity;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.view.ShadowLayout;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchServiceResultActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.shadow_backtop)
    ShadowLayout shadowBacktop;
    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;
    private ImageView search_back;
    private EditText et_search;
    TextView txt_search;

    SharePrefrenceUtil prefrenceUtil;
    private List<ShopIndexBean> beans = new ArrayList<ShopIndexBean>();
    private List<ShopIndexBean> bean = new ArrayList<ShopIndexBean>();
    private ShopProtocol protocol = new ShopProtocol();
    private int page = 1;// 当前页
    int totalPage = 0;// 总页数

    // 1. 初始化搜索框变量
    LinearLayout lin_search_block;
    MerchantServicexAdapter adapter;
    List<ModelMerchantList> mDatas = new ArrayList<>();
    List<ModelServiceItem> mDatas2 = new ArrayList<>();
    int type;
    String keywords = "";
    String i_key = "";
    String s_key = "";
    private boolean isRequesting = false; //是否正在刷新

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        lin_search_block = (LinearLayout) findViewById(R.id.lin_search_block);
        search_back = (ImageView) findViewById(R.id.search_back);
        et_search = (EditText) findViewById(R.id.et_search);
        txt_search = (TextView) findViewById(R.id.txt_search);
    }


    @Override
    protected void initData() {
        prefrenceUtil = new SharePrefrenceUtil(this);
        i_key = getIntent().getStringExtra("i_key");
        s_key = getIntent().getStringExtra("s_key");
        type = getIntent().getIntExtra("serviceType", 0);

        if (type == 0) {
            requestData();
        } else {
            requestDataService();
        }

        //默认失去焦点
//        et_search.clearFocus();//
        lin_search_block.setFocusable(true);
        lin_search_block.setFocusableInTouchMode(true);
        /*et_search.setSelection(keywords.length());*/

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        adapter = new MerchantServicexAdapter(mDatas, mDatas2, this, type);
        listview.setAdapter(adapter);

        // 设置加载更多监听
        search_back.setOnClickListener(this);
        txt_search.setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_service_result;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.txt_search:
                if (StringUtils.isEmpty(et_search.getText().toString())) {
                    return;
//                    XToast.makeText(this, "请输入关键字", XToast.LENGTH_SHORT).show();
                } else {
                    String keyWords2 = et_search.getText().toString();
                    page = 1;
                    mDatas.clear();
                    mDatas2.clear();
                    if (type == 0) {
                        i_key = keyWords2;
                        requestData();
                    } else {
                        s_key = keyWords2;
                        requestDataService();
                    }
                }
                break;
            case R.id.search_back:
                Intent intent = new Intent(this, ServicexSearchActivity.class);
                intent.putExtra("keywords", keywords);
                setResult(11, intent);
                finish();
                break;
            default:
                break;
        }

    }


    /**
     * 请求数据
     */
    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        if (!NullUtil.isStringEmpty(i_key)) {
            params.put("i_key", i_key);
            keywords = i_key;
            et_search.setText(keywords);

        }
        //从店铺进服务列表的时候传
        params.put("p", page + "");
        params.put("c_id", prefrenceUtil.getXiaoQuId());
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
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                ToastUtils.showShort(mContext.getApplicationContext(), "网络异常，请检查网络设置");
                if (page == 1) {
                    refreshLayout.setEnableLoadMore(false);
                }
            }
        });
    }


    private void requestDataService() {
        HashMap<String, String> params = new HashMap<>();
        if (!NullUtil.isStringEmpty(s_key)) {
            params.put("s_key", s_key);
            keywords = s_key;
            et_search.setText(keywords);
        }
        params.put("c_id", prefrenceUtil.getXiaoQuId());
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
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                ToastUtils.showShort(mContext.getApplicationContext(), "网络异常，请检查网络设置");
                if (page == 1) {
                    refreshLayout.setEnableLoadMore(false);
                }
            }
        });
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
        // TODO Auto-generated method stub
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }
}
