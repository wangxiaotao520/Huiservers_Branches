package com.huacheng.huiservers.protocol;

import com.huacheng.huiservers.geren.bean.AddressBean;
import com.huacheng.huiservers.geren.bean.GerenBean;
import com.huacheng.huiservers.geren.bean.PayTypeBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GerenProtocol {

    public List<GerenBean> getRepairType(String json) {
        List<GerenBean> gerenBeans=new ArrayList<GerenBean>();
        JSONObject jsonObject, jsonData;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    GerenBean type = new GerenBean();
                    jsonData = jsonArray.getJSONObject(i);
                    type.setId(jsonData.getString("id"));
                    type.setC_name(jsonData.getString("c_name"));
                    gerenBeans.add(type);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gerenBeans;
    }
    //获取地址列表
    public GerenBean addressList(String json) {
        GerenBean info = new GerenBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + data);
                JSONObject obj = new JSONObject(data);
                info.setCommunity_cn(obj.getString("community_cn"));
                String strlist = obj.getString("com_list");
                List<AddressBean> list = new ArrayList<AddressBean>();
                JSONArray array = new JSONArray(strlist);
                for (int i = 0; i < array.length(); i++) {
                    AddressBean addressBean = new AddressBean();
                    JSONObject objbean = array.getJSONObject(i);
                    addressBean.setId(objbean.getString("id"));
                    addressBean.setUid(objbean.getString("uid"));
                    addressBean.setConsignee_name(objbean.getString("consignee_name"));
                    addressBean.setConsignee_mobile(objbean.getString("consignee_mobile"));
                    addressBean.setRegion_id(objbean.getString("region_id"));
                    addressBean.setRegion_cn(objbean.getString("region_cn"));
                    addressBean.setCommunity_cn(objbean.getString("community_cn"));
                    addressBean.setCommunity_id(objbean.getString("community_id"));
                    addressBean.setDoorplate(objbean.getString("doorplate"));
                    addressBean.setStates(objbean.getString("states"));
                    addressBean.setIs_default(objbean.getString("is_default"));
                    addressBean.setCreatetime(objbean.getString("createtime"));
                    addressBean.setOperatingtime(objbean.getString("operatingtime"));
                    addressBean.setOperatingtime(objbean.getString("operatingtime"));
                    addressBean.setIs_select(objbean.getString("is_select"));
                    list.add(addressBean);
                }
                info.setCom_list(list);
            } else {
                info.setCommunity_cn(null);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //修改获取地址详情
    public AddressBean addressDetail(String json) {
        AddressBean info = new AddressBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                info.setId(obj.getString("id"));
                info.setUid(obj.getString("uid"));
                info.setConsignee_name(obj.getString("consignee_name"));
                info.setConsignee_mobile(obj.getString("consignee_mobile"));
                info.setRegion_id(obj.getString("region_id"));
                info.setRegion_cn(obj.getString("region_cn"));
                info.setDoorplate(obj.getString("doorplate"));
                info.setStates(obj.getString("states"));
                info.setIs_default(obj.getString("is_default"));
                info.setCreatetime(obj.getString("createtime"));
                info.setOperatingtime(obj.getString("operatingtime"));
                info.setCommunity_id(obj.getString("community_id"));
                info.setCommunity_cn(obj.getString("community_cn"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //添加地址成功
    public String addSuss(String json) {
        String str = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            if (StringUtils.isEquals(status, "1")) {
                str = "1";
            } else {
                str = msg;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return str;
    }

    public List<PayTypeBean> getPayList(String json) {
        List<PayTypeBean> info=new ArrayList<PayTypeBean>();
        JSONObject jsonObject, jsonData;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    PayTypeBean type = new PayTypeBean();
                    jsonData = jsonArray.getJSONObject(i);
                    type.setId(jsonData.getString("id"));
                    type.setByname(jsonData.getString("byname"));
                    type.setApp_id(jsonData.getString("app_id"));
                    type.setTypename(jsonData.getString("typename"));
                    type.setIcon(jsonData.getString("icon"));
                    type.setP_introduction(jsonData.getString("p_introduction"));
                    info.add(type);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

}

