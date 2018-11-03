package com.huacheng.huiservers.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.huacheng.huiservers.login.bean.LoginBean;
import com.huacheng.huiservers.protocol.LoginProtocol;
import com.huacheng.huiservers.utils.WXConstants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {

    String push_id;
    LoginBean loginBean = new LoginBean();
    LoginProtocol protocol = new LoginProtocol();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        WXConstants.wx_api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID, true);
        WXConstants.wx_api.handleIntent(getIntent(), this);

    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        EventBus.getDefault().post(baseResp);
        //Toast.makeText(this, "baseresp.getType = " + baseResp.getType(), Toast.LENGTH_SHORT).show();
        finish();
    }


}

