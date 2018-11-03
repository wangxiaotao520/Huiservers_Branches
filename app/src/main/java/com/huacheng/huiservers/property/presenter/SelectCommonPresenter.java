package com.huacheng.huiservers.property.presenter;

import android.content.Context;

import com.huacheng.huiservers.property.bean.ModelSelectCommon;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 接口
 * created by wangxiaotao
 * 2018/8/27 0027 下午 7:37
 */
public class SelectCommonPresenter {

    Context mContext;

    OnGetDataListener listener;

    public SelectCommonPresenter(Context mContext, OnGetDataListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    /**
     * 获取住房类型
     */
    public void getHouseType() {
        HashMap<String, String> params = new HashMap<>();

        MyOkHttp.get().post(ApiHttpClient.GET_HOUSETYPE, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取抄表类型
     */
    public void getChaobiaoType() {
        HashMap<String, String> params = new HashMap<>();

        MyOkHttp.get().post(ApiHttpClient.GET_CHAOBIAOTYPE, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取楼号
     *
     * @param community_id
     */
    public void getFloor(String community_id, String houses_type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id", community_id);
        params.put("houses_type", houses_type);

        MyOkHttp.get().post(ApiHttpClient.GET_FLOOR, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取单元号
     *
     * @param community_id
     */
    public void getUnit(String community_id, String houses_type, String buildsing_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id", community_id);
        params.put("houses_type", houses_type);
        params.put("buildsing_id", buildsing_id);
        MyOkHttp.get().post(ApiHttpClient.GET_UNIT, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取房间号
     *
     * @param community_id
     */
    public void getRoom(String community_id, String houses_type, String buildsing_id, String unit) {
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id", community_id);
        params.put("houses_type", houses_type);
        params.put("buildsing_id", buildsing_id);
        params.put("units", unit);
        MyOkHttp.get().post(ApiHttpClient.GET_ROOM, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取商铺号
     *
     * @param community_id
     */
    public void getShops(String community_id, String houses_type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id", community_id);
        params.put("houses_type", houses_type);

        MyOkHttp.get().post(ApiHttpClient.GET_SHOPS, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
                    if (listener != null) {
                        listener.onGetData(1, mDatas, msg);
                    }
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
                    if (listener != null) {
                        listener.onGetData(0, null, msg);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (listener != null) {
                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
                }
            }
        });

    }

    /**
     * 获取抄表费项
     *
     * @param community_id
     */
    public void getCBType(String community_id) {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("community_id", community_id);
//
//        MyOkHttp.get().post(ApiHttpClient.GET_FEETYPE, params, new JsonResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, JSONObject response) {
//                if (JsonUtil.getInstance().isSuccess(response)) {
//                    List<ModelSelectCommon> mDatas = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelSelectCommon.class);
//                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "成功");
//                    if (listener != null) {
//                        listener.onGetData(1, mDatas, msg);
//                    }
//                } else {
//                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "数据获取失败");
//                    if (listener != null) {
//                        listener.onGetData(0, null, msg);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, String error_msg) {
//                if (listener != null) {
//                    listener.onGetData(-1, null, "网络错误,请检查网络设置");
//                }
//            }
//        });

    }

    public interface OnGetDataListener {
        void onGetData(int status, List<ModelSelectCommon> mDatas, String ms);
    }

}
