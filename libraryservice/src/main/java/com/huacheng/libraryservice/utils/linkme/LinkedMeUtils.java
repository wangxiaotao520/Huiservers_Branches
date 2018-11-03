package com.huacheng.libraryservice.utils.linkme;

import android.app.Activity;

import com.microquation.linkedme.android.callback.LMLinkCreateListener;
import com.microquation.linkedme.android.indexing.LMUniversalObject;
import com.microquation.linkedme.android.referral.LMError;
import com.microquation.linkedme.android.util.LinkProperties;

import java.util.Map;

/**
 * Description: linkedme 工具类 生成深度链接
 * created by wangxiaotao
 * 2018/8/16 0016 下午 3:58
 */
public class LinkedMeUtils {
    private static LinkedMeUtils instance;

    OnGetLinkedmeUrlListener listener;
    private LinkedMeUtils(){

    }

    public static LinkedMeUtils getInstance() {
        if (instance == null) {
            instance = new LinkedMeUtils();
        }

        return instance;
    }

    public void getLinkedUrl(Activity activity, String share_url, String shareTitle, Map<String ,String> params , final OnGetLinkedmeUrlListener listener){

        /**创建深度链接*/
        //  提示：虽然客户端可自行创建深度链接并分享，但是web端也需要对分享链接进行处理才可使用深度链接，
        //  需要将分享链接中的深度链接截取出来，并作为“打开app”按钮的跳转链接(因此，建议使用js sdk创建深度链接)。
        //  例如：
        //     原有的分享链接为：https://www.linkedme.cc/h5/partner
        //     追加深度链接的分享链接为：https://www.linkedme.cc/h5/partner?linkedme=https://lkme.cc/AfC/CeG9o5VH8
        //     web端需要将深度链接https://lkme.cc/AfC/CeG9o5VH8取出并作为“打开app”按钮的跳转链接。

        //深度链接属性设置
        final LinkProperties properties = new LinkProperties();
        //渠道
        properties.setChannel("WEIXIN");  //微信、微博、QQ
        //功能
        properties.setFeature("Share");
        //标签
        properties.addTag("LinkedME");
        properties.addTag("goods_details");
        //阶段
        properties.setStage("Live");
        //设置该h5_url目的是为了iOS点击右上角lkme.cc时跳转的地址，一般设置为当前分享页面的地址
        properties.setH5Url(share_url);
        //自定义参数,用于在深度链接跳转后获取该数据
        if (params!=null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                properties.addControlParameter(entry.getKey(), entry.getValue());
            }
        }

        LMUniversalObject universalObject = new LMUniversalObject();
        universalObject.setTitle("LinkedME");

        // Async Link creation example
        universalObject.generateShortUrl(activity, properties, new LMLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, LMError error) {
                if (listener!=null){
                    listener.onGetUrl(url,error);
                }
            }
        });
    }

   public interface OnGetLinkedmeUrlListener{
        void onGetUrl(String url, LMError error);
   }
}
