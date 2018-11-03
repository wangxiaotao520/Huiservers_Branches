package com.huacheng.huiservers.servicenew.ui.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderList;
import com.huacheng.huiservers.servicenew.model.ModelServiceOrderDetail;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.ToolUtils;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Description: 订单详情
 * created by wangxiaotao
 * 2018/9/4 0004 下午 6:17
 */
public class ServiceOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_order_detail_title;
    private SimpleDraweeView sdv_head;
    private TextView tv_service_name;
    private TextView tv_service_detail;
    private TextView tv_order_number;
    private TextView tv_order_time;
    private TextView tv_address;
    private TextView tv_beizhu;
    private LinearLayout ll_dpd;
    private LinearLayout ll_ypd;
    private LinearLayout ll_fwz;
    private LinearLayout ll_dpj;
    private LinearLayout ll_wc;
    private LinearLayout ll_master_container;
    private TextView tv_master_name;
    private TextView tv_master_phone;
    private ImageView iv_phone_icon;
    private TextView tv_cancel;
    private TextView tv_pingjia;
    private TextView tv_tousu;
    private String order_id;
    ModelServiceOrderDetail model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        findTitleViews();
        titleName.setText("订单详情");
        ll_order_detail_title = findViewById(R.id.ll_order_detail_title);
        sdv_head = findViewById(R.id.sdv_head);
        tv_service_name = findViewById(R.id.tv_service_name);
        tv_service_detail = findViewById(R.id.tv_service_detail);
        tv_order_number = findViewById(R.id.tv_order_number);
        tv_order_time = findViewById(R.id.tv_order_time);
        tv_address = findViewById(R.id.tv_address);
        tv_beizhu = findViewById(R.id.tv_beizhu);
        ll_dpd = findViewById(R.id.ll_dpd);
        ll_ypd = findViewById(R.id.ll_ypd);
        ll_fwz = findViewById(R.id.ll_fwz);
        ll_dpj = findViewById(R.id.ll_dpj);
        ll_wc = findViewById(R.id.ll_wc);
        ll_master_container = findViewById(R.id.ll_master_container);
        tv_master_name = findViewById(R.id.tv_master_name);
        tv_master_phone = findViewById(R.id.tv_master_phone);
        iv_phone_icon = findViewById(R.id.iv_phone_icon);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_pingjia = findViewById(R.id.tv_pingjia);
        tv_tousu = findViewById(R.id.tv_tousu);
    }

    @Override
    protected void initData() {


        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", order_id);
        MyOkHttp.get().post(ApiHttpClient.GET_ORDER_DETAIL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    ModelServiceOrderDetail model = (ModelServiceOrderDetail) JsonUtil.getInstance().parseJsonFromResponse(response, ModelServiceOrderDetail.class);
                    inflateContent(model);
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "获取数据失败");
                    XToast.makeText(mContext, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(mContext, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 填充数据
     *
     * @param model
     */
    private void inflateContent(ModelServiceOrderDetail model) {
        if (model != null) {
            this.model=model;
            FrescoUtils.getInstance().setImageUri(sdv_head, ApiHttpClient.IMG_SERVICE_URL + model.getTitle_img());
            tv_service_name.setText(model.getS_name() + "");
            tv_order_number.setText("订单编号: " + model.getOrder_number());
            tv_order_time.setText("下单时间: " + ToolUtils.getStandardTime(model.getAddtime()));
            tv_address.setText(model.getAddress() + "");
            tv_beizhu.setText(model.getDescription());
            tv_master_name.setText(model.getW_fullname() + "");
            tv_master_phone.setText(model.getMobile() + "");
            String status = model.getStatus();
            if ("1".equals(status)) {
                //待派单
                ll_dpd.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
            } else if ("2".equals(status)) {
                //已派单
                ll_ypd.setVisibility(View.VISIBLE);
                ll_master_container.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
            } else if ("3".equals(status)) {
                //服务中/待付款
                ll_fwz.setVisibility(View.VISIBLE);
                ll_master_container.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.GONE);
            } else if ("4".equals(status)) {
                //        //待评价
                ll_dpj.setVisibility(View.VISIBLE);
                tv_pingjia.setVisibility(View.VISIBLE);
                tv_tousu.setVisibility(View.VISIBLE);
            } else if ("5".equals(status)) {
                //完成/已评价
                ll_wc.setVisibility(View.VISIBLE);
                ll_dpj.setVisibility(View.GONE);
                tv_tousu.setVisibility(View.VISIBLE);
                tv_pingjia.setVisibility(View.GONE);
            }else if ("5".equals(status)){
                XToast.makeText(mContext, "订单已取消", XToast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void initListener() {
        ll_order_detail_title.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        iv_phone_icon.setOnClickListener(this);
        tv_pingjia.setOnClickListener(this);
        tv_tousu.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initIntentData() {
        order_id = getIntent().getStringExtra("order_id");
    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onClick(View v) {
        if (model==null){
            return;
        }
        switch (v.getId()) {

            case R.id.tv_cancel:
                //取消订单
                Intent intent = new Intent(this, ServiceOrderDetailCommonActivity.class);
                intent.putExtra("type",0);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
                break;
            case R.id.iv_phone_icon:
                // 打电话
                if (model==null){
                    return;
                }
                // 联系商家
                new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"
                                    + model.getMobile()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }

                    }
                }).show();
//                // 测试支付
//                Intent intent4 = new Intent(this, ZhifuActivity.class);
//                intent4.putExtra("oid",order_id);
//                intent4.putExtra("type","service_new_pay");
//                intent4.putExtra("order_type","se");
//                intent4.putExtra("price","0.01");
//                startActivity(intent4);
                break;
            case R.id.tv_pingjia:
                //评价
                Intent intent2 = new Intent(this, ServiceOrderDetailCommonActivity.class);
                intent2.putExtra("type",2);
                intent2.putExtra("order_id",order_id);
                startActivity(intent2);
                break;
            case R.id.tv_tousu:
                //投诉
                Intent intent1 = new Intent(this, ServiceOrderDetailCommonActivity.class);
                intent1.putExtra("type",1);
                intent1.putExtra("order_id",order_id);
                startActivity(intent1);
                break;
            case R.id.ll_order_detail_title:
                //服务详情
                Intent  intent3 = new Intent(this, ServiceDetailActivity.class);
                intent3.putExtra("service_id", model.getS_id());
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOrderList(ModelOrderList modelOrderList){
        if (modelOrderList!=null){
            if (modelOrderList.getEvent_type()==0){
                //取消订单
                finish();
            }else if (modelOrderList.getEvent_type()==1) {
                //评价完成
                initData();
            }

        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
