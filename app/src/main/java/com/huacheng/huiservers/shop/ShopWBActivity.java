package com.huacheng.huiservers.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.UMShareDialog;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.Sessionidtest;
import com.huacheng.huiservers.utils.ShareUtils;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.cookie.Cookie;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class ShopWBActivity extends BaseUI implements OnClickListener {
    private TextView title_name, txt_right1;
    private LinearLayout lin_left;
    private WebView webView;
    private String url_str;
    String url, tag, uid, id, call_link, img;
    String title = "";
    String content = "";
    private String login_type;
    private SharedPreferences preferencesLogin;

    @Override
    protected void onResume() {
        super.onResume();
        //清除角标（华为）
        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(this);
            MyReceiver.setBadgeOfHuaWei(this, 0);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.shop_webview);
//        SetTransStatus.GetStatus(this);//系统栏默认为黑色
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        url_str = this.getIntent().getExtras().getString("url");
        title = this.getIntent().getExtras().getString("title");
        content = this.getIntent().getExtras().getString("content");
        img = this.getIntent().getExtras().getString("img");
        call_link = this.getIntent().getExtras().getString("call_link");

        id = url_str.substring(url_str.lastIndexOf("/") + 1, url_str.length());
        Log.e("ididididi", id);
        //uid=this.getIntent().getExtras().getString("uid");
        title_name = (TextView) findViewById(R.id.title_name);
        txt_right1 = (TextView) findViewById(R.id.txt_right1);
        if (this.getIntent().getExtras().getString("type").equals("1")) {
            title_name.setText("小慧推荐");
        } else {
            title_name.setText("优惠券");
        }

        txt_right1.setVisibility(View.VISIBLE);
        url = url_str;
        System.out.println("url_str=======" + url_str);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        txt_right1.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.wv_about);

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置脚本是否允许自动打开弹窗，默认false，不允许
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        if (!login_type.equals("")) {


            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            List<Cookie> cookies = MyCookieStore.cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie1 = cookies.get(i);
                if (cookie1.getName().equalsIgnoreCase("PHPSESSID")) {
                    // 使用一个常量来保存这个cookie，用于做session共享之用
                    Sessionidtest.PHPSESSID = cookie1;
                }
            }
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("PHPSESSID=%s", Sessionidtest.PHPSESSID.getValue()));
            sbCookie.append(String.format(";domain=%s", Sessionidtest.PHPSESSID.getDomain()));
            sbCookie.append(String.format(";path=%s", Sessionidtest.PHPSESSID.getPath()));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
        }
        /**
         * 调用loadUrl()方法进行加载内容
         */
        webView.loadUrl(url);
        webView.requestFocusFromTouch();
        /*webView.setWebViewClient(new WebViewClient(){
            @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});*/

        //设置WebChromeClient
        webView.setWebChromeClient(new WebChromeClient() {
            //处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//构建一个Builder来显示网页中的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWBActivity.this);
                builder.setTitle("领取");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            ;

            //处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWBActivity.this);
                builder.setTitle("confirm");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            ;

            @Override
//设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                ShopWBActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            //设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                ShopWBActivity.this.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_right1:
                preferencesLogin = this.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(this, LoginVerifyCode1Activity.class));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(ShopWBActivity.this, UMShareDialog.class);
                    intent.putExtra("url", url);
                    startActivityForResult(intent, 1);
                }
                /*new ShareAction(ShopWBActivity.this)
						.withText("hello")
						.setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
						.setCallback(shareListener)
						.open();*/
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (20 == resultCode) {
            String type = data.getExtras().getString("type");
            if (type.equals("qq")) {
                ShareUtils.shareWeb(ShopWBActivity.this, url, title
                        , content, MyCookieStore.URL + img,
                        R.mipmap.ic_launcher, SHARE_MEDIA.QQ, id, call_link);
            } else if (type.equals("qqzone")) {
                ShareUtils.shareWeb(ShopWBActivity.this, url, title
                        , content, MyCookieStore.URL + img,
                        R.mipmap.ic_launcher, SHARE_MEDIA.QZONE, id, call_link);
            } else if (type.equals("wei")) {
                ShareUtils.shareWeb(ShopWBActivity.this, url, title
                        , content, MyCookieStore.URL + img,
                        R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN, id, call_link);
            } else if (type.equals("wei_c")) {
                ShareUtils.shareWeb(ShopWBActivity.this, url, title
                        , content, MyCookieStore.URL + img,
                        R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE, id, call_link);
            }
        }
    }
}
