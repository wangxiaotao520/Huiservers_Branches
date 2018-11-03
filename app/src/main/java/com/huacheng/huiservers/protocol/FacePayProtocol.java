package com.huacheng.huiservers.protocol;

import android.text.TextUtils;

import com.huacheng.huiservers.facepay.FacePayBean;
import com.huacheng.huiservers.geren.bean.WiredBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacePayProtocol {
    /**
     * @param json
     * @return
     */
    public List<FacePayBean> getFacePayCate(String json) {
        List<FacePayBean> pays = new ArrayList<FacePayBean>();
        JSONObject jsonObject, jsonData = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length() + 1; i++) {
                    FacePayBean facePayBean = new FacePayBean();
                    if (i == 0) {
                        facePayBean.setId("0");
                        facePayBean.setC_name("请选择");
                        facePayBean.setC_img("");
                        facePayBean.setSign("0");
                    } else {
                        jsonData = jsonArray.getJSONObject(i - 1);
                        facePayBean.setId(jsonData.getString("id"));
                        facePayBean.setC_name(jsonData.getString("c_name"));
                        facePayBean.setC_img(jsonData.getString("c_img"));
                        facePayBean.setSign(jsonData.getString("sign"));
                    }
                    pays.add(facePayBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pays;
    }

    public FacePayBean getRoomPersonalInfo(String json) {
        FacePayBean personalInfo = new FacePayBean();
        JSONObject jsonObject, jsonData = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                ;
                JSONObject jsonObj = new JSONObject(data);
                personalInfo.setId(jsonObj.getString("id"));
                personalInfo.setName(jsonObj.getString("name"));
                personalInfo.setMp1(jsonObj.getString("mp1"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return personalInfo;
    }

    public FacePayBean getFacePayOrder(String json) {
        FacePayBean facepayOrder = new FacePayBean();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONObject jsonObj = new JSONObject(data);
                facepayOrder.setId(jsonObj.getString("id"));
                facepayOrder.setOrder_number(jsonObj.getString("order_number"));
                facepayOrder.setC_name(jsonObj.getString("c_name"));
                facepayOrder.setCommunity_name(jsonObj.getString("community_name"));
                facepayOrder.setBuilding_name(jsonObj.getString("building_name"));
                facepayOrder.setUnit(jsonObj.getString("unit"));
                facepayOrder.setFloor(jsonObj.getString("floor"));
                facepayOrder.setCode(jsonObj.getString("code"));
                facepayOrder.setMoney(jsonObj.getString("money"));
                facepayOrder.setAddtime(jsonObj.getString("addtime"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facepayOrder;
    }

    // 当面付历史列表
    public List<FacePayBean> getFacepayHistoryList(String json) {
        List<FacePayBean> infos = new ArrayList<FacePayBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                if (!TextUtils.isEmpty(data)) {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        FacePayBean info = new FacePayBean();
                        JSONObject obj = array.getJSONObject(i);
                        info.setId(obj.getString("id"));
                        info.setOrder_number(obj.getString("order_number"));
                        info.setC_name(obj.getString("c_name"));
                        info.setCommunity_name(obj.getString("community_name"));
                        info.setBuilding_name(obj.getString("building_name"));
                        info.setUnit(obj.getString("unit"));
                        info.setFloor(obj.getString("floor"));
                        info.setCode(obj.getString("code"));

                        info.setM_name(obj.getString("m_name"));
                        info.setFullname(obj.getString("fullname"));
                        info.setMobile(obj.getString("mobile"));
                        info.setMoney(obj.getString("money"));
                        info.setNote(obj.getString("note"));
                        info.setAddtime(obj.getString("addtime"));
                        info.setTotal_Pages(obj.getInt("total_Pages"));
                        infos.add(info);
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return infos;
    }

    /*
    * 有限缴费提交数据
    *
    * */
    public WiredBean getsubmit(String json) {
        WiredBean info = new WiredBean();
        JSONObject jsonObject, jsonData = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONObject jsonObj = new JSONObject(data);
                info.setId(jsonObj.getString("id"));
                info.setC_name(jsonObj.getString("c_name"));
                info.setOrder_number(jsonObj.getString("order_number"));
                info.setUsername(jsonObj.getString("username"));
                info.setFullname(jsonObj.getString("fullname"));
                info.setAmount(jsonObj.getString("amount"));
                info.setAddtime(jsonObj.getString("addtime"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    // 有线缴费记录
    public List<WiredBean> getWiredHistoryList(String json) {
        List<WiredBean> infos = new ArrayList<WiredBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                if (!TextUtils.isEmpty(data)) {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        WiredBean info = new WiredBean();
                        JSONObject obj = array.getJSONObject(i);
                        info.setId(obj.getString("id"));
                        info.setOrder_number(obj.getString("order_number"));
                        info.setFullname(obj.getString("fullname"));
                        info.setWired_num(obj.getString("wired_num"));
                        info.setAmount(obj.getString("amount"));
                        info.setPay_type(obj.getString("pay_type"));
                        info.setAddtime(obj.getString("addtime"));
                        info.setUptime(obj.getString("uptime"));
                        info.setStatus(obj.getString("status"));
                        info.setTotal_Pages(obj.getInt("total_Pages"));
                        infos.add(info);
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return infos;
    }
}
