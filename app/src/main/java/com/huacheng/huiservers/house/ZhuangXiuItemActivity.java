package com.huacheng.huiservers.house;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.ImgShareDialog;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by hwh on 2018/4/2.
 */

public class ZhuangXiuItemActivity extends BaseUI implements View.OnClickListener {

    private LinearLayout lin_left;
    private LinearLayout linear;
    private TextView txt_btn;
    private WebView mWebview;
    private String webCon;
    private TextView title_name;
    Bitmap bitmap;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_zhuangxiu_item);
//        SetTransStatus.GetStatus(this);

        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        linear = (LinearLayout) findViewById(R.id.linear);
        txt_btn = (TextView) findViewById(R.id.txt_btn);
        mWebview = (WebView) findViewById(R.id.webview);
        title_name = (TextView) findViewById(R.id.title_name);

        title_name.setText("我的装修");

        Intent intent = getIntent();
        webCon = intent.getStringExtra("webCon");

        //能够的调用JavaScript代码
        mWebview.getSettings().setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        //        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
        mWebview.getSettings().setSupportZoom(true);
        mWebview.getSettings().setBuiltInZoomControls(true);

        //设置字体大小
        mWebview.getSettings().setTextSize(WebSettings.TextSize.LARGEST);

        // 设置隐藏缩放工具控制条
        mWebview.getSettings().setDisplayZoomControls(false);
        //扩大比例的缩放
        mWebview.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        //设置字体大小
        mWebview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
//            byte[] bytes = Base64.decode(bean.get(position).getContent(), Base64.DEFAULT);
        //加载HTML字符串进行显示
        //mWebview.loadDataWithBaseURL(null, getNewContent(webCon), "text/html", "utf-8", null);
        String content = webCon;
        if (!"".equals(content)) {
            String css = "<style type=\"text/css\"> " +
                    "img {" +
                    "max-width: 100% !important;" +//限定图片宽度填充屏幕
                    "height:auto !important;" +//限定图片高度自动
                    "}" +
                    "</style>";
            String content1 = "<head>" + css + "</head><body>" + content + "</body></html>";
            mWebview.loadDataWithBaseURL(null, content1, "text/html", "utf-8", null);
        }
        lin_left.setOnClickListener(this);
        txt_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_btn:
                //截取控件 保存为bitmap
                linear.setDrawingCacheEnabled(true);
                linear.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                linear.layout(0, 0, linear.getMeasuredWidth(), linear.getMeasuredHeight());
                bitmap = Bitmap.createBitmap(linear.getDrawingCache());
                linear.setDrawingCacheEnabled(false);

                Intent intent = new Intent();
                intent.setClass(ZhuangXiuItemActivity.this, ImgShareDialog.class);
                intent.putExtra("url", "");
                MyCookieStore.bitmap = bitmap;
                startActivityForResult(intent, 1);
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
                ShareUtils.shareImg(ZhuangXiuItemActivity.this,
                        bitmap, SHARE_MEDIA.QQ);
            } else if (type.equals("qqzone")) {
                ShareUtils.shareImg(ZhuangXiuItemActivity.this, bitmap, SHARE_MEDIA.QZONE);
            } else if (type.equals("wei")) {
                ShareUtils.shareImg(ZhuangXiuItemActivity.this, bitmap, SHARE_MEDIA.WEIXIN);
            } else if (type.equals("wei_c")) {
                ShareUtils.shareImg(ZhuangXiuItemActivity.this, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        }
    }

}
