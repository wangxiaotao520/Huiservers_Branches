package com.huacheng.libraryservice.http.response;

/**
 * Created by wangxiaotao
 */
public interface IResponseHandler {

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
