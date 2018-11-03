package com.huacheng.huiservers.protocol;

import android.text.TextUtils;

import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.wuye.bean.WuYeBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：
 * 时间：2018/3/20 21:27
 * 功能描述:Huiservers
 */
public class HouseProtocol {


    //获取我的住宅列表 4.0版本接口
    public List<HouseBean> getHouseList(String json) {
        List<HouseBean> info = new ArrayList<HouseBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    HouseBean msg = new HouseBean();
                    JSONObject obj = array.getJSONObject(i);

                    msg.setCommunity_name(obj.getString("community_name"));
                    msg.setRoom_id(obj.getString("room_id"));
                    msg.setAddress(obj.getString("address"));
                    msg.setPer_count(obj.getString("per_count"));

                    //1016 add
                    msg.setIs_ym(obj.getString("is_ym")); //是否为远程0为无，1为亿码，2为汾西
                    msg.setCommunity_id(obj.getString("community_id"));

                    info.add(msg);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    public HouseBean gethousedetail(String json) {
        HouseBean info = new HouseBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);

                //获取小区信息
                String room_info = obj.getString("room_info");
                WuYeBean roominfo = new WuYeBean();
                JSONObject obj_room = new JSONObject(room_info);
                roominfo.setRoom_id(obj_room.getString("room_id"));
                roominfo.setCommunity_id(obj_room.getString("community_id"));
                roominfo.setCommunity_name(obj_room.getString("community_name"));
                roominfo.setBuilding_name(obj_room.getString("building_name"));
                roominfo.setUnit(obj_room.getString("unit"));
                roominfo.setCode(obj_room.getString("code"));
                roominfo.setAddress(obj_room.getString("address"));
                roominfo.setCount(obj_room.getString("count"));
                info.setRoom_info(roominfo);

                //获取家庭成员
                String per_li_info = obj.getString("per_li");
                if (!TextUtils.isEmpty(per_li_info)) {
                    JSONArray array = new JSONArray(per_li_info);
                    List<WuYeBean> beansFZ = new ArrayList<WuYeBean>();
                    for (int i = 0; i < array.length(); i++) {
                        WuYeBean adinfo = new WuYeBean();
                        JSONObject obj1 = array.getJSONObject(i);
                        adinfo.setAvatars(obj1.getString("avatars"));
                        adinfo.setNickname(obj1.getString("nickname"));
                        adinfo.setId(obj1.getString("id"));
                        adinfo.setUsername(obj1.getString("username"));
                        adinfo.setBind_type(obj1.getString("bind_type"));
                        beansFZ.add(adinfo);
                    }
                    info.setPer_li(beansFZ);
                } else {
                    info.setPer_li(null);
                }
                //获取租户成员
                String per_li_zhinfo = obj.getString("per_li_");
                if (!TextUtils.isEmpty(per_li_zhinfo)) {
                    JSONArray array = new JSONArray(per_li_zhinfo);
                    List<WuYeBean> beans = new ArrayList<WuYeBean>();
                    for (int i = 0; i < array.length(); i++) {
                        WuYeBean adinfo = new WuYeBean();
                        JSONObject obj1 = array.getJSONObject(i);
                        adinfo.setAvatars(MyCookieStore.URL
                                + obj1.getString("avatars"));
                        adinfo.setId(obj1.getString("id"));
                        adinfo.setNickname(obj1.getString("nickname"));
                        adinfo.setUsername(obj1.getString("username"));
                        adinfo.setBind_type(obj1.getString("bind_type"));
                        beans.add(adinfo);
                    }
                    info.setPer_li_(beans);
                } else {
                    info.setPer_li_(null);

                }

            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public HouseBean getHouseRoomBill(String json) {
        HouseBean house = new HouseBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject json1 = new JSONObject(data);

                house.setIs_available(json1.getString("is_available"));
                house.setIs_available_cn(json1.getString("is_available_cn"));
                //房屋信息
                String room_info = json1.getString("room_info");
                JSONObject objroom = new JSONObject(room_info);
                WuYeBean mHouseBean = new WuYeBean();
                mHouseBean.setRoom_id(objroom.getString("room_id"));
                mHouseBean.setCommunity_id(objroom.getString("community_id"));
                mHouseBean.setAddress(objroom.getString("address"));
                mHouseBean.setCommunity_name(objroom.getString("community_name"));
                mHouseBean.setBuilding_name(objroom.getString("building_name"));
                mHouseBean.setUnit(objroom.getString("unit"));
                mHouseBean.setCode(objroom.getString("code"));
                mHouseBean.setCount(objroom.getString("count"));
                house.setRoom_info(mHouseBean);
                //物业缴费
                String wuyeStr = json1.getString("wuye");
                if (!StringUtils.isEmpty(wuyeStr)) {
                    JSONArray wuyeAry = new JSONArray(wuyeStr);
                    List<HouseBean> wuyes = new ArrayList<>();
                    for (int i = 0; i < wuyeAry.length(); i++) {
                        JSONObject jsonobj = wuyeAry.getJSONObject(i);
                        HouseBean wuye = new HouseBean();
                        wuye.setBill_id(jsonobj.getString("bill_id"));
                        wuye.setRoom_id(jsonobj.getString("room_id"));
                        wuye.setCommunity_id(jsonobj.getString("community_id"));
                        wuye.setCommunity_name(jsonobj.getString("community_name"));
                        wuye.setBuilding_name(jsonobj.getString("building_name"));
                        wuye.setUnit(jsonobj.getString("unit"));
                        wuye.setCode(jsonobj.getString("code"));
                        wuye.setCharge_type(jsonobj.getString("charge_type"));
                        wuye.setSumvalue(jsonobj.getString("sumvalue"));
                        wuye.setStartdate(jsonobj.getString("startdate"));
                        wuye.setEnddate(jsonobj.getString("enddate"));
                        wuye.setTime(jsonobj.getString("time"));
                        wuyes.add(wuye);
                    }
                    house.setWuye(wuyes);
                }
                //水费
                String shuifeiStr = json1.getString("shuifei");
                HouseBean shuifeiBean = new HouseBean();
                if (!StringUtils.isEmpty(shuifeiStr)) {
                    JSONObject jsonShuifei = new JSONObject(shuifeiStr);
                    String infoStr = jsonShuifei.getString("info");
                    if (!StringUtils.isEmpty(infoStr)) {
                        JSONObject jsonInfo = new JSONObject(infoStr);
                        HouseBean shuidianInfo = new HouseBean();
                        shuidianInfo.setTotal(jsonInfo.getString("Total"));
                        shuidianInfo.setC_time(jsonInfo.getString("C_time"));
                        shuidianInfo.setSaccount(jsonInfo.getString("Saccount"));
                        shuidianInfo.setSMay_acc(jsonInfo.getString("SMay_acc"));
                        shuifeiBean.setInfoBean(shuidianInfo);
                    } else {
                        shuifeiBean.setInfoBean(null);
                    }
                    String type = jsonShuifei.getString("type");
                    String upper_limit = jsonShuifei.getString("upper_limit");
                    shuifeiBean.setUpper_limit(upper_limit);

                    shuifeiBean.setType(type);
                    house.setShuifei(shuifeiBean);
                }
                //电费
                String dianfeiStr = json1.getString("dianfei");
                HouseBean dianfeiBean = new HouseBean();
                if (!StringUtils.isEmpty(dianfeiStr)) {
                    JSONObject jsonDianfei = new JSONObject(dianfeiStr);
                    HouseBean dianfeiInfo = new HouseBean();
                    String infoStr = jsonDianfei.getString("info");
                    if (!StringUtils.isEmpty(infoStr)) {
                        JSONObject jsonInfo = new JSONObject(infoStr);
                        dianfeiInfo.setTotal(jsonInfo.getString("Total"));
                        dianfeiInfo.setC_time(jsonInfo.getString("C_time"));
                        dianfeiInfo.setDaccount(jsonInfo.getString("Daccount"));
                        dianfeiInfo.setDMay_acc(jsonInfo.getString("DMay_acc"));
                        dianfeiBean.setInfoBean(dianfeiInfo);
                    } else {
                        dianfeiBean.setInfoBean(null);
                    }
                    String type = jsonDianfei.getString("type");
                    String upper_limit = jsonDianfei.getString("upper_limit");
                    dianfeiBean.setUpper_limit(upper_limit);
                    dianfeiBean.setType(type);
                    house.setDianfei(dianfeiBean);
                }
            }

        } catch (Exception e) {
            LogUtils.e(e);
        }
        return house;
    }


    //获取家庭账单历史记录 4.0版本接口
    public List<HouseBean> getPropertyOrder(String json) {
        List<HouseBean> info = new ArrayList<HouseBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    HouseBean bean = new HouseBean();
                    JSONObject obj = array.getJSONObject(i);

                    bean.setId(obj.getString("id"));
                    bean.setUid(obj.getString("uid"));
                    bean.setUsername(obj.getString("username"));
                    bean.setNickname(obj.getString("nickname"));
                    bean.setOrder_num(obj.getString("order_num"));
                    bean.setNumber_suffix(obj.getString("number_suffix"));
                    bean.setBill_id(obj.getString("bill_id"));
                    bean.setRoom_id(obj.getString("room_id"));
                    bean.setCommunity_id(obj.getString("community_id"));
                    bean.setCommunity_name(obj.getString("community_name"));
                    bean.setBuilding_name(obj.getString("building_name"));
                    bean.setUnit(obj.getString("unit"));
                    bean.setCode(obj.getString("code"));
                    bean.setCharte_type_id(obj.getString("charte_type_id"));
                    bean.setCharge_type(obj.getString("charge_type"));
                    bean.setSumvalue(obj.getString("sumvalue"));
                    bean.setBill_time(obj.getString("bill_time"));
                    bean.setStartdate(obj.getString("startdate"));
                    bean.setEnddate(obj.getString("enddate"));
                    bean.setIs_pay(obj.getString("is_pay"));
                    bean.setPay_time(obj.getString("pay_time"));
                    bean.setPay_type(obj.getString("pay_type"));
                    bean.setAddtime(obj.getString("addtime"));
                    bean.setUptime(obj.getString("uptime"));
                    bean.setTotal_Pages(obj.getString("total_Pages"));
                    info.add(bean);
                }

            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }
//新房手册

    public List<HouseBean> getNewHouse(String json) {
        List<HouseBean> info = new ArrayList<HouseBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    HouseBean beans = new HouseBean();
                    JSONObject obj = array.getJSONObject(i);

                    beans.setId(obj.getString("id"));
                    beans.setTitle(obj.getString("title"));
                    beans.setArticle_image(obj.getString("article_image"));
                    beans.setOrder_num(obj.getString("order_num"));
                    beans.setUrl(obj.getString("url"));
                    beans.setUrl_type_id(obj.getString("url_type_id"));
                    beans.setUrl_type_name(obj.getString("url_type_name"));
                    beans.setImg_size(obj.getString("img_size"));

                    String strlist = obj.getString("list");
                    if (!StringUtils.isEmpty(strlist)) {
                        JSONObject object = new JSONObject(strlist);
                        HouseBean hlist = new HouseBean();
                        hlist.setInfo(object.getString("info"));
                        String img_size = object.getString("li");
                        JSONArray arrayimg = new JSONArray(img_size);
                        List<HouseBean> imglist = new ArrayList<HouseBean>();
                        for (int j = 0; j < arrayimg.length(); j++) {
                            HouseBean beanimg = new HouseBean();
                            JSONObject objimg = arrayimg.getJSONObject(j);
                            beanimg.setId(objimg.getString("id"));
                            beanimg.setImg(objimg.getString("img"));
                            beanimg.setUrl(objimg.getString("url"));
                            beanimg.setUrl_type(objimg.getString("url_type"));
                            beanimg.setType_name(objimg.getString("type_name"));
                            beanimg.setAdv_url(objimg.getString("adv_url"));
                            beanimg.setAdv_inside_url(objimg.getString("adv_inside_url"));
                            beanimg.setImg_size(objimg.getString("img_size"));
                            imglist.add(beanimg);
                        }
                        hlist.setLi(imglist);
                        beans.setList(hlist);
                    } else {
                        beans.setList(null);
                    }
                    info.add(beans);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 确认订单生成
    public HouseBean getMem(String json) {
        HouseBean info = new HouseBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            String msg = jsonObject.getString("msg");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                info.setContent(obj.getString("content"));
                info.setAddtime(obj.getString("addtime"));
                info.setUptime(obj.getString("uptime"));
            } else {
                info.setContent(null);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

}
