package com.huacheng.libraryservice.http;

import android.os.Handler;
import android.os.Looper;

import com.huacheng.libraryservice.http.response.IResponseHandler;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.PreferenceUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Description: 网络请求封装
 * created by wangxiaotao
 * 2018/7/27 0027 下午 4:17
 */
public class MyHttpHelper {
    HttpUtils utils = new HttpUtils(100000);
    private static MyHttpHelper instance;

    private PreferenceUtils sharePrefrenceUtil;
    private Handler mHandler=new Handler(Looper.getMainLooper());

    public static MyHttpHelper get() {
        if (instance == null) {
            instance = new MyHttpHelper();
        }

        return instance;
    }

    public void get(String url, Map<String, String> params, final IResponseHandler responseHandler) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url = url + "/"+entry.getKey()+"/"+ entry.getValue()+"/";
        }
        utils.configCookieStore(MyCookieStore_New.cookieStore);
        utils.configCurrentHttpCacheExpiry(1000);
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                final String response_body = responseInfo.result;
                if (responseHandler instanceof JsonResponseHandler) {       //json回调
                    try {
                        final JSONObject jsonBody = new JSONObject(response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((JsonResponseHandler) responseHandler).onSuccess(responseInfo.statusCode, jsonBody);
                            }
                        });
                    } catch (JSONException e) {
                        //    LogUtils.e("onResponse fail parse jsonobject, body=" + response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                responseHandler.onFailure(responseInfo.statusCode, "fail parse jsonobject, body=" + response_body);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(HttpException error, final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onFailure(0, msg);
                    }
                });
            }
        });

    }

    public void post(String url, Map<String, String> params, final IResponseHandler responseHandler) {
       utils.configCookieStore(MyCookieStore_New.cookieStore);
        RequestParams requestParams = new RequestParams();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        utils.configSoTimeout(5 * 1000);
        utils.configTimeout(5 * 1000);
        utils.configCurrentHttpCacheExpiry(1000 * 10);
        utils.send(HttpRequest.HttpMethod.POST, url, requestParams,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(final ResponseInfo<String> responseInfo) {
                        final String response_body = responseInfo.result;
                        if (responseHandler instanceof JsonResponseHandler) {       //json回调
                            try {
                                final JSONObject jsonBody = new JSONObject(response_body);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((JsonResponseHandler) responseHandler).onSuccess(responseInfo.statusCode, jsonBody);
                                    }
                                });
                            } catch (JSONException e) {
                                //    LogUtils.e("onResponse fail parse jsonobject, body=" + response_body);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        responseHandler.onFailure(responseInfo.statusCode, "fail parse jsonobject, body=" + response_body);
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, final String msg) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                responseHandler.onFailure(0, msg);
                            }
                        });
                    }
                });
    }
}


