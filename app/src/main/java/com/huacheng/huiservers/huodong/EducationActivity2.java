package com.huacheng.huiservers.huodong;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.ServiceParticipateActivity;
import com.huacheng.huiservers.center.bean.EnrollModel;
import com.huacheng.huiservers.geren.ZhifuActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.GsonTools;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.ExpandableTextView;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.huiservers.view.StretchyTextView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hwh on 2017/10/12.
 */

public class EducationActivity2 extends BaseUI implements View.OnClickListener {
    private ImageView iv_bak, iv_picture, iv_img;
    private TextView tv_title1, tv_num,
            tv_address, tv_phone, tv_start_time, txt_time_type,
            txt_day, txt_hours, txt_min, tv_money, tv_title, tv_time, tv_XS_type;
    private Button tv_yu;
    private boolean isRun = true;
    BitmapUtils bitmapUtils;
    private LinearLayout lin_miaosha, lin_img, lin_XS_bottom, iv_call, linear_time;
    private RelativeLayout rel_bottom, rel_container;
    StringUtils stringUtils;
    ExpandableTextView expandableTextView;
    StretchyTextView stretchyTextView;
    private Boolean isCollapsed = true;
    private String id, cid, cost, url, cost_cn;
    String pay_over, order_id;
    WebView webview;
    private PopupWindow mPopupWindow;
    SharedPreferences preferencesLogin;
    private List<BannerBean> imgs = new ArrayList<BannerBean>();
    String login_type;
    private Date data_start, data_now, data_end;
    long diff, mDay, mHour, mMin, mSecond;
    private String start_time, end_time, phone, titleName;
    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                System.out.println("-^^^^----" + mDay + "天" + mHour + "时" + mMin + "分" + mSecond + "秒");
                txt_day.setText(mDay + "");
                txt_hours.setText(mHour + "");
                txt_min.setText(mMin + "");
                //txt_second.setText(mSecond+"");
                computeTime();
                if (mDay == 0 && mHour == 0 && mMin == 0 && mSecond == 0) {
                    //countDown.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_education2);
        //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
        iv_bak = (ImageView) findViewById(R.id.iv_back);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_call = (LinearLayout) findViewById(R.id.iv_call);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_money = (TextView) findViewById(R.id.tv_money);
        txt_time_type = (TextView) findViewById(R.id.txt_time_type);
        txt_day = (TextView) findViewById(R.id.txt_day);
        txt_hours = (TextView) findViewById(R.id.txt_hours);
        txt_min = (TextView) findViewById(R.id.txt_min);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_address = (TextView) findViewById(R.id.tv_address);
        lin_miaosha = (LinearLayout) findViewById(R.id.lin_miaosha);
        lin_img = (LinearLayout) findViewById(R.id.lin_img);
        linear_time = (LinearLayout) findViewById(R.id.linear_time);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_yu = (Button) findViewById(R.id.tv_yu);
        bitmapUtils = new BitmapUtils(this);
        stretchyTextView = (StretchyTextView) findViewById(R.id.spread_textview);
        expandableTextView = (ExpandableTextView) findViewById(R.id.expandable_text);
        rel_container = (RelativeLayout) findViewById(R.id.rel_container);

        lin_XS_bottom = (LinearLayout) findViewById(R.id.lin_XS_bottom);//底部栏
        rel_bottom = (RelativeLayout) findViewById(R.id.rel_bottom);//底部栏
        tv_XS_type = (TextView) findViewById(R.id.tv_XS_type);

        webview = (WebView) findViewById(R.id.webview);

        stretchyTextView.setMaxLineCount(3);
        stretchyTextView.setContent("近些年来，越来越多的行业开始和互联网结合，诞生了越来越多的互联网创业公司。互联网创业公司需要面对许多的不确定因素。如果你和你的小伙伴们够幸运，你们的公司可能会在几个星期之内让用户数、商品数、订单量增长几十倍上百倍。一次促销可能会带来平时几十倍的访问流量，一次秒杀活动可能会吸引平时数百倍的访问用户。这对公司自然是极大的好事，说明产品得到认可，公司未来前景美妙。");
        expandableTextView.setText("当保罗决定离开快船的加盟火箭的时候，对于联盟和球迷来说就是一个深水炸弹，一方面震惊在保罗竟然愿意放弃快船留给他的2.1亿美元的超大合同，另一方面也震惊在保罗和哈登结合的双后场组合！尽管保罗和哈登的组合确实很出色，我们也看到了火箭在未来几年的夺冠的希望！但对于保罗为何要离开快船的原因，却依旧还让球迷猜测不已！"
                , isCollapsed);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        cid = intent.getStringExtra("cid");
        url = intent.getStringExtra("url");
        // titleName = intent.getStringExtra("name");
        tv_title.setVisibility(View.VISIBLE);

        MyCookieStore.Activity_notity = 0;
        expandableTextView.setListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(boolean isExpanded) {
                isCollapsed = isExpanded;
            }
        });

        getActivitys();
        iv_bak.setOnClickListener(this);
        tv_yu.setOnClickListener(this);
        iv_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_call:
                if (!TextUtils.isEmpty(phone)) {
                    new CommomDialog(this, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"
                                        + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }
                    }).show();//.setTitle("提示")
                }
                break;
            case R.id.tv_yu:
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                } else if (login_type.equals("1")) {//个人
                    if (pay_over.equals("1")) {//报名成功未支付
                        intent = new Intent(EducationActivity2.this, ZhifuActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("o_id", order_id);
                        bundle.putString("price", cost);
                        bundle.putString("type", "huodong");
                        bundle.putString("order_type", "hd");
                        Log.e("order_id90909909", order_id + "======" + cost);
//                        bundle.putString("cid", cid);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2000);
                    } else if (pay_over.equals("2")) {//报名成功支付成功

                    } else if (pay_over.equals("0")) {//未报名
                        click1(tv_title1);
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) { // 判断数据是否为空，就可以解决这个问题
            return;
        } else {
            switch (resultCode) {
                case 2001:
//                    XToast.makeText(EducationActivity2.this, "支付成功", XToast.LENGTH_SHORT).show();//"获取验证码失败"
                    Intent intent1 = new Intent(EducationActivity2.this, ServiceParticipateActivity.class);
                    Bundle bundle1 = new Bundle();
                    intent1.putExtra("type", "1");
                    intent1.putExtra("activityId", cid);//维修:"",教育:"114", 旅游:"115",社区活动:116 cid
//                    bundle1.putString("item_detete_id", item_id);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);

//                    setResult(3001, intent1);
                    finish();
                    break;
            }
        }
    }

   /* protected void getAddview() {
        if (imgs != null) {
            for (int i = 0; i < imgs.size(); i++) {
                View view = LinearLayout.inflate(EducationActivity2.this, R.layout.shop_detail_imgdesc_item, null);
                ImageView img = (ImageView) view.findViewById(R.id.img);
                bitmapUtils.display(img, MyCookieStore.URL + detailBean.getImgs().get(i).getImg());
                lin_img.addView(view);
            }
            detailBean.getImgs().clear();
        }
    }*/

    private void gettime() { //限购时间
        //获取当天的日期
        /*	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sDateFormat.format(new java.util.Date());*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strData = formatter.format(curDate);
        //获取开始时间和结束时间
        String str_Start = StringUtils.getDateToString(start_time, "1");
        String str_End = StringUtils.getDateToString(end_time, "1");
        System.out.println("str_Start---" + str_Start);
        System.out.println("str_End---" + str_End);
        System.out.println("date-----" + strData);
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        java.util.Calendar c3 = java.util.Calendar.getInstance();
        try {
            data_now = df.parse(strData);
            data_start = df.parse(str_Start);
            data_end = df.parse(str_End);
            c1.setTime(data_now);//c1是当前时间
            c2.setTime(data_start);//c2是获取的开始时间
            c3.setTime(data_end);//c2是获取的结束时间
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        int result2 = c1.compareTo(c3);

        if (result >= 0) {//
            if (result2 >= 0) {
                //lin_miaosha.setVisibility(View.GONE);
                txt_time_type.setText("报名已结束");
                rel_bottom.setVisibility(View.GONE);
                linear_time.setVisibility(View.GONE);
                lin_XS_bottom.setVisibility(View.VISIBLE);
                tv_XS_type.setText("活动已结束");
                //XToast.makeText(this,"秒杀结束", XToast.LENGTH_SHORT).show();
            } else if (result2 < 0) {//距离结束还有多长时间
                diff = data_end.getTime() - data_now.getTime();// 这样得到的差值是微秒级别
                SetTime();//获取天时分秒
                txt_time_type.setText("距结束 :");
                System.out.println("----###-" + mDay + "天" + mHour + "时" + mMin + "分" + mSecond + "秒");
                startRun(); //开始倒计时
                System.out.println("long----" + diff);
                rel_bottom.setVisibility(View.VISIBLE);
                lin_XS_bottom.setVisibility(View.GONE);
            }
        } else if (result < 0) {//距离秒杀还有多长时间
            //开始时间小//当前时间大
            txt_time_type.setText("距开始 :");
            diff = data_start.getTime() - data_now.getTime();// 这样得到的差值是微秒级别
            SetTime();//获取天时分秒
            System.out.println("---@@@@-" + mDay + "天" + mHour + "时" + mMin + "分" + mSecond + "秒");
            startRun();//开始倒计时
            System.out.println("long----" + diff);
            rel_bottom.setVisibility(View.GONE);
            lin_XS_bottom.setVisibility(View.VISIBLE);
            tv_XS_type.setText("活动即将开始");
        }

    }


    // 获取活动
    private void getActivitys() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        if (TextUtils.isEmpty(url)) {
            url = info.activity_details + "id/" + id;
        }
        HttpHelper hh = new HttpHelper(
                url, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject, jsonData;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    if (StringUtils.isEquals(status, "1")) {

                        MyCookieStore.Activity_notity = 0;
                        jsonData = new JSONObject(data);
                        cost = jsonData.getString("cost");
                        cost_cn = jsonData.getString("cost_cn");
                        pay_over = jsonData.getString("pay_over");
                        order_id = jsonData.getString("order_id");
                        phone = jsonData.getString("phone");
                        //tv_money.setText(cost);
                        if (!TextUtils.isEmpty(cost_cn)) {
                            tv_money.setText(cost_cn);
                            if (pay_over.equals("1")) {
                                tv_yu.setText("去支付");
                            } else if (pay_over.equals("2")) {
                                tv_yu.setText("已报名");
                            } else if (pay_over.equals("0")) {
                                tv_yu.setText("预定");
                            }
                        } else {
                            if (jsonData.getString("cost").equals("0.00") || jsonData.getString("cost").equals("0.0") || jsonData.getString("cost").equals("0")) {
                                tv_money.setText("免费");
                                if (pay_over.equals("1")) {
                                    tv_yu.setText("去支付");
                                } else if (pay_over.equals("2")) {
                                    tv_yu.setText("已报名");
                                } else if (pay_over.equals("0")) {
                                    tv_yu.setText("预定");
                                }

                            } else {
                                tv_money.setText("¥ " + jsonData.getString("cost"));
                                if (pay_over.equals("1")) {
                                    tv_yu.setText("去支付");
                                } else if (pay_over.equals("2")) {
                                    tv_yu.setText("已报名");
                                } else if (pay_over.equals("0")) {
                                    tv_yu.setText("预定");
                                }
                            }
                        }
                        /*String img = jsonData.getString("introduction");
                        System.out.println("imgs===" + img);
                        if (img.equals("null") || img == null || img.equals("")) {
                        } else {
                            JSONArray imgarray = new JSONArray(img);
                            List<BannerBean> imglist = new ArrayList<BannerBean>();
                            for (int k = 0; k < imgarray.length(); k++) {
                                JSONObject imgObj = imgarray.getJSONObject(k);
                                BannerBean imginfos = new BannerBean();
                                imginfos.setImg(imgObj.getString("img"));
                                imgs.add(imginfos);
                                View view = LinearLayout.inflate(EducationActivity2.this, R.layout.shop_detail_imgdesc_item, null);
                                ImageView imageView = (ImageView) view.findViewById(R.id.img);
                                bitmapUtils.display(imageView, MyCookieStore.URL +imgObj.getString("img"));
                                lin_img.addView(view);
                            }
                        }*/
                        webview.getSettings().setJavaScriptEnabled(true);
                        // 设置允许JS弹窗
                        //        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        // 设置可以支持缩放
                        webview.getSettings().setSupportZoom(true);
                        webview.getSettings().setBuiltInZoomControls(true);
                        // 设置隐藏缩放工具控制条
                        webview.getSettings().setDisplayZoomControls(false);
                        //扩大比例的缩放
                        webview.getSettings().setUseWideViewPort(false);
                        //自适应屏幕
                        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        webview.getSettings().setLoadWithOverviewMode(true);

                        //设置字体大小
                        //webview.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                        webview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                        //byte[] bytes = Base64.decode(jsonData.getString("introduction"), Base64.DEFAULT);
                        //加载HTML字符串进行显示
                        // webview.loadDataWithBaseURL(null, getNewContent(jsonData.getString("introduction")), "text/html", "utf-8", null);
                        String content = jsonData.getString("introduction");
                        if (!"".equals(content)) {
                            String css = "<style type=\"text/css\"> " +
                                    "img {" +
                                    "max-width: 100% !important;" +//限定图片宽度填充屏幕
                                    "height:auto !important;" +//限定图片高度自动
                                    "}" +
                                    "</style>";
                            String content1 = "<head>" + css + "</head><body>" + content + "</body></html>";
                            webview.loadDataWithBaseURL(null, content1, "text/html", "utf-8", null);
                        }
                        // stretchyTextView.setContent(jsonData.getString("introduction"));
                        tv_title1.setText(jsonData.getString("title"));
                        tv_title.setText(jsonData.getString("title"));
                        if (jsonData.getString("personal_num").equals("0")) {
                            tv_num.setText("不限");
                            tv_num.setTextColor(0xff35C07D);
                        } else {
                            tv_num.setText(jsonData.getString("personal_num") + "名");
                        }
                        tv_address.setText(jsonData.getString("address"));
                        tv_phone.setText(jsonData.getString("phone"));
                        Log.e("order_idorder_id", order_id);
                        start_time = jsonData.getString("enroll_start");
                        end_time = jsonData.getString("enroll_end");
                        tv_start_time.setText(stringUtils.getDateToString(jsonData.getString("start_time"), "1")
                                + " 至  " + stringUtils.getDateToString(jsonData.getString("end_time"), "1"));
                        if (!"".equals(jsonData.getString("picture_size"))) {
                            //获取图片的宽高--start
                            iv_img.getLayoutParams().width = ToolUtils.getScreenWidth(EducationActivity2.this);
                            Double d = Double.valueOf(ToolUtils.getScreenWidth(EducationActivity2.this)) / (Double.valueOf(jsonData.getString("picture_size")));
                            iv_img.getLayoutParams().height = (new Double(d)).intValue();
                            //获取图片的宽高--end
                        }
                        Glide
                                .with(context)
                                .load(MyCookieStore.URL + jsonData.getString("picture"))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.icon_girdview)
                                .into(iv_img);
                        gettime();
                    } else if (StringUtils.isEquals(status, "0")) {
                        XToast.makeText(context, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    hideDialog(smallDialog);
                    rel_container.setVisibility(View.GONE);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                rel_container.setVisibility(View.GONE);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    //webview   图片适配
    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }

        return doc.toString();
    }

    @Override
    protected void onResume() {
        //清除角标（华为）
        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(this);
            MyReceiver.setBadgeOfHuaWei(this, 0);
        }
        getActivitys();
        if (MyCookieStore.Activity_notity == 1) {
            Intent intent1 = new Intent(EducationActivity2.this, ServiceParticipateActivity.class);
            Bundle bundle1 = new Bundle();
            intent1.putExtra("type", "1");
            intent1.putExtra("activityId", cid);//维修:"",教育:"114", 旅游:"115",社区活动:116 cid
//                    bundle1.putString("item_detete_id", item_id);
            intent1.putExtras(bundle1);
            startActivity(intent1);

//                    setResult(3001, intent1);
            finish();
        }
        super.onResume();
    }

    // 报名
    private void enroll(String name, String phone) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(id)) {
            params.addBodyParameter("a_id", id);
            //url = "activity/activity_enroll/";

        } else {
            id = url.substring(url.lastIndexOf("/") + 1, url.length());
            params.addBodyParameter("a_id", id);
        }
        params.addBodyParameter("name", name);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("cost", cost);
        params.addBodyParameter("community_id", "1");
        Log.e("activity_enroll", id + "==" + name + "==" + phone + "===" + cost + "====" + url);
        String a = "";
        HttpHelper hh = new HttpHelper(info.activity_enroll, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                EnrollModel model = GsonTools.getVo(json.toString(), EnrollModel.class);
                if (model.getStatus() == 1) {
                    //XToast.makeText(EducationActivity2.this, model.getMsg(), XToast.LENGTH_SHORT).show();
                    mPopupWindow.dismiss();
                    if (cost.equals("0.00")) {
                        tv_yu.setText("已报名");
                        getActivitys();
                    } else {
                        tv_yu.setText("去支付");
                        pay_over = "1";
                        order_id = model.getData().getOrder_id();
                        Intent intent = new Intent(EducationActivity2.this, ZhifuActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("o_id", model.getData().getOrder_id() + "");
                        bundle.putString("price", cost);
                        bundle.putString("type", "huodong");
                        bundle.putString("order_type", "hd");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2000);
//                        startActivity();
                    }

                } else if (model.getStatus() == 0) {
                    XToast.makeText(EducationActivity2.this, model.getMsg(), XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    private void SetTime() {
        mDay = diff / (1000 * 60 * 60 * 24);
        mHour = (diff - mDay * (1000 * 60 * 60 * 24))
                / (1000 * 60 * 60);
        mMin = (diff - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60))
                / (1000 * 60);
        mSecond = (diff - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60) - mMin * (1000 * 60))
                / 1000;
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }

    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void click1(View v) {

        final View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        // 实例化一个ColorDrawable颜色
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(EducationActivity2.this, 0.5f);//0.0-1.0

        // 设置好参数之后再show
        //   mPopupWindow.showAsDropDown(iv_jia);  // 默认在iv_jia的左下角显示
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = tv_title1.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
        mPopupWindow.showAtLocation(tv_title1, Gravity.CENTER, 0, 0);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//                UIUtils.closeInputMethod(EducationActivity2.this, contentView.findViewById(R.id.et_name));
                backgroundAlpha(EducationActivity2.this, 1f);
                /*InputMethodManager inputMethodManager = (InputMethodManager) mInputEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mInputEt.getApplicationWindowToken(), 0);*/
            }
        });
    }


    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.dialog_sign_on;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        /*getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/
        final EditText et_name = (EditText) contentView.findViewById(R.id.et_name);
        final EditText et_phone = (EditText) contentView.findViewById(R.id.et_phone);
        TextView tv_1 = (TextView) contentView.findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) contentView.findViewById(R.id.tv_2);
        Button bt_bm = (Button) contentView.findViewById(R.id.bt_bm);
        Button bt_pay = (Button) contentView.findViewById(R.id.bt_pay);
        if (cost.equals("0.00")) {
            tv_1.setVisibility(View.VISIBLE);
            tv_2.setVisibility(View.GONE);
            bt_bm.setVisibility(View.VISIBLE);
            bt_pay.setVisibility(View.GONE);
        } else {
            tv_1.setVisibility(View.GONE);
            tv_2.setVisibility(View.VISIBLE);
            bt_bm.setVisibility(View.GONE);
            bt_pay.setVisibility(View.VISIBLE);
        }
        bt_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    XToast.makeText(EducationActivity2.this, "请输入名字", XToast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    XToast.makeText(EducationActivity2.this, "请输入电话", XToast.LENGTH_SHORT).show();
                } else {
                    if (ToolUtils.isMobileNO(et_phone.getText().toString().trim())) {
                        enroll(et_name.getText().toString().trim(), et_phone.getText().toString().trim());
                    } else {
                        XToast.makeText(EducationActivity2.this, "请输入正确的手机号", XToast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bt_bm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    XToast.makeText(EducationActivity2.this, "请输入电话", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isMobileNO(et_phone.getText().toString().trim())) {
                    XToast.makeText(EducationActivity2.this, "请输入正确的手机号", XToast.LENGTH_SHORT).show();
                } else {
                    enroll(et_name.getText().toString().trim(), et_phone.getText().toString().trim());
                }

            }
        });

        return contentView;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止当前Activity结束以后,   handler依然继续循环浪费资源
        timeHandler.removeCallbacksAndMessages(null);
    }
}
