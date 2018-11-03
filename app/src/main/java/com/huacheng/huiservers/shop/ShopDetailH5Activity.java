package com.huacheng.huiservers.shop;

import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.MyCookieStore;

/**
 * 类：
 * 时间：2017/12/1 10:51
 * 功能描述:Huiservers
 */
public class ShopDetailH5Activity extends BaseUI implements View.OnClickListener {
    private TextView title_name;
    private LinearLayout lin_left;
    private WebView webView;
    String url, urlinfo;
    RelativeLayout title_rel;

    protected void init() {
        super.init();
        setContentView(R.layout.abouth5);
//        SetTransStatus.GetStatus(this);//系统栏默认为黑色
        // shop_id = this.getIntent().getExtras().getString("shop_id");
        urlinfo = this.getIntent().getExtras().getString("url");
        url = MyCookieStore.SERVERADDRESS + urlinfo;
        System.out.println("url____________" + url);
        title_rel = (RelativeLayout) findViewById(R.id.title_rel);

        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.wv_about);
        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + "; app");
        String ua = webView.getSettings().getUserAgentString();
        // 打印结果
        Log.i("TAG", "UA Ag:" + userAgent);
        Log.i("TAG", "UA Agent:" + ua);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        System.out.println("url_str======="+url);
        /**
         * 调用loadUrl()方法进行加载内容
         */
        webView.requestFocusFromTouch();
       /* Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);*/
        webView.loadUrl(url);
       /* webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                return true;
            }
        });*/

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
