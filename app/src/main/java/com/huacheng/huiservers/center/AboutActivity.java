package com.huacheng.huiservers.center;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.WaitDIalog;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.libraryservice.dialog.CommomDialog;

import org.apache.http.cookie.Cookie;

public class AboutActivity extends BaseUI implements OnClickListener, AMapLocationListener {
    private TextView title_name;
    private LinearLayout lin_left;
    private WebView webView;
    private RelativeLayout title_rel;
    private LinearLayout about_left;
    private String tag, zhengwu_id;
    Cookie PHPSESSID;
    String url, strHouse;
    private Dialog WaitDialog;


    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.about);
        //   SetTransStatus.GetStatus(this);//系统栏默认为黑色

        title_rel = (RelativeLayout) findViewById(R.id.title_rel);
        about_left = (LinearLayout) findViewById(R.id.about_left);
        title_name = (TextView) findViewById(R.id.title_name);

    }

    @Override
    protected void initData() {
        super.initData();
        tag = this.getIntent().getExtras().getString("tag");
        if (tag.equals("about")) {
            title_name.setText("关于我们");
            about_left.setVisibility(View.GONE);
        } else if (tag.equals("house")) {
            strHouse = this.getIntent().getExtras().getString("strHouse");
            title_name.setText("手册详情");
            about_left.setVisibility(View.GONE);
        } else if (tag.equals("activity")) {
            strHouse = this.getIntent().getExtras().getString("strHouse");
            title_rel.setVisibility(View.GONE);
            about_left.setVisibility(View.VISIBLE);
            about_left.setOnClickListener(this);
        } else if (tag.equals("dsyg")) {
            getLocation();
            strHouse = this.getIntent().getExtras().getString("strHouse");
            about_left.setVisibility(View.GONE);
            title_name.setText("东森易购");

        } /*else if (tag.equals("pop_up")) {
            title_name.setText("优惠券");
        }*/
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.wv_about);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置允许JS弹窗
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        if (tag.equals("dsyg")) {
            WebSettings webSettings = webView.getSettings();
            //启用数据库
            webSettings.setDatabaseEnabled(true);
            String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            //设置定位的数据库路径
            webSettings.setGeolocationDatabasePath(dir);
            //最重要的方法，一定要设置，这就是出不来的主要原因
            webSettings.setDomStorageEnabled(true);

            //启用地理定位
            webSettings.setGeolocationEnabled(true);

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            //配置权限（同样在WebChromeClient中实现）
            webView.setWebChromeClient(new MyChromeViewClient());
//            webView.loadUrl("https://wxpay.wxutil.com/mch/pay/h5.v2.php");

        }
        /**
         * 调用loadUrl()方法进行加载内容
         */
        Url_info info = new Url_info(this);
        if (tag.equals("about")) {
            webView.loadUrl(info.about);
        } else if (tag.equals("house") || tag.equals("activity")) {
            webView.loadUrl(strHouse);
            System.out.println("strHouse***********" + strHouse);
        } /* else if (tag.equals("pop_up")) {
            String plink = this.getIntent().getExtras().getString("plink");
            if (!StringUtils.isEmpty(plink)) {
                webView.loadUrl(plink);
            }
        }*/
        webView.requestFocusFromTouch();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (tag.equals("activity")) {
                    // WaitDialog = WaitDIalog.createLoadingDialog(AboutActivity.this, "正在加载...");
                }

                if (tag.equals("dsyg")) {
                    if (isLoad) {
                        isLoad = false;
                        WaitDialog = WaitDIalog.createLoadingDialog(AboutActivity.this, "正在加载...");

                    }
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (tag.equals("activity")) {
                    // WaitDIalog.closeDialog(WaitDialog);
                }
                WaitDIalog.closeDialog(WaitDialog);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });


    }

    boolean isLoad = true;

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
            } else {
                startLocation();
            }
        } else {
            startLocation();
        }
    }

    public static final int COARSE_LOCATION_REQUEST_CODE = 88;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private void startLocation() {
//        showDialog(smallDialog);
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置需要地理位置信息
            mLocationOption.isNeedAddress();
            //     mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(true);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.about_left:
                finish();
                break;
            default:
                break;
        }
    }

    double longitude = 0;
    double latitude = 0;
    private boolean isInitLocaion = false;

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                longitude = location.getLongitude();//经度
                latitude = location.getLatitude();//纬度
            }

            if (!isInitLocaion) {
                isInitLocaion = true;
                if (tag.equals("dsyg")) {
                    strHouse = strHouse + "&atitude=" + latitude + "&longitude=" + longitude;
                    Log.e("url", strHouse);
                    webView.loadUrl(strHouse);
                }
            }

        } else {
//            hideDialog(smallDialog);
            ToastUtils.showShort(this, "定位失败...");
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        mLocationOption = null;
    }

    class MyChromeViewClient extends WebChromeClient {
        public void onGeolocationPermissionsShowPrompt(final String origin, final android.webkit.GeolocationPermissions.Callback callback) {
            final boolean remember = true;

            new CommomDialog(AboutActivity.this, R.style.my_dialog_DimEnabled, "允许获取您的地理位置信息吗？", new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        callback.invoke(origin, true, remember);
                        dialog.dismiss();
                    } else {
                        callback.invoke(origin, false, remember);
                    }
                }
            }).show();//.setTitle("提示")
           /* AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
            builder.setTitle("位置信息");
            builder.setMessage(origin + "允许获取您的地理位置信息吗？").setCancelable(true).setPositiveButton("允许",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            callback.invoke(origin, true, remember);
                        }
                    })
                    .setNegativeButton("不允许",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    callback.invoke(origin, false, remember);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();*/
        }
    }
}
