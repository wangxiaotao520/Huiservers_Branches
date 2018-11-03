package com.huacheng.huiservers.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.Shop_order_listAdapter;
import com.huacheng.huiservers.center.bean.ShopOrderBeanType;
import com.huacheng.huiservers.center.bean.XorderDetailBean;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：商城订单
 * 时间：2018/9/19 15:37
 * created by DFF
 */
public class ShopOrderListCommon extends BaseFragment implements Shop_order_listAdapter.Callback {
    private ListView mListView;
    private SmartRefreshLayout refreshLayout;
    private int page = 1;
    private boolean isInit = false;       //页面是否进行了初始化
    private boolean isRequesting = false; //是否正在刷新
    int type;
    RelativeLayout rel_no_data;
    List<ShopOrderBeanType> datas = new ArrayList<>();
    Shop_order_listAdapter new_shop_orderAdapter;
    private String mType_back;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        mType_back = arguments.getString("type_back");
    }

    @Override
    public void initView(View view) {
        mListView = view.findViewById(R.id.listview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rel_no_data = view.findViewById(R.id.rel_no_data);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        new_shop_orderAdapter = new Shop_order_listAdapter(mActivity, datas, type, this);
        mListView.setAdapter(new_shop_orderAdapter);

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
                requestData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRequesting = true;
                requestData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, New_Shop_Order_DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("order_id", datas.get(position).getId());
                bundle.putString("status", type + "");
                bundle.putString("order_num", datas.get(position).getOrder_number());
                bundle.putString("item_id", position + "");
                System.out.println("item_id$$$$$$$$&" + position + "");
                System.out.println("getOrder_numbe$$$$$$$$&" + datas.get(position).getOrder_number() + "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //第一页做特殊处理
        if (type == 0) {
            isInit = true;
//            page = 1;
//            isRequesting = true;
//            showDialog(smallDialog);
//            requestData();
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
        //从支付成功页跳转到订单
        if (type == 1 && !NullUtil.isStringEmpty(mType_back) && "type_zf_dfk".equals(mType_back)) {
            isInit = true;
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
        if (type == 2 && !NullUtil.isStringEmpty(mType_back) && "type_zf_dsh".equals(mType_back)) {
            isInit = true;
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        // 根据接口请求数据
        HashMap<String, String> params = new HashMap<>();
        if (type == 0) {
            params.put("status", "0");
        } else if (type == 1) {
            params.put("status", "1");
        } else if (type == 2) {
            params.put("status", "2");
        } else if (type == 3) {
            params.put("status", "3");
        }
        params.put("p", page + "");

        MyOkHttp.get().get(ApiHttpClient.GET_SHOP_ORDER_LIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                isRequesting = false;
                hideDialog(smallDialog);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ShopOrderBeanType> modelOrderList = (List<ShopOrderBeanType>) JsonUtil.getInstance().getDataArrayByName(response, "data", ShopOrderBeanType.class);
                    inflateContent(modelOrderList);
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(mActivity, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                isRequesting = false;
                hideDialog(smallDialog);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                XToast.makeText(mActivity, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
                if (page == 1) {
                    refreshLayout.setEnableLoadMore(false);
                }
            }
        });
    }

    /**
     * 填充数据
     *
     * @param
     */
    private void inflateContent(List<ShopOrderBeanType> modelOrderList) {
        if (modelOrderList != null && modelOrderList.size() > 0) {
            rel_no_data.setVisibility(View.GONE);
            List<ShopOrderBeanType> list_new = modelOrderList;
            if (page == 1) {
                datas.clear();
            }
            datas.addAll(list_new);
            page++;
            if (page > Integer.valueOf(modelOrderList.get(0).getTotal_Pages())) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                refreshLayout.setEnableLoadMore(true);
            }
            new_shop_orderAdapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                // 占位图显示出来
                rel_no_data.setVisibility(View.VISIBLE);
                datas.clear();
            }
            refreshLayout.setEnableLoadMore(false);
            new_shop_orderAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.shop_order_common;
    }

    /**
     * 选中时才调用
     *
     * @param param
     */
    public void onTabSelectedRefresh(String param) {
        if (type > 0 && isInit == false && isRequesting == false) {
            isInit = true;
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
    }

    /**
     * 刷新
     *
     * @param param
     */
    public void onTabRefresh(String param) {
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }

    }

    /**
     * 删除点击事件
     *
     * @param adbeanTypes
     * @param type
     */
    List<String> list_info_id = new ArrayList<String>();

    @Override
    public void click(ShopOrderBeanType adbeanTypes) {

        list_info_id.clear();
        for (int j = 0; j < adbeanTypes.getImg().size(); j++) {
            String info_id = adbeanTypes.getImg().get(j).getInfo_id();
            list_info_id.add(info_id);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list_info_id.size(); i++) {
            if (i == 0) {
                sb.append(String.valueOf(list_info_id.get(i)));
            } else {
                sb.append("," + String.valueOf(list_info_id.get(i)));
            }
        }
        getdatas(sb.toString(), adbeanTypes);

    }

    private void getdatas(String id, final ShopOrderBeanType adbean) {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);

        MyOkHttp.get().post(ApiHttpClient.GET_SHOP_ORDER_DEL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {

                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getId().equals(adbean.getId())) {
                            datas.remove(i);
                        }
                    }
                    new_shop_orderAdapter.notifyDataSetChanged();
                    XToast.makeText(mActivity, "删除成功", XToast.LENGTH_SHORT).show();
                } else {
                    try {
                        XToast.makeText(mActivity, response.getString("msg"), XToast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(mActivity, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 详情界面回调退款 评价 删除
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void back(XorderDetailBean info) {
        if (info != null) {
            if (info.getBack_type() == 1) {//删除
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getId().equals(info.getId())) {
                        datas.remove(i);
                    }
                }
                new_shop_orderAdapter.notifyDataSetChanged();
            } else if (info.getBack_type() == 2) {//评价
               /* //待评价刷新一下
                for (int i = 0; i < datas.size(); i++) {
                    for (int i1 = 0; i1 < datas.get(i).getImg().size(); i1++) {
                        if (datas.get(i).getImg().get(i1).getInfo_id().equals(info.getId())) {
                            datas.get(i).getImg().remove(i);
                        }
                    }
                }*/
                EventBus.getDefault().post("pj");
                new_shop_orderAdapter.notifyDataSetChanged();
            } else if (info.getBack_type() == 3) {//退款 以及收货
                EventBus.getDefault().post("tk_sh");
                new_shop_orderAdapter.notifyDataSetChanged();
            } else if (info.getBack_type() == 4) {//订单中支付成功
                EventBus.getDefault().post("zf");
                new_shop_orderAdapter.notifyDataSetChanged();
            }
        }
    }

}
