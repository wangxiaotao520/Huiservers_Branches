package com.huacheng.libraryservice.http.response;

import org.json.JSONObject;

/**
 * json类型的回调接口
 * Created by wangxiaotao
 */
public abstract class JsonResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, JSONObject response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
