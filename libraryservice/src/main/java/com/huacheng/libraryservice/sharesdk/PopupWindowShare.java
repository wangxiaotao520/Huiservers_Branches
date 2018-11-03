package com.huacheng.libraryservice.sharesdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.huacheng.libraryservice.R;
import com.huacheng.libraryservice.dialog.SmallDialog;
import com.huacheng.libraryservice.utils.AppConstant;
import com.huacheng.libraryservice.utils.ToastUtils;
import com.mob.MobSDK;

import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Modify by LiYing on 2018/1/05
 * 从此以后只负责分享 和鱼友交易更多
 */
public class PopupWindowShare implements OnClickListener {
    private static final String TAG = "MorePopupWindow";
    private PopupWindow mPopupWindow;

    private SmallDialog smallDialog;
    private LayoutInflater inflater;


    private Button  tv_share_to_weichat,
            tv_share_to_weichatfriends,
            tv_share_to_qq, tv_share_to_qzone;
    private Context context;
    private Button btnCancel;



    private PopupWindowHandler handler;
    private int shareTag = AppConstant.SHARE_COMMON;

    private String share_title;
    private String share_desc;
    private String share_icon;
    private String share_url;

    /**
     * 初始化分享平台
     */
    private void initSharePlatform() {
       // ShareSDK.initSDK(context);
        MobSDK.init(context,context.getString(R.string.MobAppkey),context.getString(R.string.MobAppkey));
    }


    /**
     * 分享APP
     */
    public PopupWindowShare(Context context, int shareTag) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        handler = new PopupWindowHandler();
        initSharePlatform();
        this.shareTag = shareTag;
        initShareAPPPopupWindow();
    }

    /**
     * 通用自定义分享方法
     */
    public PopupWindowShare(Context context, String share_title, String share_desc, String share_icon, String share_url, int shareTag) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.shareTag = shareTag;
        this.share_title = share_title;
        this.share_desc = share_desc;
        this.share_icon = share_icon;
        this.share_url = share_url;
        initSharePlatform();
        smallDialog = new SmallDialog(context);
        initShareAPPPopupWindow();
    }


    /**
     * 分享APP PopupWindow
     */
    private void initShareAPPPopupWindow() {
        View popupWindow = inflater.inflate(R.layout.share_app_popupwindow, null);
        btnCancel = (Button) popupWindow.findViewById(R.id.btn_pop_cancel);
        btnCancel.setOnClickListener(this);

        //举报收藏


        tv_share_to_weichat = (Button) popupWindow.findViewById(R.id.tv_share_app_to_weichat);
        tv_share_to_weichatfriends = (Button) popupWindow.findViewById(R.id.tv_share_app_to_weichatfav);
        tv_share_to_qq = (Button) popupWindow.findViewById(R.id.tv_share_app_to_qq);
        tv_share_to_qzone = (Button) popupWindow.findViewById(R.id.tv_share_app_to_qzone);

        tv_share_to_weichat.setOnClickListener(this);
        tv_share_to_weichatfriends.setOnClickListener(this);
        tv_share_to_qq.setOnClickListener(this);
        tv_share_to_qzone.setOnClickListener(this);

        mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.popUpwindow_anim);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_pop_cancel) {
            mPopupWindow.dismiss();
        }else if (view.getId()==R.id.tv_share_app_to_weichat){
            startShare(Wechat.NAME);
        }else  if (view.getId()==R.id.tv_share_app_to_weichatfav){
            startShare(WechatMoments.NAME);
        }else if (view.getId()==R.id.tv_share_app_to_qq){
            if (!isQQClientInstalled(context)){
                ToastUtils.showShort(context, "您没有安装QQ客户端~");
                return;
            }
            startShare(QQ.NAME);
        }else  if (view.getId()==R.id.tv_share_app_to_qzone){
            if (!isQQClientInstalled(context)){
                ToastUtils.showShort(context, "您没有安装QQ客户端~");
                return;
            }
            startShare(QZone.NAME);
        }


    }




    private void startShare(String mShare_meidia) {
        FunctionThirdPlatForm fc_share = new FunctionThirdPlatForm(context, ShareSDK.getPlatform(mShare_meidia), mPopupWindow);
        if (shareTag == AppConstant.SHARE_APP) {
//            fc_share.setShareParams(context.getString(R.string.shareSDK_download_title),context.getString(R.string.shareSDK_download_content),
//                    context.getString(R.string.w3g_address_of_shareSDK_download),AppConstant.YULIN_ICON);
        }  else if(shareTag == AppConstant.SHARE_COMMON){
            fc_share.setShareParams(share_title,share_desc,share_url,share_icon);
        }
    }

    class PopupWindowHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }



    public void showBottom(View parent) {
//        if (mPopupWindow == null) {
//            initPopuptWindow();
//        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            //设置屏幕透明度
            backgroundAlpha(0.4f);
        }
    }

    public void dismiss() {
        if (mPopupWindow == null)
            return;
        mPopupWindow.dismiss();
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(final float bgAlpha) {
        if (context instanceof Activity) {
            final Window window = ((Activity) context).getWindow();
            final WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            if (shareTag != 2) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bgAlpha == 1) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
                        } else {
                            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
                        }
                        window.setAttributes(lp);
                    }
                });
            }
        }
    }
    /**
     * 判断是否安装了微信
     * @param context
     * @return
     */
    public static boolean isWeixinInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName.toLowerCase(Locale.ENGLISH);
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否安装了QQ
     * @param context
     * @return
     */
    public static boolean isQQClientInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName.toLowerCase(Locale.ENGLISH);
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
