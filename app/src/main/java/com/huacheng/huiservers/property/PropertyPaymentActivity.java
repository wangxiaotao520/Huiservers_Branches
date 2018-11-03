package com.huacheng.huiservers.property;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.adapter.PropertyPaymentAdapter;
import com.huacheng.huiservers.property.bean.PropertyInfo1;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：property 订单缴费记录
 * 时间：2018/8/4 10:49
 * created by DFF
 */
public class PropertyPaymentActivity extends BaseActivity {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.list)
    ListView mList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rel_no_data)
    RelativeLayout mRelNoData;

    PropertyPaymentAdapter paymentAdapter;
    List<PropertyInfo1> mdatas = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTitleName.setText("缴费记录");

        paymentAdapter = new PropertyPaymentAdapter(this, mdatas);
        mList.setAdapter(paymentAdapter);
    }

    @Override
    protected void initData() {
        getPaymentList();
    }

    @Override
    protected void initListener() {
        mLinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.property_payment_record;
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

    private void getPaymentList() {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        MyOkHttp.get().post(ApiHttpClient.GET_USER_BILL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<PropertyInfo1> mlist = JsonUtil.getInstance().getDataArrayByName(response, "data", PropertyInfo1.class);
                    if (mlist != null && mlist.size() > 0) {
                        mRelNoData.setVisibility(View.GONE);
                        mList.setVisibility(View.VISIBLE);
                        mdatas.clear();
                        mdatas.addAll(mlist);
                        paymentAdapter.notifyDataSetChanged();
                    } else {
                        mdatas.clear();
                        mRelNoData.setVisibility(View.VISIBLE);
                        mList.setVisibility(View.GONE);
                    }
                } else {
                    mdatas.clear();
                    mRelNoData.setVisibility(View.VISIBLE);
                    mList.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyPaymentActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
