package com.huacheng.huiservers.servicenew.ui.order;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderDetailCommon;
import com.huacheng.huiservers.servicenew.model.ModelOrderList;
import com.huacheng.huiservers.servicenew.ui.adapter.OrderDetailCommonAdapter;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:取消订单，举报投诉，评价
 * created by wangxiaotao
 * 2018/9/5 0005 下午 3:56
 */
public class ServiceOrderDetailCommonActivity extends BaseActivity{
    private  int type=0;//0.取消订单1投诉举报，用户评价
    private XLHRatingBar ratingBar;
    private ListView list_view;
    private EditText et_live_content;
    private TextView tv_text_count;
    private TextView tv_btn;
    private TextView tv_pingjia_intro;
    List<ModelOrderDetailCommon> mDatas =new ArrayList<>();
    private OrderDetailCommonAdapter<ModelOrderDetailCommon> adapter;
    private ModelOrderDetailCommon model_seleced;
    private String order_id;
    private LinearLayout ll_no_name;
    private ImageView iv_check;
    private boolean is_check_noname;

    @Override
    protected void initIntentData() {
        type = getIntent().getIntExtra("type", 0);
        order_id = getIntent().getStringExtra("order_id");
    }

    @Override
    protected void initView() {
        findTitleViews();
        if (type==0){
            titleName.setText("取消订单");
        }else if (type==1){
            titleName.setText("投诉举报");
        }else {
            titleName.setText("发表评价");
        }
        FrameLayout fl_icon_container = findViewById(R.id.fl_icon_container);
        ImageView iv_cancel_reason = findViewById(R.id.iv_cancel_reason);
        ImageView iv_jubao = findViewById(R.id.iv_jubao);
        LinearLayout ll_pingjia_container = findViewById(R.id.ll_pingjia_container);
        ratingBar = findViewById(R.id.ratingBar);
        tv_pingjia_intro = findViewById(R.id.tv_pingjia_intro);
        ImageView iv_other_icon = findViewById(R.id.iv_other_icon);
        et_live_content = findViewById(R.id.et_live_content);
        tv_text_count = findViewById(R.id.tv_text_count);
        tv_btn = findViewById(R.id.tv_btn);
        list_view = findViewById(R.id.list_view);
        ll_no_name=findViewById(R.id.ll_no_name);
        iv_check=findViewById(R.id.iv_check);
        if (type==0){
            ll_pingjia_container.setVisibility(View.GONE);
            fl_icon_container.setVisibility(View.VISIBLE);
            iv_cancel_reason.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.VISIBLE);
            iv_other_icon.setVisibility(View.VISIBLE);
            tv_btn.setText("取消订单");
        }else if (type==1){
            ll_pingjia_container.setVisibility(View.GONE);
            fl_icon_container.setVisibility(View.VISIBLE);
            iv_jubao.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.VISIBLE);
            iv_other_icon.setVisibility(View.VISIBLE);
            tv_btn.setText("提交投诉");
        }else {
            ll_pingjia_container.setVisibility(View.VISIBLE);
            fl_icon_container.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.GONE);
            iv_other_icon.setVisibility(View.GONE);
            ll_no_name.setVisibility(View.VISIBLE);
            tv_btn.setText("发布评价");
        }
        ratingBar.setCountSelected(5);
        tv_pingjia_intro.setText("力荐");

        adapter = new OrderDetailCommonAdapter<>(this, mDatas);
        list_view.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        if (type==0||type==1){
            requestAbortData();
        }

    }

    /**
     * 请求取消原因,投诉原因接口
     */
    private void requestAbortData() {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        if (type==0){
            params.put("c_alias","HC_cancel");
        }else if (type==1){
            params.put("c_alias","HC_report");
        }
        MyOkHttp.get().post(ApiHttpClient.ABORT_LIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)){
                    List <ModelOrderDetailCommon>data = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelOrderDetailCommon.class);
                    mDatas.clear();
                    mDatas.addAll(data);
                    adapter.notifyDataSetChanged();
                }else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response,"获取数据失败");
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

    @Override
    protected void initListener() {
        ratingBar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                if (countSelected==1){
                    tv_pingjia_intro.setText("很差");
                }else if (countSelected==2){
                    tv_pingjia_intro.setText("较差");
                }else if (countSelected==3){
                    tv_pingjia_intro.setText("还行");
                }else if (countSelected==4){
                    tv_pingjia_intro.setText("推荐");
                }else if (countSelected==5){
                    tv_pingjia_intro.setText("力荐");
                }
            }
        });
        et_live_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s!=null){
                    int length = s.length();
                    tv_text_count.setText(length+"/150");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交
                if (type==0){
                    String content = et_live_content.getText().toString().trim();
                    if (model_seleced==null&&NullUtil.isStringEmpty(content)) {
                        XToast.makeText(mContext, "取消原因不可为空", XToast.LENGTH_SHORT).show();
                       // ToastUtils.show(mContext,"取消原因不可为空", Toast.LENGTH_SHORT);
                        return;
                    }

                    commitCancel(content);
                }else if (type==1){
                    String content = et_live_content.getText().toString().trim();
                    if (model_seleced==null&&NullUtil.isStringEmpty(content)) {
                        XToast.makeText(mContext, "取消原因不可为空", XToast.LENGTH_SHORT).show();
                      //  ToastUtils.show(mContext,"举报原因不可为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    commitJubao(content);
                }else {
                    String content = et_live_content.getText().toString().trim();
                    if (NullUtil.isStringEmpty(content)){
                        XToast.makeText(mContext, "内容不可为空", XToast.LENGTH_SHORT).show();
                     //   ToastUtils.show(mContext,"内容不可为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    commitPingJia(content);
                }
            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((int)id==-1){
                    return;
                }
                //
                for (int i = 0; i < mDatas.size(); i++) {
                    ModelOrderDetailCommon modelOrderDetailCommon = mDatas.get(i);
                    if ((int) id==i) {
                        //选中状态
                       if (!modelOrderDetailCommon.isSelected()){
                           modelOrderDetailCommon.setSelected(true);
                       }
                        model_seleced=modelOrderDetailCommon;
                    }else {
                        modelOrderDetailCommon.setSelected(false);
                    }
                }
                if (adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
       ll_no_name.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!is_check_noname){
                   is_check_noname=true;
                   iv_check.setBackgroundResource(R.mipmap.ic_order_detail_check);
               }else {
                   is_check_noname=false;
                   iv_check.setBackgroundResource(R.mipmap.ic_order_detail_uncheck);
               }
           }
       });
    }

    /**
     * 提交评价
     * @param content
     */
    private void commitPingJia(final String content) {

        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", order_id);
        params.put("score",ratingBar.getCountSelected()+"");
        params.put("evaluate",content);
        // 是否匿名
        if (is_check_noname){
            params.put("anonymous","1");
        }else {
            params.put("anonymous","2");
        }
        MyOkHttp.get().post(ApiHttpClient.COMMIT_PINGJIA, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "评价成功");
                    ToastUtils.show(mContext,msg, Toast.LENGTH_SHORT);

                    // 评价成功后也得发eventbus,把服务列表页和详情页的状态改一下
                    ModelOrderList modelOrderList = new ModelOrderList();
                    modelOrderList.setEvent_type(1);
                    modelOrderList.setId(order_id);
                    modelOrderList.setEvaluatime((long)(System.currentTimeMillis()*1f/1000)+"");
                    if (NullUtil.isStringEmpty(content)){
                        modelOrderList.setEvaluate("");
                    }else {
                        modelOrderList.setEvaluate(content);
                    }
                    modelOrderList.setScore(ratingBar.getCountSelected()+"");
                    finish();
                    EventBus.getDefault().post(modelOrderList);
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "评价失败");
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
     * 提交举报
     * @param content
     */
    private void commitJubao(String content) {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", order_id);
        if (model_seleced!=null){
            params.put("cate_id_arr", ""+model_seleced.getId());
        }
        params.put("report_other", ""+content);
        MyOkHttp.get().post(ApiHttpClient.COMMIT_JUBAO, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "举报成功");
                    ToastUtils.show(mContext,msg, Toast.LENGTH_SHORT);
                    finish();
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "举报失败");
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
     * 提交取消
     * @param content
     */
    private void commitCancel(String content) {

        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", order_id);
        if (model_seleced!=null){
            params.put("cancel_type", model_seleced.getId()+"");
        }
        params.put("cancel_other", content);
        MyOkHttp.get().post(ApiHttpClient.COMMIT_CANCEL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "取消成功");
                    ToastUtils.show(mContext,msg, Toast.LENGTH_SHORT);
                    finish();
                    //得写 EVENTBUS
                    ModelOrderList modelOrderList = new ModelOrderList();
                    modelOrderList.setEvent_type(0);
                    modelOrderList.setId(order_id);
                    EventBus.getDefault().post(modelOrderList);
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "取消失败");
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail_common;
    }


    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }
}
