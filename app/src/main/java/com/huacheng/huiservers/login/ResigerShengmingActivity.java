package com.huacheng.huiservers.login;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.Url_info;

public class ResigerShengmingActivity extends BaseUI implements OnClickListener {
    private TextView title_name;
    private LinearLayout lin_left;
    WebView webView;

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
        setContentView(R.layout.shengming);
 //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
//		Typeface iconfont = Typeface.createFromAsset(this.getAssets(), "icons/iconfont.ttf");
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("用户协议");
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        findViewById(R.id.webview);
        lin_left.setOnClickListener(this);


        webView = (WebView) findViewById(R.id.webview);
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

        /**
         * 调用loadUrl()方法进行加载内容
         */
        Url_info info = new Url_info(this);
        webView.loadUrl(info.user_agreement);

         /* else if (tag.equals("pop_up")) {
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

            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            /*@Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                view.loadUrl(url);
            }*/
        });

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;

            default:
                break;
        }

    }

}
