package com.huacheng.huiservers.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.ZhifuActivity;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.protocol.WuYeProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.wuye.bean.ProperyGetOrderBean;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmPropertyOrderActivity extends BaseUI implements OnClickListener {


    private LinearLayout lin_left;
    private TextView title_name, txt_property_index, txt_property_balance, tv_property_pay, tvTypeBalanceTxt;
    private EditText et_property_amount;
    private ShopProtocol protocol = new ShopProtocol();

    HouseBean water, elec;
    String type;
    HouseBean roomBill;

    WuYeBean wuye;
    String billName = "";
    public static ConfirmPropertyOrderActivity staticConfirm;

    @Override
    protected void init() {
        super.init();
        staticConfirm = this;
        setContentView(R.layout.confirm_property);
 //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        title_name = (TextView) findViewById(R.id.title_name);

        tvTypeBalanceTxt = (TextView) findViewById(R.id.tv_typeBalance_txt);
        txt_property_index = (TextView) findViewById(R.id.txt_property_index);
        txt_property_balance = (TextView) findViewById(R.id.txt_property_balance);
        et_property_amount = (EditText) findViewById(R.id.et_property_amount);
        //底部栏
        tv_property_pay = (TextView) findViewById(R.id.tv_property_pay);
        lin_left.setOnClickListener(this);
        tv_property_pay.setOnClickListener(this);
        initA();
        getPropertyInfo();
    }


    ShopProtocol protocol2 = new ShopProtocol();

    public void initA() {
        billName = getIntent().getStringExtra("name");
        if (!StringUtils.isEmpty(billName)) {
            title_name.setText(billName + "缴费");
            tvTypeBalanceTxt.setText(billName + "余额");
        }
    }

    /**
     * 获取用户水/电表详情
     */
    public void getPropertyInfo() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams p = new RequestParams();

        wuye = (WuYeBean) getIntent().getSerializableExtra("room");
        if (wuye != null) {
            if (!StringUtils.isEmpty(wuye.getRoom_id())) {
                p.addBodyParameter("room_id", wuye.getRoom_id());
                new HttpHelper(info.getRoomBill, p, this) {

                    @Override
                    protected void setData(String json) {
                        hideDialog(smallDialog);
                        roomBill = new HouseProtocol().getHouseRoomBill(json);
                        if (roomBill != null) {
                            type = getIntent().getStringExtra("type");
                            switch (type) {
                                case "36864"://水
                                    water = roomBill.getShuifei();
                                    if (water != null) {
                                        HouseBean infoBean = water.getInfoBean();
                                        if (infoBean != null) {
                                            txt_property_index.setText("水表数字：" + infoBean.getTotal());
                                            txt_property_balance.setText("余额：" + infoBean.getSMay_acc() + "元");
                                        }
                                    }
                                    break;
                                case "36865"://电
                                    elec = roomBill.getDianfei();
                                    if (elec != null) {
                                        HouseBean infoBean = elec.getInfoBean();
                                        if (infoBean != null) {
                                            txt_property_index.setText("电表数字：" + infoBean.getTotal());
                                            txt_property_balance.setText("余额：" + infoBean.getDMay_acc() + "元");
                                        }
                                    }
                                    break;
                                default:
                                    break;
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

        }

    }

    private void waterEle(String typebk, String oidbk) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", oidbk);
        if (typebk.equals("wuye")) {
            params.addBodyParameter("type", "property");// 物业的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("shop") || typebk.equals("shop_1")) {// shop_1
            // 是从购物流程一路付款成功的
            params.addBodyParameter("type", "shop");// 购物的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("Yweixiu")) {
            params.addBodyParameter("type", "service");// 维修预付款的
            params.addBodyParameter("prepay", "1");
        } else if (typebk.equals("weixiu")) {
            params.addBodyParameter("type", "service");// 维修尾款的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("huodong")) {
            params.addBodyParameter("type", "activity");// 活動
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("facepay")) {
            params.addBodyParameter("type", "face");// 当面付
            params.addBodyParameter("prepay", "0");
        }
        new HttpHelper(info.confirm_order_payment, params,
                this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String str = protocol2.setShop(json);
                if (str.equals("1")) {
                    XToast.makeText(ConfirmPropertyOrderActivity.this, str,
                            XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(ConfirmPropertyOrderActivity.this, str,
                            XToast.LENGTH_SHORT).show();
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
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left://返回
                closeInputMethod();
                finish();
                break;
            case R.id.tv_property_pay://立即支付
                String amout = et_property_amount.getText().toString();
                if (amout.equals("")) {
                    XToast.makeText(this, "请输入正确金额", XToast.LENGTH_SHORT).show();
                }

                /*else if (NumberValidationUtils.isNumber3(amout)) {
                    XToast.makeText(this, "金额开头不能是小数格式", XToast.LENGTH_SHORT).show();
                } else if (NumberValidationUtils.isNumber4(amout)) {
                    XToast.makeText(this, "金额尾数不能是小数格式", XToast.LENGTH_SHORT).show();
                } else if (!NumberValidationUtils.isRealNumber(amout)) {
                    XToast.makeText(this, "请输入正确金额", XToast.LENGTH_SHORT).show();
                } else if (!NumberValidationUtils.judgeOneDecimal(amout)) {
                    XToast.makeText(this, "金额仅能输入一位小数格式", XToast.LENGTH_SHORT).show();
                }*/


                else if (water != null) {
                    String upperLimit = water.getUpper_limit();
                    HouseBean infoBean = water.getInfoBean();
                    if (infoBean != null) {
                        String acc = infoBean.getSMay_acc();
                        double upperA_D = Double.parseDouble(upperLimit);
                        double acc_D = Double.parseDouble(acc);

                        double totalD = upperA_D - acc_D;
                        double amoutA_D = Double.parseDouble(amout);
                        if (amoutA_D <= totalD) {
                            if (wuye != null) {
                                getPropertyOrderconfirm(wuye);//立即生成支付订单
                            } else {
                                XToast.makeText(this, "物业数据异常", XToast.LENGTH_SHORT).show();
                            }

                        } else {
                            XToast.makeText(this, "您剩余缴费金额上限为：" + (upperA_D - acc_D) + "元", XToast.LENGTH_SHORT).show();
                        }

                    } else {
                        XToast.makeText(this, "数据异常", XToast.LENGTH_SHORT).show();
                    }
                } else if (elec != null) {
                    String upperLimit = elec.getUpper_limit();
                    HouseBean infoBean = elec.getInfoBean();
                    if (infoBean != null) {
                        String acc = infoBean.getDMay_acc();
                        double upperA_D = Double.parseDouble(upperLimit);
                        double acc_D = Double.parseDouble(acc);

                        double totalD = upperA_D - acc_D;
                        double amoutA_D = Double.parseDouble(amout);
                        if (amoutA_D <= totalD) {
                            if (wuye != null) {
                                getPropertyOrderconfirm(wuye);//立即生成支付订单
                            } else {
                                XToast.makeText(this, "物业数据异常", XToast.LENGTH_SHORT).show();
                            }
                        } else {
                            XToast.makeText(this, "您剩余缴费金额上限为：" + (upperA_D - acc_D) + "元", XToast.LENGTH_SHORT).show();
                        }

                    } else {
                        XToast.makeText(this, "数据异常", XToast.LENGTH_SHORT).show();
                    }
                } else {
                    if (wuye != null) {
//                        XToast.makeText(this, "OK", XToast.LENGTH_SHORT).show();
                        getPropertyOrderconfirm(wuye);//立即生成支付订单

                    }
                }

                break;

            default:
                break;
        }
    }

    private void getPropertyOrderconfirm(final WuYeBean wuye) {//立即支付
        showDialog(smallDialog);
        final String amoutStr = et_property_amount.getText().toString();
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", wuye.getRoom_id());
        params.addBodyParameter("type", type);
        params.addBodyParameter("amount", amoutStr);
        params.addBodyParameter("community_id", wuye.getCommunity_id());
        params.addBodyParameter("community_name", wuye.getCommunity_name());
        params.addBodyParameter("building_name", wuye.getBuilding_name());
        params.addBodyParameter("unit", wuye.getUnit());
        params.addBodyParameter("code", wuye.getCode());
        new HttpHelper(info.property_createorder, params, ConfirmPropertyOrderActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
//                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");
//                    WuYeBean wuy= wuye;
                    if (status.equals("1")) {
                        UIUtils.closeInputMethod(ConfirmPropertyOrderActivity.this, et_property_amount);

                        ProperyGetOrderBean getOrder = new WuYeProtocol().getProperyGetOrder(json);
                        if (getOrder != null) {
                            et_property_amount.setText("");
                            Intent intent = new Intent(ConfirmPropertyOrderActivity.this, ZhifuActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("o_id", getOrder.getOid());
                            bundle.putString("price", amoutStr);//et_property_amount.getText().toString()
                            bundle.putString("type", "wuye");
                            bundle.putString("order_type", "wy");
                          /*  bundle.putSerializable("room", wuye);
                            bundle.putString("billName", billName);
                            bundle.putString("waterEle", type);*/
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 111);

                        }
                    } else {
                        XToast.makeText(ConfirmPropertyOrderActivity.this, msg, XToast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 110) {
                String typeCB = data.getExtras().getString("typeCallback");
                String o_idCB = data.getExtras().getString("o_idCallback");
                if (!StringUtils.isEmpty(typeCB) && !StringUtils.isEmpty(o_idCB)) {
                    waterEle(typeCB, o_idCB);
                }
            }
        }
    }

    /**
     * 关闭软键盘
     */
    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

            imm.hideSoftInputFromWindow(et_property_amount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
