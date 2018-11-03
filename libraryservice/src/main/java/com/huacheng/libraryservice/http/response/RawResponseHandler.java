package com.huacheng.libraryservice.http.response;

/**
 * raw 字符串结果回调
 * Created by wangxiaotao
 */
public abstract class RawResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, String response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
