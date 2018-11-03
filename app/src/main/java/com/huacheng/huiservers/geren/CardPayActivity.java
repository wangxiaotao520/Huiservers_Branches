package com.huacheng.huiservers.geren;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.facepay.FacepayIndexActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.CardPayBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.wuye.ConfirmPropertyOrderActivity;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.socialize.sina.helper.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/12/21.
 */

public class CardPayActivity extends BaseUI implements View.OnClickListener {

    TextView title_name;
    LinearLayout lin_left;
    EditText et_cardno, et_getcode;
    TextView txt_getcode, txt_enter_pay;
    String oid = "";
    String order_type = "";
    String price = "";
    String type = "";
    ShopProtocol protocol2 = new ShopProtocol();
    String key;
    Message msg;
    String cid = "";
    private MyCount countDownTimer;
    private String timestamp;//获取验证码加密字符串
    private String jmStr;//获取验证码加密字符串

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.cardpay);
  //      SetTransStatus.GetStatus(this);// 系统栏默认为黑色
        title_name = (TextView) findViewById(R.id.title_name);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        et_cardno = (EditText) findViewById(R.id.et_cardno);
        et_getcode = (EditText) findViewById(R.id.et_getcode);
        txt_getcode = (TextView) findViewById(R.id.txt_getcode);
        txt_enter_pay = (TextView) findViewById(R.id.txt_enter_pay);
        //
        title_name.setText("一卡通支付");
        oid = getIntent().getExtras().getString("oid");
        order_type = getIntent().getExtras().getString("order_type");
        price = getIntent().getExtras().getString("price");
        type = getIntent().getExtras().getString("type");

        lin_left.setOnClickListener(this);
        txt_getcode.setOnClickListener(this);
        txt_enter_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                UIUtils.closeInputMethod(CardPayActivity.this, et_getcode);
                if (ConfirmPropertyOrderActivity.staticConfirm != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("typeCallback", type);
                    bundle.putString("o_idCallback", oid);
                    intent.putExtras(bundle);
                    setResult(102, intent);
                }
                if (FacepayIndexActivity.sFacePayIndex != null) {
                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
                    setResult(9999, intent);

                    /*Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("typeCallback", type);
                    bundle.putString("o_idCallback", oid);
                    intent.putExtras(bundle);
                    setResult(998, intent);*/
                }
                finish();

                break;
            case R.id.txt_getcode:
                if (et_cardno.getText().toString().equals("")) {
                    XToast.makeText(this, "请输入卡号", XToast.LENGTH_SHORT).show();
                } else {
                    // 获取当前网络时间戳//耗时操作 需放在子线程中
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            URL url = null;//取得资源对象
                            try {
                                url = new URL("http://www.baidu.com");
                                URLConnection uc = url.openConnection();//生成连接对象
                                uc.connect(); //发出连接
                                long ld = uc.getDate() / 1000; //取得网站日期时间、

                                timestamp = String.valueOf(ld);
                                //获取加密字符串
                                String ttttt = MD5.hexdigest("hui-shenghuo.api_sms").substring(8, 24);
                                jmStr = MD5.hexdigest(timestamp + ttttt + timestamp).substring(0, 16);
                                //getCode();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getCode();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("timestamp------" + timestamp);
                        }
                    }).start();
                }

                break;
            case R.id.txt_enter_pay:
                if (et_cardno.getText().toString().equals("")) {
                    XToast.makeText(this, "请输入卡号", XToast.LENGTH_SHORT).show();
                } else if (et_getcode.getText().toString().equals("")) {
                    XToast.makeText(this, "请输入验证码", XToast.LENGTH_SHORT).show();
                } else {
                    getPayInfo();
//                    XToast.makeText(this, "Stop", XToast.LENGTH_SHORT);
                }
                break;

        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    countDownTimer = new MyCount(60000, 1000);
                    countDownTimer.start();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onDestroy() {
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
        super.onDestroy();
    }

    /**
     * 获取验证码
     */
    public void getCode() {
        showDialog(smallDialog);
        String url = "";
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", oid);
        params.addBodyParameter("typename", "hcpay");
        params.addBodyParameter("cardno", et_cardno.getText().toString());// "600000011730"
        params.addBodyParameter("ApiSmstokentime", timestamp);// 时间戳
        params.addBodyParameter("ApiSmstoken", jmStr);// 加密字符串
        if (type.equals("wuye") || type.equals("wuyeNew")) {
            url = info.pay_property_order;// 物业的
        } else if (type.equals("shop") || type.equals("shop_1")) {// shop_1
            // 是从购物流程一路付款成功的
            url = info.pay_shopping_order;// 购物的
        } else if (type.equals("huodong")) {
            url = info.pay_activity_order;// 活动的
        } else if (type.equals("facepay")) {
            url = info.pay_face_order;// 当面付
        } else if (type.equals("wired")) {
            url = info.pay_wired_order;// 有线缴费
        }else if ("service_new_pay".equals(type)){
            url=info.service_new_pay;//新服务支付
        }
        new HttpHelper(url, params,
                this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    String str = jsonObject.getString("status");
                    String jsonmsg = jsonObject.getString("msg");
                    CardPayBean payBean = protocol2.getHCCard(json);
                    if (payBean != null) {
                        XToast.makeText(CardPayActivity.this, jsonmsg, XToast.LENGTH_SHORT).show();
                        key = payBean.getKey();
                        msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } else {
                        XToast.makeText(CardPayActivity.this, jsonmsg, XToast.LENGTH_SHORT).show();//"获取验证码失败"
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

    private void getPayInfo() {
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("rand", et_getcode.getText().toString());
        params.addBodyParameter("key", key);
        params.addBodyParameter("cardno", et_cardno.getText().toString());//"600000011730"
        params.addBodyParameter("order_id", oid);
        params.addBodyParameter("otype", order_type);
        params.addBodyParameter("price", price);
        showDialog(smallDialog);
        new HttpHelper(info.pay_shopping_check, params,
                CardPayActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if (StringUtils.isEquals(status, "1")) {
                        UIUtils.closeInputMethod(CardPayActivity.this, et_getcode);
                        Intent intent = new Intent();
                        setResult(123, intent);
                        finish();

                    } else {
                        XToast.makeText(CardPayActivity.this, msg, XToast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            txt_getcode.setText("重新发送");
            txt_getcode.setEnabled(true);
            txt_getcode.setOnClickListener(CardPayActivity.this);
//            txt_btn.setText("登录");
            /*txt_btn.setTextColor(getResources().getColor(R.color.white));
            txt_btn.setBackground(getResources().getDrawable(R.drawable.corners_bg_red));
            txt_btn.setEnabled(true);*/
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished / 1000 >= 1) {
                txt_getcode.setEnabled(false);
            }
            txt_getcode.setTextColor(getResources().getColor(R.color.black2new));
            txt_getcode.setBackground(getResources().getDrawable(R.drawable.cornes_bgwhite_edgegray2));
           /* txt_btn.setTextColor(getResources().getColor(R.color.white));
            txt_btn.setBackground(getResources().getDrawable(R.drawable.corners_bg_red));
            txt_btn.setEnabled(true);*/
            txt_getcode.setText(millisUntilFinished / 1000 + "秒后重发");
        }
    }

}
