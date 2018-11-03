package com.huacheng.huiservers.servicenew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ModelServiceIndex;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.servicenew.model.ModelItemBean;
import com.huacheng.huiservers.servicenew.ui.adapter.MoreServiceAdapter;
import com.huacheng.huiservers.servicenew.ui.adapter.ServiceRecycleviewAdapter;
import com.huacheng.huiservers.servicenew.ui.order.FragmentOrderListActivity;
import com.huacheng.huiservers.servicenew.ui.scan.CustomCaptureActivity;
import com.huacheng.huiservers.servicenew.ui.search.ServicexSearchActivity;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.SharePrefrenceUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description: 新版服务页面
 * created by wangxiaotao
 * 2018/9/3 0003 下午 6:48
 */
public class FragmentServiceNew extends BaseFragment implements View.OnClickListener {

    public static final int CAMERA_STATE_REQUEST_CODE = 102; //获取相机权限
    ServiceAdapter serviceAdapter;
    ListView lvService;
    RecyclerView rlv_service;
    RelativeLayout rel_container;
    SharePrefrenceUtil prefrenceUtil;
    ServiceRecycleviewAdapter adapter_rlv;
    LinearLayout lin_s_scanCode, lin_s_search, lin_s_order;
    SharedPreferences preferencesLogin;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;
    Unbinder unbinder;
    @BindView(R.id.rel_menu_top)
    RelativeLayout relMenuTop;

    private String login_type;
    Intent intent = new Intent();
    /*List<ModelAds> modelAds_list = new ArrayList<>();
    ModelServiceIndex service = new ModelServiceIndex();*/
    List<ModelItemBean> mDatas = new ArrayList<>();
    List<ModelItemBean> data = new ArrayList<>();
    int page = 2;
    int totalPage = 2;


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

    @Override
    public void initView(View view) {
        preferencesLogin = mActivity.getSharedPreferences("login", 0);
        prefrenceUtil = new SharePrefrenceUtil(mContext);
        // lvService = view.findViewById(R.id.lv_service);
        rel_container = view.findViewById(R.id.rel_container);
        rlv_service = view.findViewById(R.id.rlv_service);

        lin_s_scanCode = view.findViewById(R.id.lin_s_scanCode);
        lin_s_search = view.findViewById(R.id.lin_s_search);
        lin_s_order = view.findViewById(R.id.lin_s_order);

        rel_container.requestFocus();
        rel_container.setFocusable(true);
        rel_container.setFocusableInTouchMode(true);

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);

    }

    @Override
    public void initIntentData() {

    }


    @Override
    public void initListener() {
        lin_s_scanCode.setOnClickListener(this);
        lin_s_search.setOnClickListener(this);
        lin_s_order.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                getbanner();
            }
        });

        rlv_service.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //在这里做颜色渐变的操作
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    relMenuTop.setBackgroundColor(getResources().getColor(R.color.white));
////                    setAlpha(1);
                } else {
                    relMenuTop.setBackgroundColor(getResources().getColor(R.color.transparents));
                  /*  View firstView = layoutManager.findViewByPosition(position);
                    int top = firstView.getTop();
                    int height = relMenuTop.getHeight();
                    int firstHeight = firstView.getHeight();
                    setAlpha(Math.min(1f, (float) -top / (float) (firstHeight - height)));*/
                }

            }

        });


    }

    /*float alpha=0;
    public void setAlpha(float alpha) {
        if (alpha == this.alpha) return;
        this.alpha = alpha;
        relMenuTop.setAlpha(alpha);
//        relMenuTop.setBackgroundAlpha(alpha);
    }*/


    /**
     * 服务首页数据
     */
    private void getServiceIndex() {
        HashMap<String, String> params = new HashMap<>();
        params.put("c_id", prefrenceUtil.getXiaoQuId());
        MyOkHttp.get().get(ApiHttpClient.GET_SERVICE_INDEX, params, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (JsonUtil.getInstance().isSuccess(response)) {
                    ModelServiceIndex serviceSingle = (ModelServiceIndex) JsonUtil.getInstance().parseJsonFromResponse(response, ModelServiceIndex.class);
                    contentInflate(serviceSingle);
                } else {
                    relNoData.setVisibility(View.VISIBLE);
                    relMenuTop.setVisibility(View.GONE);
                    rlv_service.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        });
    }


    /**
     * 获取服务首页数据
     *
     * @param modelAds_list
     */
    private void getServiceIndex(final List<ModelAds> modelAds_list) {
        HashMap<String, String> params = new HashMap<>();
        params.put("c_id", prefrenceUtil.getXiaoQuId());
        MyOkHttp.get().get(ApiHttpClient.GET_SERVICE_INDEX, params, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (JsonUtil.getInstance().isSuccess(response)) {
                    ModelServiceIndex serviceInternal = (ModelServiceIndex) JsonUtil.getInstance().parseJsonFromResponse(response, ModelServiceIndex.class);
                    contentInflate(modelAds_list, serviceInternal);
                } else {
                    //service 数据为空时，只显示顶部广告
                    if (modelAds_list != null && modelAds_list.size() > 0) {
                        relNoData.setVisibility(View.GONE);
                        relMenuTop.setVisibility(View.VISIBLE);
                        rlv_service.setVisibility(View.VISIBLE);
                        adapter_rlv = new ServiceRecycleviewAdapter(null, modelAds_list, mContext);
                        rlv_service.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
                        rlv_service.setItemAnimator(new DefaultItemAnimator());
                        rlv_service.setAdapter(adapter_rlv);

                    } else {
                        relNoData.setVisibility(View.VISIBLE);
                        relMenuTop.setVisibility(View.GONE);
                        rlv_service.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        });
    }

    private void contentInflate(List<ModelAds> modelAds_list, ModelServiceIndex serviceInternal) {
        if (serviceInternal != null || (modelAds_list != null && modelAds_list.size() > 0)) {
            relMenuTop.setVisibility(View.VISIBLE);
            rlv_service.setVisibility(View.VISIBLE);
            relNoData.setVisibility(View.GONE);
            adapter_rlv = new ServiceRecycleviewAdapter(serviceInternal, modelAds_list, mContext);
            rlv_service.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            rlv_service.setItemAnimator(new DefaultItemAnimator());
            rlv_service.setAdapter(adapter_rlv);
//            adapter_rlv.notifyDataSetChanged();
            adapter_rlv.setOnItemMoreServiceListener(new ServiceRecycleviewAdapter.IMoreServiceListener() {
                @Override
                public void onItemMoreService(ServiceRecycleviewAdapter.ViewHolder finalHolder) {
                    mlv_more_service = finalHolder.mlv_more_service;
                    lin_btn_more_service = finalHolder.lin_btn_more_service;
                    lin_btn_more_service.setVisibility(View.GONE);

                    getMoreService();

                    moreServiceAdapter = new MoreServiceAdapter(mDatas, mContext);
                    mlv_more_service.setAdapter(moreServiceAdapter);
                }

            });

        } else {
            relMenuTop.setVisibility(View.GONE);
            rlv_service.setVisibility(View.GONE);
            relNoData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        showDialog(smallDialog);
        getbanner();

    }

    private boolean firstIsDialog = true;

    private void getbanner() {//获取商城首页顶部广告信息

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("community_id", prefrenceUtil.getXiaoQuId());
        hashMap.put("c_name", "service_indextop");
        MyOkHttp.get().get(ApiHttpClient.GET_ADVERTISING, hashMap, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                //   hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelAds> modelAds_list = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelAds.class);
                    if (firstIsDialog) {
                        showDialog(smallDialog);
                        firstIsDialog = false;
                    }
                    getServiceIndex(modelAds_list);
                } else {
                    if (firstIsDialog) {
                        showDialog(smallDialog);
                        firstIsDialog = false;
                    }
                    getServiceIndex();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
              if (relMenuTop!=null) {
                  relMenuTop.setVisibility(View.GONE);
                  rlv_service.setVisibility(View.GONE);
                  relNoData.setVisibility(View.VISIBLE);
              }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        });

    }

    MyListView mlv_more_service;
    MoreServiceAdapter moreServiceAdapter;
    LinearLayout lin_btn_more_service;

    /**
     * 填充数据
     *
     * @param service
     */
    private void contentInflate(ModelServiceIndex service) {
        if (service != null) {
            relMenuTop.setVisibility(View.VISIBLE);
            rlv_service.setVisibility(View.VISIBLE);
            relNoData.setVisibility(View.GONE);
            adapter_rlv = new ServiceRecycleviewAdapter(service, null, mContext);
            rlv_service.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            rlv_service.setItemAnimator(new DefaultItemAnimator());
            rlv_service.setAdapter(adapter_rlv);
            adapter_rlv.notifyDataSetChanged();

            adapter_rlv.setOnItemMoreServiceListener(new ServiceRecycleviewAdapter.IMoreServiceListener() {
                @Override
                public void onItemMoreService(ServiceRecycleviewAdapter.ViewHolder finalHolder) {
                    mlv_more_service = finalHolder.mlv_more_service;
                    lin_btn_more_service = finalHolder.lin_btn_more_service;
                    lin_btn_more_service.setVisibility(View.GONE);

                    getMoreService();

                    moreServiceAdapter = new MoreServiceAdapter(mDatas, mContext);
                    mlv_more_service.setAdapter(moreServiceAdapter);
                }

            });

        } else {
            relMenuTop.setVisibility(View.GONE);
            rlv_service.setVisibility(View.GONE);
            relNoData.setVisibility(View.VISIBLE);
        }


    }


    private void getMoreService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("p", page + "");
        if (page <= totalPage) {
            showDialog(smallDialog);
            MyOkHttp.get().get(ApiHttpClient.GET_SERVICEMORE, params, new JsonResponseHandler() {
                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    hideDialog(smallDialog);
                    if (JsonUtil.getInstance().isSuccess(response)) {
                        data = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelItemBean.class);
                        if (data != null && data.size() > 0) {
                            lin_btn_more_service.setVisibility(View.VISIBLE);
                            if (page == 1) {
                                mDatas.clear();
                            }
                            mDatas.addAll(data);
                            page++;
                            totalPage = data.get(0).getTotal_Pages();
                            moreServiceAdapter.notifyDataSetChanged();

                        } else {
                            if (page == 1) {
                                // 占位图显示出来
                                mDatas.clear();
                            }
                            lin_btn_more_service.setVisibility(View.GONE);
                        }
                    } else {
                        String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                        ToastUtils.showShort(mContext.getApplicationContext(), msg);
                    }
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    hideDialog(smallDialog);
                    ToastUtils.showShort(mContext.getApplicationContext(), "网络异常，请检查网络设置");
                }
            });
        } else {
            lin_btn_more_service.setVisibility(View.GONE);
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.frgment_service_new;
    }

    @Override
    public void onClick(View view) {
        login_type = preferencesLogin.getString("login_type", "");
        switch (view.getId()) {
            case R.id.lin_s_scanCode:
                preferencesLogin = mActivity.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(mActivity, LoginVerifyCode1Activity.class));
                } else {
//                    !ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)
//                   以下方法 判断是否开启相机无效 0928
                   /* if (!PermissionUtils.checkPermissionGranted(mActivity, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.CAMERA}, CAMERA_STATE_REQUEST_CODE);
                    } else {
                    }*/

                    IntentIntegrator intentIntegrator = new IntentIntegrator(mActivity)
                            .setOrientationLocked(false);
                    intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                        /*intentIntegrator.setPrompt("将服务师傅的二维码放入框内\n" +
                            "即可扫描付款");*/
                    // 设置自定义的activity是ScanActivity
                    intentIntegrator.initiateScan(); // 初始化扫描
                }
                break;
            case R.id.lin_s_search:
                intent.setClass(mContext, ServicexSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_s_order:
                preferencesLogin = mActivity.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(getActivity(), LoginVerifyCode1Activity.class));
                } else {
                    intent = new Intent(mContext, FragmentOrderListActivity.class);
                    startActivity(intent);
                }

                break;
            default:
                break;
        }
    }

}
