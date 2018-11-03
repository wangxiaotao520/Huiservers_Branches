package com.huacheng.huiservers.servicenew.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.New_AddressActivity;
import com.huacheng.huiservers.geren.bean.AddressBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ServiceProtocol;
import com.huacheng.huiservers.servicenew.model.ModelServiceDetail;
import com.huacheng.huiservers.servicenew.ui.dialog.ServiceSuccessDialog;
import com.huacheng.huiservers.servicenew.ui.order.FragmentOrderListActivity;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类描述：服务确认下单
 * 时间：2018/9/5 09:36
 * created by DFF
 */
public class ServiceConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.tv_service_name)
    TextView mTvServiceName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.ly_address)
    LinearLayout mLyAddress;
    @BindView(R.id.tv_center)
    TextView mTvCenter;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_btn)
    TextView mTvBtn;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.ly_noaddress)
    LinearLayout mLyNoaddress;

    String tag_id, tag_name, tag_price, service_id, service_name, AddressID;
    SharePrefrenceUtil sharePrefrenceUtil;


    @Override
    protected void initView() {
        ButterKnife.bind(this);

        sharePrefrenceUtil = new SharePrefrenceUtil(this);

        mTitleName.setText("确认服务信息");
        mTvServiceName.setText(service_name);
        mTvPrice.setText("¥" + tag_price);
        mEditContent.addTextChangedListener(mTextWatcher);
        titleView();


    }

    private void titleView() {
        View view1 = findViewById(R.id.view1);
        TextView textView1 = view1.findViewById(R.id.tv_center);
        textView1.setText("服务项目");
        View view2 = findViewById(R.id.view2);
        TextView textView2 = view2.findViewById(R.id.tv_center);
        textView2.setText("服务地址");
        View view3 = findViewById(R.id.view3);
        TextView textView3 = view3.findViewById(R.id.tv_center);
        textView3.setText("备注");

    }

    @Override
    protected void initData() {
        getAddress();
    }

    private void getAddress() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", sharePrefrenceUtil.getXiaoQuId());//
        new HttpHelper(info.submit_repair_before, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                AddressBean address = new ServiceProtocol().getAddress(json);
                if (address != null) {
                    if (TextUtils.isEmpty(address.getAddress_id())) {
                        mLyAddress.setVisibility(View.GONE);
                        mLyNoaddress.setVisibility(View.VISIBLE);
                    } else {
                        mLyAddress.setVisibility(View.VISIBLE);
                        mLyNoaddress.setVisibility(View.GONE);

                        mTvAddress.setText(address.getAddress());
                        AddressID = address.getAddress_id();
                        mTvName.setText(address.getContact());
                        mTvPhone.setText(address.getMobile());
                    }
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
    protected void initListener() {
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new ToolUtils(mEditContent, ServiceConfirmOrderActivity.this).closeInputMethod();
                return false;
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_confirm;
    }

    @Override
    protected void initIntentData() {

        service_id = getIntent().getStringExtra("service_id");
        service_name = getIntent().getStringExtra("service_name");
        tag_id = getIntent().getStringExtra("tag_id");
        tag_name = getIntent().getStringExtra("tag_name");
        tag_price = getIntent().getStringExtra("tag_price");


    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.lin_left, R.id.ly_address, R.id.tv_btn, R.id.ly_noaddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                new ToolUtils(mEditContent, ServiceConfirmOrderActivity.this).closeInputMethod();
                finish();
                break;
            case R.id.ly_address:
            case R.id.ly_noaddress:
                Intent intent = new Intent(this, New_AddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address", "serviceDan");//shopyes
                bundle.putString("type", "order");
                intent.putExtras(bundle);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_btn:
                if (mLyNoaddress.getVisibility() == View.VISIBLE) {
                    XToast.makeText(this, "地址不能为空", XToast.LENGTH_SHORT).show();
                    return;
                }
                getsubmit();

                break;
        }
    }

    String order_id;

    private void getsubmit() {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("s_id", service_id);
        params.put("s_tag_id", tag_id);
        params.put("s_tag_cn", tag_name);
        params.put("price", tag_price);
        params.put("address_id", AddressID);
        params.put("address", mTvAddress.getText().toString().trim());
        params.put("contacts", mTvName.getText().toString().trim());
        params.put("mobile", mTvPhone.getText().toString().trim());
        params.put("description", mEditContent.getText().toString().trim());

        MyOkHttp.get().post(ApiHttpClient.GET_SSERVICE_RESERVE, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(final int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    ModelServiceDetail mModelOrdetDetail = (ModelServiceDetail) JsonUtil.getInstance().parseJsonFromResponse(response, ModelServiceDetail.class);
                    order_id = mModelOrdetDetail.getId();
                    //下单成功后请求接口给机构端推送订单信息`
                    setpush(order_id);
                    new ServiceSuccessDialog(mContext, R.style.my_dialog_DimEnabled, new ServiceSuccessDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, String str) {

                            Intent intent = new Intent(mContext, FragmentOrderListActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    }).show();

                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(ServiceConfirmOrderActivity.this, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(ServiceConfirmOrderActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 给机构端推送订单信息`
     */
    private void setpush(String order_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", order_id);
        MyOkHttp.get().post(ApiHttpClient.PUSH_INS, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 200://地址返回
                String person_name = data.getExtras().getString("person_name");
                String person_mobile = data.getExtras().getString("person_mobile");
                String person_address = data.getExtras().getString("address");
                AddressID = data.getExtras().getString("address_id");
                if (!person_address.equals("")) {
                    mLyAddress.setVisibility(View.VISIBLE);
                    mLyNoaddress.setVisibility(View.GONE);
                    mTvPhone.setText(person_mobile);
                    mTvName.setText(person_name);
                    mTvAddress.setText(person_address);
                }
                break;
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            // mTvNum.setText(s);
            mTvNum.setText(s.length() + "/150");
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = mEditContent.getSelectionStart();
            editEnd = mEditContent.getSelectionEnd();
            if (temp.length() > 150) {

                s.delete(editStart - 1, editEnd);
                mEditContent.setText(s);
                //mTvNum.setText(s.length() + "/150");
                mEditContent.setSelection(s.length());
            }
        }
    };

}
