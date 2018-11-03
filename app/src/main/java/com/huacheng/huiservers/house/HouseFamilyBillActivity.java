package com.huacheng.huiservers.house;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.adapter.HouseFamilyBillWuyeAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.wuye.ConfirmPropertyOrderActivity;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/21.
 */

public class HouseFamilyBillActivity extends BaseUI {

    List<HouseBean> houseBills;
    @BindView(R.id.tv_community_name)
    TextView tvCommunityName;
    @BindView(R.id.tv_buildingUnitCode)
    TextView tvBuildingUnitCode;
    @BindView(R.id.tv_bind_num)
    TextView tvBindNum;
    @BindView(R.id.lin_mybillOrder)
    LinearLayout linMybillOrder;
    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.lin_top)
    LinearLayout linTop;

    @BindView(R.id.listview_wuye)
    MyListView listviewWuye;
    @BindView(R.id.tv_mybill_WaterPrice)
    TextView tvMybillWaterPrice;
    @BindView(R.id.tv_mybill_ElecPrice)
    TextView tvMybillElecPrice;
    @BindView(R.id.lin_wuye_bottom)
    View linWuyeBottom;
    @BindView(R.id.left)
    ImageView left;

    @BindView(R.id.lin_houseWuye)
    LinearLayout linHouseWuye;
    @BindView(R.id.lin_houseWater)
    LinearLayout linHouseWater;
    @BindView(R.id.lin_houseElec)
    LinearLayout linHouseElec;

    @BindView(R.id.lin_nodata)
    LinearLayout linNodata;
    @BindView(R.id.lin_houseWuye_txt)
    LinearLayout linHouseWuyeTxt;

    WuYeBean roomInfo;
    String room_id;
    HouseBean water;
    HouseBean elec;
    Intent intent = new Intent();
    String Is_available_cn, Is_available;

    @Override
    protected void init() {
        super.init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.house_familyebill);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);

        //跳到顶部
        linTop.setFocusable(true);
        linTop.setFocusableInTouchMode(true);
        linTop.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData1();
    }

    List<HouseBean> wuyes;

    private void initData1() {
        room_id = getIntent().getStringExtra("room_id");
        Url_info urlInfo = new Url_info(this);
        RequestParams p = new RequestParams();
        p.addBodyParameter("room_id", room_id);
        showDialog(smallDialog);
        new HttpHelper(urlInfo.getRoomBill, p, this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                HouseBean roomBill = new HouseProtocol().getHouseRoomBill(json);
                roomInfo = roomBill.getRoom_info();
                tvCommunityName.setText(roomBill.getRoom_info().getCommunity_name());
                tvBuildingUnitCode.setText(roomBill.getRoom_info().getAddress());
                tvBindNum.setText(roomBill.getRoom_info().getCount() + "人已绑定房屋");
                Is_available = roomBill.getIs_available();//根据获取的值来判断时候能跳到下一页
                Is_available_cn = roomBill.getIs_available_cn();
                if (roomBill != null) {
                    linNodata.setVisibility(View.GONE);
                    wuyes = roomBill.getWuye();
                    if (wuyes != null) {
                        linNodata.setVisibility(View.GONE);
                        if (wuyes.size() > 0) {
                            linHouseWuyeTxt.setVisibility(View.GONE);
                            linHouseWuye.setVisibility(View.VISIBLE);
                            HouseFamilyBillWuyeAdapter adapter = new HouseFamilyBillWuyeAdapter(wuyes, roomInfo, Is_available, Is_available_cn, HouseFamilyBillActivity.this);
                            listviewWuye.setAdapter(adapter);
                            linWuyeBottom.setVisibility(View.GONE);
                        } else {
                            linHouseWuye.setVisibility(View.GONE);
                            linHouseWuyeTxt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        linHouseWuye.setVisibility(View.GONE);
                        linHouseWuyeTxt.setVisibility(View.VISIBLE);
                    }

                    water = roomBill.getShuifei();
                    if (water != null) {
                        HouseBean infoBean = water.getInfoBean();
                        if (infoBean != null) {
                            linHouseWater.setVisibility(View.VISIBLE);
                            tvMybillWaterPrice.setText("余额：" + infoBean.getSMay_acc());
                        } else {
                            linHouseWater.setVisibility(View.GONE);
                        }
                    } else {
                        linHouseWater.setVisibility(View.GONE);
                    }

                    elec = roomBill.getDianfei();
                    if (elec != null) {
                        HouseBean infoBean = elec.getInfoBean();
                        if (infoBean != null) {
                            linHouseElec.setVisibility(View.VISIBLE);
                            tvMybillElecPrice.setText("余额：" + infoBean.getDMay_acc());
                        } else {
                            linHouseElec.setVisibility(View.GONE);
                        }
                    } else {
                        linHouseElec.setVisibility(View.GONE);
                    }

                } else {
                    linNodata.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }
    //}


    @OnClick({R.id.lin_houseWater, R.id.lin_houseElec, R.id.lin_mybillOrder, R.id.lin_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_houseWater:
                if (wuyes != null) {
                    XToast.makeText(this, "请您先缴物业费", XToast.LENGTH_SHORT).show();
                } else {
                    if (Is_available.equals("0")) {
                        if (water != null) {
                            intent.setClass(this, ConfirmPropertyOrderActivity.class);
                            intent.putExtra("type", water.getType());
                            intent.putExtra("type_cn", water.getType_cn());
                            intent.putExtra("name", "水费");
                            intent.putExtra("room", roomInfo);
                            startActivity(intent);
                        } else {
                            XToast.makeText(this, "水费信息为空", XToast.LENGTH_SHORT).show();
                        }
                    } else {
                        XToast.makeText(this, Is_available_cn, XToast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.lin_houseElec:
                if (wuyes != null) {
                    XToast.makeText(this, "请您先缴物业费", XToast.LENGTH_SHORT).show();
                } else {
                    if (Is_available.equals("0")) {
                        if (elec != null) {
                            intent.setClass(this, ConfirmPropertyOrderActivity.class);
                            intent.putExtra("type", elec.getType());
                            intent.putExtra("type_cn", elec.getType_cn());
                            intent.putExtra("name", "电费");
                            intent.putExtra("room", roomInfo);
                            startActivity(intent);
                        } else {
                            XToast.makeText(this, "电费信息为空", XToast.LENGTH_SHORT).show();
                        }
                    } else {
                        XToast.makeText(this, Is_available_cn, XToast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.lin_mybillOrder:
                if (Is_available.equals("0")) {
                    if (roomInfo != null) {
                        Intent intent = new Intent(this, HouseBillRecordingActivity.class);
                        intent.putExtra("room", roomInfo);
                        startActivity(intent);
                        Log.d("Bill-room_id", roomInfo.getRoom_id());
                    } else {
                        XToast.makeText(this, "Room信息为空", XToast.LENGTH_SHORT).show();
                    }
                } else {
                    XToast.makeText(this, Is_available_cn, XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_left:
                finish();
                break;
        }
    }

}
