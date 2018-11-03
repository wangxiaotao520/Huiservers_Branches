package com.huacheng.huiservers.servicenew.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderList;
import com.huacheng.huiservers.servicenew.ui.adapter.FragmentOrderAdapter;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：订单
 * 时间：2018/7/20 09:43
 * created by DFF
 */
public class FragmentOrderCommon extends BaseFragment implements View.OnClickListener {
    private ListView listview;
    private SmartRefreshLayout refreshLayout;
    private int page = 1;
    private boolean isInit = false;       //页面是否进行了初始化
    private boolean isRequesting = false; //是否正在刷新
    int type;
    FragmentOrderAdapter fragmentOrderAdapter;
    RelativeLayout rel_no_data;
    private List<ModelOrderList> datas = new ArrayList();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {

        listview = view.findViewById(R.id.listview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rel_no_data = view.findViewById(R.id.rel_no_data);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        fragmentOrderAdapter = new FragmentOrderAdapter(mActivity, datas, type);
        listview.setAdapter(fragmentOrderAdapter);

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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (datas.get(position).getStatus().equals("6")) {
                    Toast.makeText(mActivity, "订单已取消", XToast.LENGTH_SHORT).show();
                } else {
                    //   详情跳转
                    ModelOrderList modelOrderList = datas.get(position);
                    Intent intent = new Intent(mActivity, ServiceOrderDetailActivity.class);
                    intent.putExtra("order_id", modelOrderList.getId());
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        // 根据接口请求数据
        //dsm->待上门  wfk->未付款 dpj->待评价 ypj->已评价 qxdd->取消订单
        HashMap<String, String> params = new HashMap<>();
        if (type == 0) {
            params.put("type", "qb");
        } else if (type == 1) {
            params.put("type", "dfw");
        } else if (type == 2) {
            params.put("type", "dpj");
        } else if (type == 3) {
            params.put("type", "wc");
        }
        params.put("p", page + "");

        MyOkHttp.get().get(ApiHttpClient.GET_MY_ORDER, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                isRequesting = false;
                hideDialog(smallDialog);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelOrderList> modelOrderList = (List<ModelOrderList>) JsonUtil.getInstance().getDataArrayByName(response, "data", ModelOrderList.class);
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
    private void inflateContent(List<ModelOrderList> modelOrderList) {
        if (modelOrderList != null && modelOrderList.size() > 0) {
            rel_no_data.setVisibility(View.GONE);
            List<ModelOrderList> list_new = modelOrderList;
            if (page == 1) {
                datas.clear();
            }
            datas.addAll(list_new);
            page++;
            if (page > modelOrderList.get(0).getTotal_Pages()) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                refreshLayout.setEnableLoadMore(true);
            }
            fragmentOrderAdapter.notifyDataSetChanged();
            if (page == 2) {
                listview.post(new Runnable() {
                    @Override
                    public void run() {
                        listview.setSelection(0);
                    }
                });
            }
        } else {
            if (page == 1) {
                // 占位图显示出来
                rel_no_data.setVisibility(View.VISIBLE);
                datas.clear();
            }
            refreshLayout.setEnableLoadMore(false);
            fragmentOrderAdapter.notifyDataSetChanged();
        }

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
/*
        //支付完成后传过来的值
        Bundle arguments = getArguments();
        if (arguments != null) {
            String typeReceipt = arguments.getString("typeReceipt", "0");
            if ("2".equals(typeReceipt) && type == 2) {
                page = 1;
                isInit = true;
                refreshLayout.autoRefresh();
            }
        }*/

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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_listcommon;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 回调
     *
     * @param
     * @param
     */

   /* @Override
    public void click(final ModelOrderList.ListBean listBean, final int type) {

        if (type == 0) {//上门
            new CommomDialog(mContext, R.style.dialog, "确定上门", new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        getSend(listBean, type);
                        dialog.dismiss();
                    }
                }
            }).show();
        } else if (type == 1) {//收款
            Intent intent = new Intent(mActivity, ReceiptActivity.class);
            String oid = listBean.getId();
            String onum = listBean.getOrder_number();
            intent.putExtra("oid", oid);
            intent.putExtra("onum", onum);
            startActivity(intent);

        }
    }
*/
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 更新数据（取消订单，评价完成）
     *
     * @param modelOrderList
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOrderList(ModelOrderList modelOrderList) {
        ModelOrderList modelOrderList_remove = null;
        if (modelOrderList != null) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).getId().equals(modelOrderList.getId())) {
                    modelOrderList_remove = datas.get(i);
                }
            }
            //全部
            if (modelOrderList_remove != null) {
                if (type == 0) {
                    if (modelOrderList.getEvent_type() == 0) {
                        //取消订单
                        modelOrderList_remove.setStatus("6");
                        fragmentOrderAdapter.notifyDataSetChanged();
                    } else if (modelOrderList.getEvent_type() == 1) {
                        //评价完成
                        modelOrderList_remove.setStatus("5");
                        modelOrderList_remove.setEvaluate(modelOrderList.getEvaluate() + "");
                        modelOrderList_remove.setScore(modelOrderList.getScore() + "");
                        modelOrderList_remove.setEvaluatime(modelOrderList.getEvaluatime());
                        fragmentOrderAdapter.notifyDataSetChanged();
                    }
                } else if (type == 1) {//待服务
                    if (modelOrderList.getEvent_type() == 0) {
                        if (modelOrderList_remove!=null){
                            datas.remove(modelOrderList_remove);
                            fragmentOrderAdapter.notifyDataSetChanged();
                        }
                    }
                }else if (type==2){//待评价
                    if (modelOrderList.getEvent_type() == 1) {
                        if (modelOrderList_remove!=null){
                            datas.remove(modelOrderList_remove);
                            fragmentOrderAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    //完成
                    //取消订单和评价完成都要刷新
                    if(refreshLayout!=null){
                        refreshLayout.autoRefresh();
                    }
                }
            }

        }
    }
}
