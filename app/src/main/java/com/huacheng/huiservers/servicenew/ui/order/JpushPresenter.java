package com.huacheng.huiservers.servicenew.ui.order;

import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Description:
 * created by wangxiaotao
 * 2018/9/10 0010 上午 11:03
 */
public class JpushPresenter {

    public void paySuccessJpush(String order_id){
        HashMap<String, String> params = new HashMap<>();
        params.put("id",order_id);
        MyOkHttp.get().post(ApiHttpClient.PAY_SERVICE_SUCCESS, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)){

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }
}
