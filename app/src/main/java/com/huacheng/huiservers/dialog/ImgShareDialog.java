package com.huacheng.huiservers.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by hwh on 2018/3/31.
 */

public class ImgShareDialog extends Activity implements View.OnClickListener {
    private LinearLayout linear_qq, linear_qZone, linear_weixin, linear_weixin1;
    private LinearLayout pop_layout;
    private ImageView imageView;
    /* private UMImage imageurl,imagelocal;
     private UMWeb web;*/
    private String ShareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_img_share);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        linear_qq = (LinearLayout) this.findViewById(R.id.linear_qq);
        linear_qZone = (LinearLayout) this.findViewById(R.id.linear_qZone);
        linear_weixin = (LinearLayout) this.findViewById(R.id.linear_weixin);
        linear_weixin1 = (LinearLayout) this.findViewById(R.id.linear_weixin1);
        pop_layout = (LinearLayout) this.findViewById(R.id.pop_layout);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        //imageView.setImageBitmap(MyCookieStore.bitmap);
        Intent intent = getIntent();
        ShareUrl = intent.getStringExtra("url");
/*
        imageurl = new UMImage(this,"http://n.hui-shenghuo.cn/data/upload/images/app_logo.png");
        imageurl.setThumb(new UMImage(this, R.drawable.logo));

        web = new UMWeb(ShareUrl);
        web.setTitle("This is web title");
        web.setThumb(new UMImage(this, R.drawable.logo));
        web.setDescription("my description");*/

        //添加按钮监听
        linear_qq.setOnClickListener(this);
        linear_qZone.setOnClickListener(this);
        linear_weixin.setOnClickListener(this);
        linear_weixin1.setOnClickListener(this);
        pop_layout.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent data = new Intent();
        switch (v.getId()) {
            case R.id.linear_qq:
                data.putExtra("type", "qq");
                //请求代码可以自己设置，这里设置成20
                setResult(20, data);
                //关闭掉这个Activity
                finish();
               /* ShareUtils.shareWeb(UMShareDialog.this, "https://mobile.umeng.com/", "hello"
                        , "快来看", "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png",
                        R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                );*/
               /* new ShareAction(UMShareDialog.this)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withText("hello")//分享内容
                        .withMedia(imagelocal )
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();*/
                break;
            case R.id.linear_qZone:
                data.putExtra("type", "qqzone");
                //请求代码可以自己设置，这里设置成20
                setResult(20, data);
                //关闭掉这个Activity
                finish();
               /* ShareUtils.shareWeb(UMShareDialog.this, ShareUrl, "hello"
                        , "快来看", "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png",
                        R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                );*/
               /* new ShareAction(UMShareDialog.this)
                        .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                        .withText("hello")//分享内容
                        .withMedia(imagelocal )
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();*/
                break;
            case R.id.linear_weixin:
                data.putExtra("type", "wei");
                //请求代码可以自己设置，这里设置成20
                setResult(20, data);
                //关闭掉这个Activity
                finish();
               /* ShareUtils.shareWeb(UMShareDialog.this, ShareUrl, "hello"
                        , "快来看", "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png",
                        R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                );*/
               /* new ShareAction(UMShareDialog.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withText("hello")//分享内容
                        .withMedia(imagelocal )
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();*/
                break;
            case R.id.linear_weixin1:
                data.putExtra("type", "wei_c");
                //请求代码可以自己设置，这里设置成20
                setResult(20, data);
                //关闭掉这个Activity
                finish();
               /* ShareUtils.shareWeb(UMShareDialog.this, ShareUrl, "hello"
                        , "快来看", "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png",
                        R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                );*/
               /* new ShareAction(UMShareDialog.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withText("hello")//分享内容
                        .withMedia(imagelocal )
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();*/
                break;
            case R.id.pop_layout:
                break;
            default:
                break;
        }
        finish();
    }

    /*private UMShareListener shareListener = new UMShareListener() {
        *//**
     * @descrption 分享开始的回调
     * @param platform 平台类型
     *//*
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        *//**
     * @descrption 分享成功的回调
     * @param platform 平台类型
     *//*
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(UMShareDialog.this,"成功了",Toast.LENGTH_LONG).show();
        }

        *//**
     * @descrption 分享失败的回调
     * @param platform 平台类型
     * @param t 错误原因
     *//*
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(UMShareDialog.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        */

    /**
     * @param platform 平台类型
     * @descrption 分享取消的回调
     *//*
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(UMShareDialog.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
