package com.huacheng.huiservers.house;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.protocol.WuYeProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.socialize.sina.helper.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 类：
 * 时间：2017/10/16 17:49
 * 功能描述:Huiservers
 */
public class MyZhuZhaiRZActivity extends BaseUI implements View.OnClickListener {
    private EditText edt_phone, edt_code;
    private TextView txt_getcode;
    private TextView title_name, txt_btn;
    public static MyZhuZhaiRZActivity instant;
    private LinearLayout lin_left;
    WuYeProtocol wuYeProtocol = new WuYeProtocol();
    List<WuYeBean> beans = new ArrayList<WuYeBean>();
    ShopProtocol protocol = new ShopProtocol();
    private Message msg;
    long ld;
    String timestamp;
    String jmStr;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {//倒计时开启
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    new MyCount(60000, 1000).start();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_zhuzhai_renzheng);
        //      SetTransStatus.GetStatus(this);//系统栏默认为黑色
        instant = this;
        title_name = (TextView) findViewById(R.id.title_name);
        txt_getcode = (TextView) findViewById(R.id.txt_getcode);
        edt_code = (EditText) findViewById(R.id.edt_code);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        txt_btn = (TextView) findViewById(R.id.txt_btn);
        // img_close = (ImageView) findViewById(R.id.img_close);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        title_name.setText("业主认证");
        lin_left.setOnClickListener(this);
        txt_getcode.setOnClickListener(this);
        txt_btn.setOnClickListener(this);
        //禁用验证码、登录
        txt_getcode.setTextColor(getResources().getColor(R.color.colorPrimary));
        txt_getcode.setBackground(getResources().getDrawable(R.drawable.cornes_bgwhite_edge_orange));
        txt_getcode.setEnabled(true);
       /* txt_btn.setTextColor(getResources().getColor(R.color.white2new));
        txt_btn.setBackground(getResources().getDrawable(R.drawable.corners_bg_transparent_red));
        txt_btn.setEnabled(false);*/
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.lin_left:
                // Bundle bundle = new Bundle();
               /* bundle.putSerializable("info", "");
                intent.putExtras(bundle);*/
                //setResult(333, intent);
                finish();
                break;
            case R.id.txt_getcode:
                if (edt_phone.getText().toString().equals("")) {
                    XToast.makeText(MyZhuZhaiRZActivity.this, "请输入所在物业预留的手机号", XToast.LENGTH_SHORT).show();
                } else if (edt_phone.getText().toString().trim().equals("")) {
                    XToast.makeText(this, "请输入验证码", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isMobileNO(edt_phone.getText().toString())) {
                    XToast.makeText(MyZhuZhaiRZActivity.this, "请输入正确的手机格式", XToast.LENGTH_SHORT).show();
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
                                ld = uc.getDate() / 1000; //取得网站日期时间、
                                System.out.println("ld------" + ld);
                                timestamp = String.valueOf(ld);
                                //获取加密字符串
                                String ttttt = MD5.hexdigest("hui-shenghuo.api_sms").substring(8, 24);
                                jmStr = MD5.hexdigest(timestamp + ttttt + timestamp).substring(0, 16);
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

            case R.id.txt_btn:
                if (edt_phone.getText().toString().equals("")) {
                    XToast.makeText(MyZhuZhaiRZActivity.this, "请输入所在物业预留的手机号", XToast.LENGTH_SHORT).show();
                } else if (edt_phone.getText().toString().trim().equals("")) {
                    XToast.makeText(this, "请输入验证码", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isMobileNO(edt_phone.getText().toString())) {
                    XToast.makeText(MyZhuZhaiRZActivity.this, "请输入正确的手机格式", XToast.LENGTH_SHORT).show();
                } else {
                    getserch();
                }
/*
                Intent intent1 = new Intent(MyZhuZhaiRZActivity.this, HouseSelectActivity.class);
                startActivity(intent1);*/
                break;
        }
    }

    private void getserch() {//验证短信接口.查询是否在物业系统中留有信息
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", edt_phone.getText().toString());
        params.addBodyParameter("mobile_vcode", edt_code.getText().toString());
        HttpHelper hh = new HttpHelper(urlInfo.check_bind_sms, params, MyZhuZhaiRZActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol.setShop(json);
                if (str.equals("1")) {
                    getinfo();//验证码验证成功查询
                } else {
                    XToast.makeText(MyZhuZhaiRZActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");

            }
        };
    }

    private void getCode() {//物业验证发送验证码
        Url_info urlInfo = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", edt_phone.getText().toString());
        params.addBodyParameter("ApiSmstokentime", timestamp);// 时间戳
        params.addBodyParameter("ApiSmstoken", jmStr);// 加密字符串
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(urlInfo.bind_send_sms,
                params, MyZhuZhaiRZActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol.setShop(json);
                if (str.equals("1")) {
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    XToast.makeText(MyZhuZhaiRZActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getinfo() {//短信验证码验证成功后下一步（）
        Url_info urlInfos = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", edt_phone.getText().toString());
        System.out.println("mobile-----" + edt_phone.getText().toString());
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(urlInfos.check_property_mobile,
                params, MyZhuZhaiRZActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        beans = wuYeProtocol.getWuYe(json);

                        // String room_id = beans.get(0).getRoom_id();
                        getfinish();//查询成功直接执行绑定


                    } else {
                        XToast.makeText(MyZhuZhaiRZActivity.this, msg, XToast.LENGTH_SHORT).show();
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


    private void getfinish() {//完成绑定
        Url_info urlInfoss = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", edt_phone.getText().toString());
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(urlInfoss.property_bind_user,
                params, MyZhuZhaiRZActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                System.out.println("json00000---" + json);
                str = protocol.setShop(json);
                if (str.equals("1")) {
                    MyCookieStore.WY_notify = 1;
                    if (beans.size() == 1) {//当数量为1时直接跳转到家庭成员信息界面
                        Intent intent1 = new Intent(MyZhuZhaiRZActivity.this, HousePersonInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("room_id", beans.get(0).getRoom_id());
                        System.out.println("room_id***********对不对" + beans.get(0).getRoom_id());
                        intent1.putExtras(bundle);
                        startActivity(intent1);

                    } else {//否则跳转选择房屋绑定界面
                        Intent intent1 = new Intent(MyZhuZhaiRZActivity.this, HouseSelectActivity.class);
                        intent1.putExtra("wuye_type", "fw_info");//为fw_info时 选择小区完成后跳转到房屋信息
                        startActivity(intent1);

                    }
                    //临时文件存储
                    SharedPreferences preferences1 = MyZhuZhaiRZActivity.this.getSharedPreferences("login", 0);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("is_wuye", "2");
                    editor.commit();
                    finish();
                    HouseGuideActivity.instant.finish();//绑定成功后finish掉 当前界面与说明页
                    XToast.makeText(MyZhuZhaiRZActivity.this, "绑定成功", XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(MyZhuZhaiRZActivity.this, str, XToast.LENGTH_SHORT).show();
                    System.out.println("str=========" + str);
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
            txt_getcode.setOnClickListener(MyZhuZhaiRZActivity.this);
            txt_getcode.setTextColor(getResources().getColor(R.color.colorPrimary));
            txt_getcode.setBackground(getResources().getDrawable(R.drawable.cornes_bgwhite_edge_orange));


        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished / 1000 >= 1) {
                txt_getcode.setClickable(false);
            }
            txt_getcode.setTextColor(getResources().getColor(R.color.black2new));
            txt_getcode.setBackground(getResources().getDrawable(R.drawable.cornes_bgwhite_edgegray2));

            txt_getcode.setText(millisUntilFinished / 1000 + "秒后重发");
            //txt_getcode.setBackgroundColor(getResources().getColor(R.color.view_background));
        }
    }


}
