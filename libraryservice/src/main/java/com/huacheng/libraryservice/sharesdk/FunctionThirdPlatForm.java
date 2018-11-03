package com.huacheng.libraryservice.sharesdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 类说明： 第三方登录/注册/绑定/分享相关
 * @author wz
 * @version 1.0
 * @date 2014-12-5
 */
public class FunctionThirdPlatForm {
    private static final int MSG_ERR = 1;
    private static final int MSG_SUCCESS = 2;
    private static final int MSG_CANCEL = 3;
    private static final int MSG_REG = 4;
    private OnShareFinishedListener onShareListener;
    private Platform pf;                // 操作平台
    private PopupWindow mPopupWindow;
    Handler handlerUI;
    Context mContext;
    public static final int DO_THIRD_SHARE = 153;//第三方分享

    public void setOnShareFinishedListener(OnShareFinishedListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    /**
     * 获取当前操作的平台
     * @return
     */
    public Platform getPlatform() {
        return pf;
    }

    /**
     * 需要注意的是必须先initShareSDK
     *
     * @param context
     * @param platform 平台 ，使用ShareSDK.getPlaform获取
     */
    public FunctionThirdPlatForm(Context context, Platform platform) {
        this.mContext=context;
        pf = platform;
     //   pf.SSOSetting(false);// 设置成true直接使用网页授权，否则先考虑使用目标客户端授权，没有客户端情况下使用网页授权
    }

    /**
     * @param mPopupWindow 分享成功关闭mPopupWindow
     */
    public FunctionThirdPlatForm(Context context, Platform platform, PopupWindow mPopupWindow) {
        this.mContext=context;
        pf = platform;
    //    pf.SSOSetting(false);// 设置成true直接使用网页授权，否则先考虑使用目标客户端授权，没有客户端情况下使用网页授权
        this.mPopupWindow = mPopupWindow;
        initUiHandler();
    }


    protected void initUiHandler() {
        handlerUI = new UIHandler();
    }






    public class UIHandler extends Handler {
        public UIHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if (msg.what == DO_THIRD_SHARE) {
                switch (msg.arg1) {
                    case MSG_SUCCESS:// 成功
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                           mPopupWindow.dismiss();
                        }
                        if(onShareListener!=null){
                            onShareListener.shareSuccess();
                        }else{
                           // Toast.makeText(mContext.getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
                            ToastUtils.showToastShare(mContext.getApplicationContext(),"分享成功");
                        }
                        break;
                    case MSG_ERR:// 失败
                        Throwable t = (Throwable) msg.obj;
                        String expName = t.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)
                                || "WechatFavoriteNotSupportedException".equals(expName)) {
                            Toast.makeText(mContext, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "分享失败" , Toast.LENGTH_SHORT).show();
                            if(onShareListener!=null){
                                onShareListener.shareError();
                            }
                        }
                        break;
                    case MSG_CANCEL:// 取消
                        Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }
    }



    /**
      * Method: 标题限制512byte，Content限制1kb
      * Author: Lydia
      * Date: 2017/9/7
      */
    public void setShareParams(String str_shareTitle, String str_shareContent, String str_shareUrl, String img_shareImgUrl){
        String configHost = ApiHttpClient.API_URL;
        // 封装到ShareSdk进行分享
        ShareParams sp = new ShareParams();
        if (str_shareTitle.length()>150) {
            str_shareTitle = str_shareTitle.substring(0,150);
        }
        if (str_shareContent.length()>340) {
            str_shareContent = str_shareContent.substring(0,340);
        }
        sp.setTitle(str_shareTitle);// 标题
        sp.setText(str_shareContent);// 内容

        if (pf.getName().equals(QQ.NAME) || pf.getName().equals(QZone.NAME)) {
            // QQ分享
            sp.setTitleUrl(str_shareUrl); // 标题的超链接,点击之后直接跳转到指定的页面
            sp.setSite("社区慧生活");
            sp.setSiteUrl(configHost);
            if (img_shareImgUrl != null) {
                sp.setImageUrl(img_shareImgUrl.toString());
            }
        } else if (pf.getName().equals(Wechat.NAME)
                || pf.getName().equals(WechatMoments.NAME)) {
            // 分享到微信
            if (img_shareImgUrl != null) {
                sp.setImageUrl(img_shareImgUrl.toString());
            }
            sp.setShareType(Wechat.SHARE_WEBPAGE);// 微信必须设置一个SHARE类型，否则返回err,这里使用3G网站
            sp.setUrl(str_shareUrl);// 超链接，点击之后进入3G网站
//			if (str_shareVedioUrl != null) {// 分享视频
//				sp.setShareType(Wechat.SHARE_VIDEO);
//				sp.setUrl(str_shareVedioUrl);
//			} else {
//				// 在這里填充其他扩充类型，例如音乐等
//
//			}
        }

        // 设置分享事件回调
        pf.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Log.e("[Mob]","分享失败:arg2=" + arg2.getMessage() + "；arg1=" + arg1);
                Message msg = new Message();
                msg.what = DO_THIRD_SHARE;
                msg.obj = arg2;
                msg.arg1 = MSG_ERR;
                handlerUI.sendMessage(msg);
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Message msg = new Message();
                msg.what = DO_THIRD_SHARE;
                msg.arg1 = MSG_SUCCESS;
                handlerUI.sendMessageDelayed(msg,500);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Message msg = new Message();
                msg.what = DO_THIRD_SHARE;
                msg.arg1 = MSG_CANCEL;
                handlerUI.sendMessage(msg);
            }
        });
        pf.share(sp);
    }




    public interface OnShareFinishedListener {
        void shareSuccess();
        void shareError();
    }
}
