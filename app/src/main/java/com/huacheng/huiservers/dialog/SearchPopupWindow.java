package com.huacheng.huiservers.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.lidroid.xutils.http.RequestParams;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.qq.tencent.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchPopupWindow extends Activity implements OnClickListener{
	private LinearLayout linear_qq, linear_qZone, linear_weixin,linear_weixin1;
	private RelativeLayout pop_layout;
	private IWXAPI api;
	private Tencent mTencent;
	private final String Q_APPID = "1105961986";
	private final String W_APPID = "wx8765e31488491eb2";
	private String texts;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		Window win = getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
		linear_qq = (LinearLayout) this.findViewById(R.id.linear_qq);
		linear_qZone = (LinearLayout) this.findViewById(R.id.linear_qZone);
		linear_weixin = (LinearLayout) this.findViewById(R.id.linear_weixin);
		linear_weixin1 = (LinearLayout) this.findViewById(R.id.linear_weixin1);
		pop_layout = (RelativeLayout) this.findViewById(R.id.pop_layout);
	//	regToWx();
		//regToQQ();
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		if (TextUtils.isEmpty(url)){
			getText();
		}

		//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity

		//添加按钮监听
		linear_qq.setOnClickListener(this);
		linear_qZone.setOnClickListener(this);
		linear_weixin.setOnClickListener(this);
		linear_weixin1.setOnClickListener(this);
		pop_layout.setOnClickListener(this);
	}

	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void getText(){
		RequestParams params = new RequestParams();
		HttpHelper hh = new HttpHelper(
				MyCookieStore.SERVERADDRESS+"site/get_share", params,this) {

			@Override
			protected void setData(String json) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					String status = jsonObject.getString("status");
					String data = jsonObject.getString("data");
					if (StringUtils.isEquals(status, "1")) {
						System.out.println("obj====" + jsonObject);
						texts = data;
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			protected void requestFailure(Exception error, String msg) {
				UIUtils.showToastSafe("网络异常，请检查网络设置");
			}
		};
	}
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ShareListener myListener = new ShareListener();
		Tencent.onActivityResultData(requestCode,resultCode,data,myListener);
		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.REQUEST_QQ_SHARE ||
					resultCode == Constants.REQUEST_QZONE_SHARE ||
					resultCode == Constants.REQUEST_OLD_SHARE) {
				Tencent.handleResultData(data, myListener);
			}
		}
	}
	private void regToQQ() {
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。
		mTencent = Tencent.createInstance(Q_APPID, SearchPopupWindow.this);
	}
	private void regToWx() {
		api = WXAPIFactory.createWXAPI(this, W_APPID, true);
		api.registerApp(W_APPID);
	}
	private void qq(boolean flag) {
		SharedPreferences preferencesLogin=SearchPopupWindow.this.getSharedPreferences("login", 0);
		if (mTencent.isSessionValid() && mTencent.getOpenId() == null) {
			XToast.makeText(SearchPopupWindow.this, "您还未安装QQ", XToast.LENGTH_SHORT).show();
		}
		final Bundle params = new Bundle();
		if (TextUtils.isEmpty(url)){
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			params.putString(QQShare.SHARE_TO_QQ_TITLE, "给你分享一个好用的APP");
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, texts);
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://down.hui-shenghuo.cn/");
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png");
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "社区慧生活");
		}else {
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			params.putString(QQShare.SHARE_TO_QQ_TITLE, "给你分享一个好用的APP");
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, texts);
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://n.hui-shenghuo.cn/data/upload/images/app_logo.png");
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "社区慧生活");
		}

		if(flag){
			params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		}
		mTencent.shareToQQ(SearchPopupWindow.this, params, new ShareListener());
	}

	// 0-分享给朋友  1-分享到朋友圈
	private void weiChat(int flag) {
		if (!api.isWXAppInstalled()) {
			// mToast("您还未安装微信", 0);
			return;
		}
		SharedPreferences preferencesLogin=SearchPopupWindow.this.getSharedPreferences("login", 0);
		//login_type=preferencesLogin.getString("login_type", "");
		String text;
		if (TextUtils.isEmpty(url)){
			 text =texts+"http://down.hui-shenghuo.cn/";
		}else {
			 text =url;
		}

		//初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;
		//用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.description = text;

		*//* //创建一个WXWebPageObject对象，用于封装要发送的Url
	        WXWebpageObject webpage = new WXWebpageObject();
	        webpage.webpageUrl = "http://down.hui-shenghuo.cn/";
	        //创建一个WXMediaMessage对象
	        WXMediaMessage msg = new WXMediaMessage(webpage);
	        msg.title = "社区慧生活";
	        msg.description = "这是我们做的一款app，高端大气上档次，快来看看吧！";*//*

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求，这个必须有，否则会出错
		req.message = msg;

		//表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

		api.sendReq(req);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (HomeActivity.mTencent != null) {
			HomeActivity.mTencent.releaseResource();
		}
	}
	private class ShareListener implements IUiListener{

		@Override
		public void onCancel() {

			System.out.println("分享取消");
			Log.e("fengxiang","分享取消");
			XToast.makeText(SearchPopupWindow.this,"分享取消", XToast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Object arg0) {

			System.out.println("分享成功");
			Log.e("fengxiang","分享成功");
			XToast.makeText(SearchPopupWindow.this,"分享成功", XToast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError arg0) {

			System.out.println("分享出错");
			Log.e("fengxiang","分享出错");
			XToast.makeText(SearchPopupWindow.this,"分享出错", XToast.LENGTH_SHORT).show();
		}

	}*/
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.linear_qq:
				//qq(false);
				break;
			case R.id.linear_qZone:
				//qq(true);
				break;
			case R.id.linear_weixin:
				//weiChat(0);
				break;
			case R.id.linear_weixin1:
				//weiChat(1);
				break;
			case R.id.pop_layout:
				break;
			default:
				break;
		}
		finish();
	}
}  