package com.huacheng.huiservers.property;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.property.adapter.NewPropertyAdapter;
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
 * 类描述：新增缴费
 * 时间：2018/8/4 15:49
 * created by DFF
 */
public class PropertyNewActivity extends BaseActivity {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.ry_add)
    RelativeLayout mRyAdd;
    NewPropertyAdapter propertyAdapter;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.lin_right)
    LinearLayout mLinRight;
    @BindView(R.id.list)
    MyListView mList;

    List<HouseBean> mdatas = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTitleName.setText("我的房屋");
        mIvRight.setBackground(getResources().getDrawable(R.drawable.ic_order5));
        mLinRight.setVisibility(View.VISIBLE);
        propertyAdapter = new NewPropertyAdapter(this, mdatas);
        mList.setAdapter(propertyAdapter);

    }

    @Override
    protected void initData() {
        getHouseList();

    }

    private void getHouseList() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HashMap<String, String> params = new HashMap<>();
        MyOkHttp.get().post(info.binding_community, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<HouseBean> mlist = JsonUtil.getInstance().getDataArrayByName(response, "data", HouseBean.class);
                    if (mlist != null && mlist.size() > 0) {
                        mList.setVisibility(View.VISIBLE);
                        mdatas.clear();
                        mdatas.addAll(mlist);
                        propertyAdapter.notifyDataSetChanged();
                    } else {
                        mdatas.clear();
                        mList.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyNewActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void initListener() {

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (mdatas.get(position).getIs_ym().equals("0")) {
                    intent = new Intent(PropertyNewActivity.this, PropertyHomeListActivity.class);
                } else {
                    intent = new Intent(PropertyNewActivity.this, PropertyHomeNewJFActivity.class);
                }
                intent.putExtra("room_id", mdatas.get(position).getRoom_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.new_property;
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

    //绑定成功刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BDCallback(HouseBean info) {
        HouseBean houseBean = new HouseBean();
        if (info != null) {
         //   houseBean.setAddress(info.getBuilding_name() + "号楼" + info.getUnit() + "单元" + info.getCode());
            houseBean.setAddress(info.getAddress()+"");
            houseBean.setCommunity_name(info.getCommunity_name());
            houseBean.setCommunity_id(info.getCommunity_id());
            houseBean.setIs_ym(info.getIs_ym());
            houseBean.setRoom_id(info.getRoom_id());
            boolean is_contain = false;
            for (int i = 0; i < mdatas.size(); i++) {
                String community_id = mdatas.get(i).getCommunity_id();
                if (community_id.equals(houseBean.getCommunity_id())) {
                    is_contain = true;
                }
            }
            if (!is_contain) {
                mdatas.add(0, houseBean);

            }
            propertyAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.lin_left, R.id.iv_right, R.id.ry_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.iv_right:

                startActivity(new Intent(PropertyNewActivity.this, PropertyPaymentActivity.class));

                break;
            case R.id.ry_add:
                Intent intent=new Intent();
            //    startActivity(new Intent(PropertyNewActivity.this, PropertyMyHomeActivity.class));
                startActivity(new Intent(PropertyNewActivity.this, PropertyBindHomeActivity.class));
                break;
        }
    }
}
