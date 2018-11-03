package com.huacheng.huiservers.center;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;

public class ALLWebviewActivity extends BaseUI implements OnClickListener {
    private TextView title_name;
    private LinearLayout lin_left;
    private WebView webView;
    String url, name;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.about);
        //   SetTransStatus.GetStatus(this);//系统栏默认为黑色
        title_name = (TextView) findViewById(R.id.title_name);
        name = this.getIntent().getExtras().getString("name");
        url = this.getIntent().getExtras().getString("url");
        title_name.setText(name);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.wv_about);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl(url);
        webView.requestFocusFromTouch();

    }

    @Override
    protected void initData() {
        super.initData();

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
