package com.huacheng.huiservers.protocol;

import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.wuye.bean.NewWuyeBean;
import com.huacheng.huiservers.wuye.bean.ProperyGetOrderBean;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.huacheng.huiservers.wuye.bean.WuyeInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WuYeProtocol {
    //获取绑定的物业户主信息
    public List<WuYeBean> getWuYe(String json) {
        List<WuYeBean> info = new ArrayList<WuYeBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    WuYeBean msg = new WuYeBean();
                    JSONObject obj = array.getJSONObject(i);
                    msg.setArea(obj.getString("area"));
                    msg.setRoom_id(obj.getString("room_id"));
                    msg.setName(obj.getString("name"));
                    msg.setMobile(obj.getString("mobile"));
                    msg.setBuilding_id(obj.getString("building_id"));
                    msg.setBuilding_name(obj.getString("building_name"));
                    msg.setCode(obj.getString("code"));
                    msg.setUnit(obj.getString("unit"));
                    msg.setCommunity_id(obj.getString("community_id"));
                    msg.setCommunity_name(obj.getString("community_name"));
                    msg.setCompany_id(obj.getString("company_id"));
                    msg.setCompany_name(obj.getString("company_name"));
                    msg.setDepartment_id(obj.getString("department_id"));
                    msg.setDepartment_name(obj.getString("department_name"));
                    msg.setFloor(obj.getString("floor"));
                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //生成物业订单信息
    public WuYeBean getWuorder(String json) {
        WuYeBean info = new WuYeBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                info.setId(obj.getString("id"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //绑定获取物业小区信息
    public List<WuYeBean> getWuYeXQInfo(String json) {
        List<WuYeBean> info = new ArrayList<WuYeBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    WuYeBean msg = new WuYeBean();
                    JSONObject obj = array.getJSONObject(i);
                    msg.setId(obj.getString("id"));
                    msg.setName(obj.getString("name"));
                    msg.setIs_ym(obj.getString("is_ym"));
                    msg.setDepartment_id(obj.getString("department_id"));
                    msg.setDepartment_name(obj.getString("department_name"));
                    msg.setCompany_id(obj.getString("company_id"));
                    msg.setCompany_name(obj.getString("company_name"));
                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //绑定获取物业小区信息
    public List<WuYeBean> getWuYebudling(String json) {
        List<WuYeBean> info = new ArrayList<WuYeBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    WuYeBean msg = new WuYeBean();
                    JSONObject obj = array.getJSONObject(i);
                    msg.setBuildsing_id(obj.getString("buildsing_id"));

                   /*
                    msg.setName(obj.getString("name"));
                    msg.setCommunity_id(obj.getString("community_id"));
                    msg.setUnits(obj.getString("units"));
                    msg.setFloors(obj.getString("floors"));
                    msg.setTypes(obj.getString("types"));
                    msg.setInfo(obj.getString("info"));*/
                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public List<WuYeBean> getWuYeUnit(String json) {
        List<WuYeBean> info = new ArrayList<WuYeBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    WuYeBean msg = new WuYeBean();
                    JSONObject obj = array.getJSONObject(i);
                    msg.setUnit(obj.getString("unit"));

                   /*
                    msg.setName(obj.getString("name"));
                    msg.setCommunity_id(obj.getString("community_id"));
                    msg.setUnits(obj.getString("units"));
                    msg.setFloors(obj.getString("floors"));
                    msg.setTypes(obj.getString("types"));
                    msg.setInfo(obj.getString("info"));*/
                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    //绑定获取物业小区房间
    public List<WuYeBean> getWuYeCode(String json) {
        List<WuYeBean> info = new ArrayList<WuYeBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    WuYeBean msg = new WuYeBean();
                    JSONObject obj = array.getJSONObject(i);
                    msg.setId(obj.getString("id"));
                    msg.setCode(obj.getString("code"));
                    msg.setFloor(obj.getString("floor"));
                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    /**
     * 获取用户水/电表详情
     *
     * @param json
     * @return
     */
    public List<NewWuyeBean> getPropertyInfo(String json) {
        List<NewWuyeBean> wuyes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray objAry = new JSONArray(data);
                for (int i = 0; i < objAry.length(); i++) {
                    JSONObject obj = objAry.getJSONObject(i);
                    NewWuyeBean msg = new NewWuyeBean();
                    msg.setId(obj.getString("id"));
                    msg.setFullname(obj.getString("fullname"));
                    msg.setMobile(obj.getString("mobile"));
                    msg.setCommunity_id(obj.getString("community_id"));
                    msg.setCommunity_name(obj.getString("community_name"));
                    msg.setBuilding_name(obj.getString("building_name"));
                    msg.setUnit(obj.getString("unit"));
                    msg.setFloor(obj.getString("floor"));
                    msg.setCode(obj.getString("code"));
                    msg.setRoom_id(obj.getString("room_id"));
                    msg.setBind_type(obj.getString("bind_type"));
                    msg.setBind_status(obj.getString("bind_status"));

                    String infoStr = obj.getString("info");
                    if (!StringUtils.isEmpty(infoStr)) {
                        JSONObject infoJson = new JSONObject(infoStr);
                        WuyeInfo info = new WuyeInfo();
                        info.setXq(infoJson.getString("Xq"));
                        info.setCust_id(infoJson.getString("Cust_id"));
                        info.setCust_name(infoJson.getString("Cust_name"));
                        info.setCust_addr(infoJson.getString("cust_addr"));
                        info.setTel1(infoJson.getString("Tel1"));
                        info.setAccount(infoJson.getString("Account"));
                        info.setMay_acc(infoJson.getString("May_acc"));

                        String dlistStr = infoJson.getString("d_list");
                        if (!StringUtils.isEmpty(dlistStr)) {
                            JSONObject dlist = new JSONObject(dlistStr);
                            WuyeInfo list = new WuyeInfo();
                            list.setTotal(dlist.getString("Total"));
                            list.setC_time(dlist.getString("C_time"));
                            list.setDaccount(dlist.getString("Daccount"));
                            list.setDMay_acc(dlist.getString("DMay_acc"));
                            info.setD_list(list);
                        }
                        String slistStr = infoJson.getString("s_list");
                        if (!StringUtils.isEmpty(slistStr)) {
                            JSONObject slist = new JSONObject(slistStr);
                            WuyeInfo list = new WuyeInfo();
                            list.setTotal(slist.getString("Total"));
                            list.setC_time(slist.getString("C_time"));
                            list.setSaccount(slist.getString("Saccount"));
                            list.setSMay_acc(slist.getString("SMay_acc"));
                            info.setS_list(list);
                        }
                        msg.setInfo(info);
                    }
                    wuyes.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return wuyes;
    }

    public ProperyGetOrderBean getProperyGetOrder(String json) {
        ProperyGetOrderBean getOrd = new ProperyGetOrderBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                getOrd.setId(obj.getString("id"));
                getOrd.setOid(obj.getString("oid"));
               /* getOrd.setUid(obj.getString("uid"));
                getOrd.setUsername(obj.getString("username"));
                getOrd.setNickname(obj.getString("nickname"));
                getOrd.setOrder_num(obj.getString("order_num"));
                getOrd.setBill_id(obj.getString("bill_id"));
                getOrd.setRoom_id(obj.getString("room_id"));
                getOrd.setCommunity_id(obj.getString("community_id"));
                getOrd.setCommunity_name(obj.getString("community_name"));
                getOrd.setBuilding_name(obj.getString("building_name"));
                getOrd.setUnit(obj.getString("unit"));
                getOrd.setCode(obj.getString("code"));
                getOrd.setCharte_type_id(obj.getString("charte_type_id"));
                getOrd.setCharge_type(obj.getString("charge_type"));
                getOrd.setSumvalue(obj.getString("sumvalue"));
                getOrd.setIs_pay(obj.getString("is_pay"));
                getOrd.setPay_time(obj.getString("pay_time"));
                getOrd.setPay_type(obj.getString("pay_type"));
                getOrd.setAddtime(obj.getString("addtime"));*/
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return getOrd;
    }


}
