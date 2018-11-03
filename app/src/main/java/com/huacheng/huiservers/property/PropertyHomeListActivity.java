package com.huacheng.huiservers.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.adapter.PropertyWYInfoAdapter;
import com.huacheng.huiservers.property.bean.EventProperty;
import com.huacheng.huiservers.property.bean.ModelPropertyWyInfo;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Author: badge
 * Create: 2018/8/3 19:08
 */
public class PropertyHomeListActivity extends BaseActivity {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.lsit)
    MyListView mLsit;
    @BindView(R.id.tv_house)
    TextView mTvHouse;
    @BindView(R.id.tv_verifyID)
    TextView mTvVerifyID;
    @BindView(R.id.tv_account_price)
    TextView mTvAccountPrice;
    @BindView(R.id.tv_brn)
    TextView mTvBrn;
    @BindView(R.id.rel_no_data)
    RelativeLayout mRelNoData;
    @BindView(R.id.ll_payment)
    LinearLayout llPayment;

    private StringBuilder sb_bill_ids;
    private List<String> strlist = new ArrayList<String>();
    double realPriceTotal = 0;
    String room_id;//, fullname
    PropertyWYInfoAdapter wyInfoAdapter;
    ModelPropertyWyInfo propertyInfo;
    List<ModelPropertyWyInfo.WuyeBean> wyListData = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        titleName.setText("缴费");

        titleName.setFocusable(true);
        titleName.setFocusableInTouchMode(true);
        titleName.requestFocus();


    }

    @Override
    protected void initData() {

        room_id = this.getIntent().getExtras().getString("room_id");
        getBillOrderInfo(room_id);
    }

    private void getBillOrderInfo(String room_id) {
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("room_id", room_id);

        MyOkHttp.get().post(ApiHttpClient.GET_ROOM_BILL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    propertyInfo = (ModelPropertyWyInfo) JsonUtil.getInstance().parseJsonFromResponse(response, ModelPropertyWyInfo.class);
                    if (propertyInfo != null) {
                        if (propertyInfo.getRoom_info() != null) {
                            mTvHouse.setText(propertyInfo.getRoom_info().getAddress());
                            mTvVerifyID.setText(propertyInfo.getRoom_info().getFullname());
                        }

                        List<ModelPropertyWyInfo.WuyeBean> wyList = propertyInfo.getWuye();
                        if (wyList != null && wyList.size() > 0) {
                            llPayment.setVisibility(View.VISIBLE);
                            mRelNoData.setVisibility(View.GONE);
                            mLsit.setVisibility(View.VISIBLE);

                            wyListData.clear();
                            wyListData.addAll(wyList);
                            wyInfoAdapter = new PropertyWYInfoAdapter(PropertyHomeListActivity.this, wyListData);
                            mLsit.setAdapter(wyInfoAdapter);

                            strlist.clear();
                            for (int i = 0; i < wyList.size(); i++) {
                                String str = wyList.get(i).getId();
                                String realPrice = wyList.get(i).getRealPrice();
                                double realPrice_d = Double.parseDouble(realPrice);
                                realPriceTotal += realPrice_d;
                                strlist.add(str);
                                System.out.println("str===" + str);
                                System.out.println("strlist===" + strlist.get(i));

                            }
                            sb_bill_ids = new StringBuilder();
                            for (int i = 0; i < strlist.size(); i++) {
                                if (i == 0) {
                                    sb_bill_ids.append(String.valueOf(strlist.get(i)));
                                } else {
                                    sb_bill_ids.append("," + String.valueOf(strlist.get(i)));
                                }
                            }
                            System.out.println("sb===" + sb_bill_ids.toString());

                            mTvAccountPrice.setText("¥ " + realPriceTotal + "");
                        } else {
                            llPayment.setVisibility(View.GONE);
                            mTvAccountPrice.setText("¥ 0.00");
                            mRelNoData.setVisibility(View.VISIBLE);
                            mLsit.setVisibility(View.GONE);
                        }

                    }
                } else {
                    XToast.makeText(PropertyHomeListActivity.this, "获取失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyHomeListActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.property_homelist;
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

    @OnClick({R.id.lin_left, R.id.tv_brn})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_brn:

                if (propertyInfo.getWuye() != null) {
                    Intent intent = new Intent(PropertyHomeListActivity.this, PropertyFrimOrderActivity.class);
                    intent.putExtra("room_id", room_id);
                    intent.putExtra("bill_id", sb_bill_ids.toString());
                    startActivity(intent);
                } else {
                    XToast.makeText(PropertyHomeListActivity.this, "费用已缴清", XToast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //新物业回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void WYCallback(EventProperty eventPro) {
        finish();
    }

}
