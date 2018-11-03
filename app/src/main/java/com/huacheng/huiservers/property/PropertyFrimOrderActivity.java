package com.huacheng.huiservers.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.ZhifuActivity;
import com.huacheng.huiservers.property.adapter.PropertyWYInfo1Adapter;
import com.huacheng.huiservers.property.bean.EventProperty;
import com.huacheng.huiservers.property.bean.PropertyInfo1;
import com.huacheng.huiservers.property.bean.WuyeListBean;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.base.BaseActivity;
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
 * 类描述：property 物业确认订单
 * 时间：2018/8/3 19:13
 * created by DFF
 */
public class PropertyFrimOrderActivity extends BaseActivity {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_pay_time)
    TextView mTvPayTime;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.txt_btn)
    TextView mTxtBtn;
    @BindView(R.id.list)
    MyListView mList;

    String oid;
    private String bill_id, room_id;
    PropertyWYInfo1Adapter wyInfoAdapter;
    List<List<WuyeListBean>> wyListData = new ArrayList<>();
    PropertyInfo1 propertyInfo;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTitleName.setText("确认订单支付");

        wyInfoAdapter = new PropertyWYInfo1Adapter(this, wyListData);
        mList.setAdapter(wyInfoAdapter);


    }

    @Override
    protected void initData() {
        room_id = this.getIntent().getExtras().getString("room_id");
        bill_id = this.getIntent().getExtras().getString("bill_id");
        getOrderInfo();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.property_frim_order;
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

    private void getOrderInfo() {
        Url_info info = new Url_info(this);
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("room_id", room_id);
        params.put("bill_id", bill_id);

        MyOkHttp.get().post(info.make_property_order, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    propertyInfo = (PropertyInfo1) JsonUtil.getInstance().parseJsonFromResponse(response, PropertyInfo1.class);
                    if (propertyInfo != null) {

                        mTvName.setText(propertyInfo.getNickname());
                        mTvAddress.setText("地址：" + propertyInfo.getAddress());
                        mTvOrderNumber.setText("订单编号：" + propertyInfo.getOrder_num());
                        mTvPayTime.setText("支付时间：" + StringUtils.getDateToString(propertyInfo.getAddtime(), "1"));
                        mTvAmount.setText("¥ " + propertyInfo.getTot_sumvalue());
                        if (propertyInfo.getList() != null && propertyInfo.getList().size() > 0) {
                            List<List<WuyeListBean>> wyList = propertyInfo.getList();
                            for (int i = 0; i < wyList.size(); i++) {
                                for (int i1 = 0; i1 < wyList.get(i).size(); i1++) {
                                    oid = wyList.get(i).get(i1).getOid();
                                }
                            }

                            wyListData.clear();
                            wyListData.addAll(wyList);
                            wyInfoAdapter.notifyDataSetChanged();
                        } else {
                            wyListData.clear();
                            if (wyInfoAdapter != null) {
                                wyInfoAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                } else {
                    XToast.makeText(PropertyFrimOrderActivity.this, "获取失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyFrimOrderActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.lin_left, R.id.txt_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_btn:
                Intent intent = new Intent(PropertyFrimOrderActivity.this, ZhifuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("o_id", oid);
                bundle.putString("price", propertyInfo.getTot_sumvalue() + "");
                bundle.putString("type", "wuyeNew");
                bundle.putString("order_type", "wy");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


}
