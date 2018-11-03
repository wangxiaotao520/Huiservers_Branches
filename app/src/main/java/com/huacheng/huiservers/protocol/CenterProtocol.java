package com.huacheng.huiservers.protocol;

import com.huacheng.huiservers.center.bean.ActivityDetailBean;
import com.huacheng.huiservers.center.bean.CityBean;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.center.bean.Imgs;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.center.bean.PayInfoBean;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
import com.huacheng.huiservers.center.bean.PropertyRepair;
import com.huacheng.huiservers.center.bean.PropertyRepairDetail;
import com.huacheng.huiservers.center.bean.Reply;
import com.huacheng.huiservers.center.bean.ShopOrderBeanType;
import com.huacheng.huiservers.center.bean.XorderDetailBean;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CenterProtocol {

    public PropertyRepairDetail getPropertyRepairsDetails(String json) {
        PropertyRepairDetail detail = new PropertyRepairDetail();
        JSONObject jsonObject, jsonData, jsonImg, jsonReply = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                jsonData = new JSONObject(data);
                PropertyRepair repair = new PropertyRepair();
                repair.setId(jsonData.getString("id"));
                repair.setUid(jsonData.getString("uid"));
                repair.setP_id(jsonData.getString("p_id"));
                repair.setP_name(jsonData.getString("p_name"));
                repair.setC_id(jsonData.getString("c_id"));
                repair.setC_name(jsonData.getString("c_name"));
                repair.setFullname(jsonData.getString("fullname"));
                repair.setMobile(jsonData.getString("mobile"));
                repair.setType(jsonData.getString("type"));
                repair.setType_cn(jsonData.getString("type_cn"));
                repair.setAddress(jsonData.getString("address"));
                repair.setDescription(jsonData.getString("description"));
                repair.setStarttime(jsonData.getString("starttime"));
                repair.setEndtime(jsonData.getString("endtime"));
                repair.setIs_replace(jsonData.getString("is_replace"));
                repair.setAdmin_id(jsonData.getString("admin_id"));
                repair.setAdmin_name(jsonData.getString("admin_name"));
                repair.setReply(jsonData.getString("reply"));
                repair.setReply_time(jsonData.getString("reply_time"));
                repair.setStatus(jsonData.optString("status"));
//                repair.setStatus(jsonData.getString("status"));
                repair.setOvertime(jsonData.getString("overtime"));
                repair.setAddtime(jsonData.getString("addtime"));
                //
                String admin_photo = jsonData.optString("admin_photo");
                String admin_rank = jsonData.optString("admin_rank");
                repair.setAdmin_photo(admin_photo);
                repair.setAdmin_rank(admin_rank);
               /* if (!admin_photo.equals("")) {
//                    repair.setAdmin_photo(jsonData.getString("admin_photo"));
                }
                if (!admin_rank.equals("")) {
//                    repair.setAdmin_rank(jsonData.getString("admin_rank"));
                }*/
                detail.setPropertyRepair(repair);
                String imgs = jsonData.optString("imgs");
                if (!StringUtils.isEmpty(imgs)) {
                    JSONArray jsonArray = new JSONArray(imgs);
                    List<Imgs> imgsList = new ArrayList<Imgs>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Imgs imgsBean = new Imgs();
                        jsonImg = jsonArray.getJSONObject(i);
                        imgsBean.setId(jsonImg.getString("id"));
                        imgsBean.setUid(jsonImg.getString("uid"));
                        imgsBean.setRepair_id(jsonImg.getString("repair_id"));
                        imgsBean.setImg(jsonImg.getString("img"));
                        imgsBean.setAddtime(jsonImg.getString("addtime"));
                        imgsList.add(imgsBean);
                    }
                    detail.setImgs(imgsList);
                }

                //回复
                String reply_list = jsonData.optString("r_list");
                if (!StringUtils.isEmpty(reply_list)) {
                    JSONArray jsonAryReply = new JSONArray(reply_list);
                    List<Reply> replys = new ArrayList<Reply>();
                    for (int i = 0; i < jsonAryReply.length(); i++) {
                        Reply reply = new Reply();
                        jsonReply = jsonAryReply.getJSONObject(i);
                        reply.setRepair(jsonReply.getString("repair"));
                        reply.setAddtime(jsonReply.getString("addtime"));
                        reply.setIns_name(jsonReply.getString("ins_name"));
                        reply.setCom_name(jsonReply.getString("com_name"));
                        reply.setPosition(jsonReply.getString("position"));
                        reply.setAvatars(jsonReply.getString("avatars"));
                        reply.setFullname(jsonReply.getString("fullname"));
                        replys.add(reply);
                    }
                    detail.setR_list(replys);
                }

            } else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;
    }


    public List<PropertyRepair> propertyRepairs(String json) {
        List<PropertyRepair> propertyRepairs = new ArrayList<PropertyRepair>();
        JSONObject jsonObject, jsonData = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                if (!StringUtils.isEmpty(data)) {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PropertyRepair repair = new PropertyRepair();
                        jsonData = jsonArray.getJSONObject(i);
                        repair.setId(jsonData.getString("id"));
                        repair.setUid(jsonData.getString("uid"));
                        repair.setP_id(jsonData.getString("p_id"));
                        repair.setP_name(jsonData.getString("p_name"));
                        repair.setC_id(jsonData.getString("c_id"));
                        repair.setC_name(jsonData.getString("c_name"));
                        repair.setFullname(jsonData.getString("fullname"));
                        repair.setMobile(jsonData.getString("mobile"));
                        repair.setType(jsonData.getString("type"));
                        repair.setType_cn(jsonData.getString("type_cn"));
                        repair.setAddress(jsonData.getString("address"));
                        repair.setDescription(jsonData.getString("description"));
                        repair.setStarttime(jsonData.getString("starttime"));
                        repair.setEndtime(jsonData.getString("endtime"));
                        repair.setIs_replace(jsonData.getString("is_replace"));
                        repair.setAdmin_id(jsonData.getString("admin_id"));
                        repair.setAdmin_name(jsonData.getString("admin_name"));
                        repair.setReply(jsonData.getString("reply"));
                        repair.setReply_time(jsonData.getString("reply_time"));
                        repair.setStatus(jsonData.getString("status"));
                        repair.setOvertime(jsonData.getString("overtime"));
                        repair.setAddtime(jsonData.getString("addtime"));
                        repair.setTotal_Pages(jsonData.getString("total_Pages"));
                        propertyRepairs.add(repair);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return propertyRepairs;
    }

    public List<PropertyRepair> getJiaoyu(String json) {
        List<PropertyRepair> propertyRepairs = new ArrayList<PropertyRepair>();
        JSONObject jsonObject, jsonData = null;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if(status.equals("1")){
                if(!StringUtils.isEmpty(data)){
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PropertyRepair repair = new PropertyRepair();
                        jsonData = jsonArray.getJSONObject(i);
                        repair.setId(jsonData.getString("id"));
                        repair.setUid(jsonData.getString("uid"));
                        repair.setA_id(jsonData.getString("a_id"));
                        repair.setTitle(jsonData.getString("title"));
                        repair.setPicture(jsonData.getString("picture"));
                        repair.setPersonal_surplus(jsonData.getString("personal_surplus"));
                        repair.setCost(jsonData.getString("cost"));
                        repair.setAddtime(jsonData.getString("addtime"));
                        repair.setCost_cn(jsonData.getString("cost_cn"));
                        repair.setOutside_url(jsonData.getString("outside_url"));
                        repair.setMenu_name(jsonData.getString("menu_name"));
                        repair.setEnroll_end(jsonData.getString("enroll_end"));
                        repair.setIntroduction(jsonData.getString("introduction"));
                        repair.setStatus(jsonData.getString("status"));
                        repair.setTotal_Pages(jsonData.getString("total_Pages"));
                        propertyRepairs.add(repair);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return propertyRepairs;
    }
    // 获取个人信息
    public PersoninfoBean getinfo(String json) {
        PersoninfoBean info = new PersoninfoBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                info.setUid(obj.getString("uid"));
                info.setUsername(obj.getString("username"));
                info.setAvatars(obj.getString("avatars"));
                info.setNickname(obj.getString("nickname"));
                info.setFullname(obj.getString("fullname"));
                info.setGuest_num(obj.getString("guest_num"));
                info.setDecoration_num(obj.getString("decoration_num"));
                info.setCart_num(obj.getString("cart_num"));
                info.setOrder_num(obj.getString("order_num"));
                info.setService_num(obj.getString("service_num"));
                info.setSocial_num(obj.getString("social_num"));
                info.setStatus(obj.getString("status"));
                info.setSex(obj.getString("sex"));
                info.setBirthday(obj.getString("birthday"));
                info.setUtype(obj.getString("utype"));
                info.setHotline(obj.getString("hotline"));
                info.setIs_bind_property(obj.getString("is_bind_property"));
//                info.setOrder_accept(obj.getString("order_accept"));
//                info.setCoupon_num(obj.getString("coupon_num"));
                //               String address = obj.getString("house_address");
            /*    if (!address.equals("")) {
                    JSONObject objaddress = new JSONObject(address);
                    PersoninfoBean infoaddress = new PersoninfoBean();
                    infoaddress.setCommunity_cn(objaddress.getString("community_cn"));
                    infoaddress.setRegion_cn(objaddress.getString("region_cn"));
                    infoaddress.setDoorplate(objaddress.getString("doorplate"));
                    info.setHouse_address(infoaddress);
                } else {
                    info.setHouse_address(null);
                }*/
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 获取个人信息1103
    public PersoninfoBean getinfo3(String json) {
        PersoninfoBean info = new PersoninfoBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                info.setUid(obj.getString("uid"));
                info.setUsername(obj.getString("username"));
                info.setFullname(obj.getString("fullname"));
                info.setAvatars(obj.getString("avatars"));
                info.setNickname(obj.getString("nickname"));
                info.setSex(obj.getString("sex"));
                info.setBirthday(obj.getString("birthday"));
                info.setUtype(obj.getString("utype"));
                info.setStatus(obj.getString("status"));
                info.setIs_bind_property(obj.getString("is_bind_property"));
                info.setHotline(obj.getString("hotline"));
                /*info.setStatus_3(obj.getString("status_3"));
                info.setStatus_4(obj.getString("status_4"));
                info.setStatus_6(obj.getString("status_6"));*/
//              info.setIs_user_archives(obj.getString("is_user_archives"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 选择商铺
    public List<CityBean> getSH(String json) {
        List<CityBean> citList = new ArrayList<CityBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    CityBean info = new CityBean();
                    JSONObject obj = array.getJSONObject(i);
                    info.setId(obj.getString("id"));
                    info.setMerchant_name(obj.getString("merchant_name"));
                    info.setUid(obj.getString("uid"));
                    citList.add(info);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return citList;
    }

    public CouponBean getCoupon40List(String json) {
        CouponBean coupon = new CouponBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject jsonData = new JSONObject(data);
                String myCouponListStr = jsonData.getString("my_coupon_list");
                String coupon_listStr = jsonData.getString("coupon_list");
                List<CouponBean> myCoupons = new ArrayList<>();
                if (!StringUtils.isEmpty(myCouponListStr)) {
                    JSONArray objarray = new JSONArray(myCouponListStr);
                    for (int i = 0; i < objarray.length(); i++) {
                        JSONObject obj = objarray.getJSONObject(i);
                        CouponBean myCoupon = new CouponBean();
                        myCoupon.setId(obj.getString("id"));
                        myCoupon.setDisplay(obj.getString("display"));
                        myCoupon.setName(obj.getString("name"));
                        myCoupon.setCategory_id(obj.getString("category_id"));
                        myCoupon.setCategory_name(obj.getString("category_name"));
                        myCoupon.setRule_id(obj.getString("rule_id"));
                        myCoupon.setRule_name(obj.getString("rule_name"));
                        myCoupon.setShop_cate(obj.getString("shop_cate"));
                        myCoupon.setShop_id(obj.getString("shop_id"));
                        myCoupon.setAmount(obj.getString("amount"));
                        myCoupon.setFulfil_amount(obj.getString("fulfil_amount"));
                        myCoupon.setTotal_num(obj.getString("total_num"));
                        myCoupon.setUsed_num(obj.getString("used_num"));
                        myCoupon.setCondition(obj.getString("condition"));
                        myCoupon.setUserule(obj.getString("userule"));
                        myCoupon.setRepeat(obj.getString("repeat"));
                        myCoupon.setAddress(obj.getString("address"));
                        myCoupon.setTopclass(obj.getString("topclass"));
                        myCoupon.setTopclass_cn(obj.getString("topclass_cn"));
                        myCoupon.setCategory(obj.getString("category"));
                        myCoupon.setCategory_cn(obj.getString("category_cn"));
                        myCoupon.setSubclass(obj.getString("subclass"));
                        myCoupon.setSubclass_cn(obj.getString("subclass_cn"));
                        myCoupon.setPhoto(obj.getString("photo"));
                        myCoupon.setBox_img(obj.getString("box_img"));
                        myCoupon.setShare_amount(obj.getString("share_amount"));
                        myCoupon.setOrderby(obj.getString("orderby"));
                        myCoupon.setStartime(obj.getString("startime"));
                        myCoupon.setEndtime(obj.getString("endtime"));
                        myCoupon.setAddtime(obj.getString("addtime"));
                        myCoupon.setM_c_id(obj.getString("m_c_id"));
                        myCoupons.add(myCoupon);
                    }
                    coupon.setMy_coupon_list(myCoupons);
                }

                List<CouponBean> couponlists = new ArrayList<>();
                if (!StringUtils.isEmpty(coupon_listStr)) {
                    JSONArray objarray = new JSONArray(coupon_listStr);
                    for (int i = 0; i < objarray.length(); i++) {
                        JSONObject obj = objarray.getJSONObject(i);
                        CouponBean couponList = new CouponBean();
                        couponList.setId(obj.getString("id"));
                        couponList.setDisplay(obj.getString("display"));
                        couponList.setName(obj.getString("name"));
                        couponList.setCategory_id(obj.getString("category_id"));
                        couponList.setCategory_name(obj.getString("category_name"));
                        couponList.setRule_id(obj.getString("rule_id"));
                        couponList.setRule_name(obj.getString("rule_name"));
                        couponList.setShop_cate(obj.getString("shop_cate"));
                        couponList.setShop_id(obj.getString("shop_id"));
                        couponList.setAmount(obj.getString("amount"));
                        couponList.setFulfil_amount(obj.getString("fulfil_amount"));
                        couponList.setTotal_num(obj.getString("total_num"));
                        couponList.setUsed_num(obj.getString("used_num"));
                        couponList.setCondition(obj.getString("condition"));
                        couponList.setUserule(obj.getString("userule"));
                        couponList.setRepeat(obj.getString("repeat"));
                        couponList.setAddress(obj.getString("address"));
                        couponList.setTopclass(obj.getString("topclass"));
                        couponList.setTopclass_cn(obj.getString("topclass_cn"));
                        couponList.setCategory(obj.getString("category"));
                        couponList.setCategory_cn(obj.getString("category_cn"));
                        couponList.setSubclass(obj.getString("subclass"));
                        couponList.setSubclass_cn(obj.getString("subclass_cn"));
                        couponList.setPhoto(obj.getString("photo"));
                        couponList.setBox_img(obj.getString("box_img"));
                        couponList.setShare_amount(obj.getString("share_amount"));
                        couponList.setOrderby(obj.getString("orderby"));
                        couponList.setStartime(obj.getString("startime"));
                        couponList.setEndtime(obj.getString("endtime"));
                        couponList.setAddtime(obj.getString("addtime"));
                        couponlists.add(couponList);
                    }
                    coupon.setCoupon_list(couponlists);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return coupon;
    }

    public CouponBean getCoupon40Detail(String json) {
        CouponBean myCoupon = new CouponBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                myCoupon.setId(obj.getString("id"));
                myCoupon.setDisplay(obj.getString("display"));
                myCoupon.setName(obj.getString("name"));
                myCoupon.setCategory_id(obj.getString("category_id"));
                myCoupon.setCategory_name(obj.getString("category_name"));
                myCoupon.setRule_id(obj.getString("rule_id"));
                myCoupon.setRule_name(obj.getString("rule_name"));
                myCoupon.setShop_cate(obj.getString("shop_cate"));
                myCoupon.setShop_id(obj.getString("shop_id"));
                myCoupon.setAmount(obj.getString("amount"));
                myCoupon.setFulfil_amount(obj.getString("fulfil_amount"));
                myCoupon.setTotal_num(obj.getString("total_num"));
                myCoupon.setUsed_num(obj.getString("used_num"));
                myCoupon.setCondition(obj.getString("condition"));
                myCoupon.setUserule(obj.getString("userule"));
                myCoupon.setRepeat(obj.getString("repeat"));
                myCoupon.setAddress(obj.getString("address"));
                myCoupon.setTopclass(obj.getString("topclass"));
                myCoupon.setTopclass_cn(obj.getString("topclass_cn"));
                myCoupon.setCategory(obj.getString("category"));
                myCoupon.setCategory_cn(obj.getString("category_cn"));
                myCoupon.setSubclass(obj.getString("subclass"));
                myCoupon.setSubclass_cn(obj.getString("subclass_cn"));
                myCoupon.setPhoto(obj.getString("photo"));
                myCoupon.setBox_img(obj.getString("box_img"));
                myCoupon.setShare_amount(obj.getString("share_amount"));
                myCoupon.setOrderby(obj.getString("orderby"));
                myCoupon.setStartime(obj.getString("startime"));
                myCoupon.setEndtime(obj.getString("endtime"));
                myCoupon.setAddtime(obj.getString("addtime"));
                myCoupon.setCoupon_status(obj.getString("coupon_status"));
                myCoupon.setImg_size(obj.getString("img_size"));
                myCoupon.setM_c_id(obj.getString("m_c_id"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return myCoupon;
    }

    public List<CouponBean> getCoupon40Recording(String json) {
        List<CouponBean> coupons = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray objarray = new JSONArray(data);
                for (int i = 0; i < objarray.length(); i++) {
                    JSONObject obj = objarray.getJSONObject(i);
                    CouponBean coupon = new CouponBean();
                    coupon.setId(obj.getString("id"));
                    coupon.setStatus(obj.getString("status"));
                    coupon.setUid(obj.getString("uid"));
                    coupon.setUtype(obj.getString("utype"));
                    coupon.setUsername(obj.getString("username"));
                    coupon.setC_id(obj.getString("c_id"));
                    coupon.setName(obj.getString("name"));
                    coupon.setCategory_id(obj.getString("category_id"));
                    coupon.setCategory_name(obj.getString("category_name"));
                    coupon.setTotal_num(obj.getString("total_num"));
                    coupon.setAmount(obj.getString("amount"));
                    coupon.setFulfil_amount(obj.getString("fulfil_amount"));
                    coupon.setTopclass(obj.getString("topclass"));
                    coupon.setTopclass_cn(obj.getString("topclass_cn"));
                    coupon.setCategory(obj.getString("category"));
                    coupon.setCategory_cn(obj.getString("category_cn"));
                    coupon.setSubclass(obj.getString("subclass"));
                    coupon.setSubclass_cn(obj.getString("subclass_cn"));
                    coupon.setStartime(obj.getString("startime"));
                    coupon.setEndtime(obj.getString("endtime"));
                    coupon.setUsedtime(obj.getString("usedtime"));
                    coupon.setAddtime(obj.getString("addtime"));
                    coupon.setPhoto(obj.getString("photo"));
                    coupon.setCondition(obj.getString("condition"));
                    coupon.setStatus_cn(obj.getString("status_cn"));
                    coupons.add(coupon);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return coupons;
    }

    // 我的优惠券列表接口
    public CouponBean getOrderCouPon(String json) {
        CouponBean type = new CouponBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                type.setAmount(obj.getString("amount"));
                type.setName(obj.getString("name"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return type;
    }

    // 版本更新接口
    public PayInfoBean getApk(String json) {
        PayInfoBean info = new PayInfoBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                info.setPath(obj.getString("path"));
                info.setCompel(obj.getString("compel"));
                info.setMgs(obj.getString("mgs"));
            } else {
                info.setPath(null);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public String getResult(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(json);
        String status = jsonObject.getString("status");
        /*String data = jsonObject.getString("data");
        String msg = jsonObject.getString("msg");*/
        if (StringUtils.isEquals(status, "1")) {
            return "1";
        } else {
            return status;
        }
    }

    // 设置状态
    public String setStatus(String json) {
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
    public ActivityDetailBean getActDetail(String json) {
        ActivityDetailBean detail = new ActivityDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (status.equals("1")) {
                JSONObject jsonData = new JSONObject(data);
                detail.setId(jsonData.getString("id"));
                detail.setUid(jsonData.getString("uid"));
                detail.setCost(jsonData.getString("cost"));
                detail.setOrder_number(jsonData.getString("order_number"));
                detail.setName(jsonData.getString("name"));
                detail.setIs_pay(jsonData.getString("is_pay"));
                detail.setTitle(jsonData.getString("title"));
                detail.setPhone(jsonData.getString("phone"));
                detail.setA_id(jsonData.getString("a_id"));
                detail.setTel_phone(jsonData.getString("tel_phone"));
                detail.setAddress(jsonData.getString("address"));
                detail.setStatus(jsonData.optString("status"));
                detail.setStart_time(jsonData.optString("start_time"));
                detail.setPersonal_num(jsonData.optString("personal_num"));
                detail.setAddtime(jsonData.optString("addtime"));
                detail.setIntroduction(jsonData.optString("introduction"));
                detail.setEnd_time(jsonData.optString("end_time"));

                //回复
                String reply_list = jsonData.optString("r_list");
                if (!StringUtils.isEmpty(reply_list)) {
                    JSONArray jsonAryReply = new JSONArray(reply_list);
                    List<Reply> replys = new ArrayList<Reply>();
                    for (int i = 0; i < jsonAryReply.length(); i++) {
                        Reply reply = new Reply();
                        JSONObject jsonReply = jsonAryReply.getJSONObject(i);
                        reply.setRepair(jsonReply.getString("repair"));
                        reply.setAddtime(jsonReply.getString("addtime"));
                        reply.setIns_name(jsonReply.getString("ins_name"));
                        reply.setCom_name(jsonReply.getString("com_name"));
                        reply.setPosition(jsonReply.getString("position"));
                        reply.setAvatars(jsonReply.getString("avatars"));
                        replys.add(reply);
                    }
                    detail.setR_list(replys);
                } else {
                    detail.setR_list(null);
                }

            } else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;
    }

    // 新购物订单列表数据
    public List<ShopOrderBeanType> getXOrderList(String json) {
        List<ShopOrderBeanType> info = new ArrayList<ShopOrderBeanType>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray objarray = new JSONArray(data);
                for (int i = 0; i < objarray.length(); i++) {
                    JSONObject obj = objarray.getJSONObject(i);
                    ShopOrderBeanType type = new ShopOrderBeanType();
                    type.setId(obj.getString("id"));
                    type.setOrder_number(obj.getString("order_number"));
                    type.setPrice_total(obj.getString("price_total"));
                    type.setPro_num(obj.getString("pro_num"));
                    type.setTotal_Pages(obj.getString("total_Pages"));
                    // 商品列表
                    String add = obj.getString("img");
                    if(!StringUtils.isEmpty(add)){
                        List<BannerBean> tagBean = new ArrayList<BannerBean>();
                        JSONArray tagobj = new JSONArray(add);
                        for (int j = 0; j < tagobj.length(); j++) {
                            JSONObject addobj = tagobj.getJSONObject(j);
                            BannerBean tag = new BannerBean();
                            tag.setImg(addobj.getString("img"));
                            tag.setInfo_id(addobj.getString("info_id"));
                            tagBean.add(tag);
                        }
                        type.setImg(tagBean);

                    }
                    info.add(type);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 新购物订单详情数据
    public List<XorderDetailBean> getXOrderDetail(String json) {
        List<XorderDetailBean> info = new ArrayList<XorderDetailBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray objarray = new JSONArray(data);
                for (int i = 0; i < objarray.length(); i++) {
                    JSONObject obj = objarray.getJSONObject(i);
                    XorderDetailBean type = new XorderDetailBean();
                    type.setId(obj.getString("id"));
                    type.setOid(obj.getString("oid"));
                    type.setStatus(obj.getString("status"));
                    type.setP_id(obj.getString("p_id"));
                    type.setPrice(obj.getString("price"));
                    type.setNumber(obj.getString("number"));
                    type.setIs_pay(obj.getString("is_pay"));
                    type.setPay_type(obj.getString("pay_type"));
                    type.setPay_time(obj.getString("pay_time"));
                    type.setRefundamount(obj.getString("refundamount"));
                    type.setAddtime(obj.getString("addtime"));
                    type.setSendtime(obj.getString("sendtime"));
                    type.setRefundtime(obj.getString("refundtime"));
                    type.setOvertime(obj.getString("overtime"));
                    type.setTitle_thumb_img(obj.getString("title_thumb_img"));
                    type.setTitle(obj.getString("title"));
                    type.setTitle_img(obj.getString("title_img"));
                    type.setM_c_id(obj.getString("m_c_id"));
                    type.setM_c_name(obj.getString("m_c_name"));
                    type.setM_c_amount(obj.getString("m_c_amount"));
                    type.setDescription(obj.getString("description"));
                    type.setIs_shop(obj.getString("is_shop"));
                    info.add(type);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    //账户管理  temp为1时是师父端钱包账户   为2是个人中心账户
    public List<ListBean> myMoney(String json, String temp) {
        List<ListBean> citList = new ArrayList<ListBean>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                System.out.println("obj====" + array);
                for (int i = 0; i < array.length(); i++) {
                    ListBean info = new ListBean();
                    JSONObject obj = array.getJSONObject(i);
                    info.setId(obj.getString("id"));
                    if (temp.equals("2")) {
                        info.setAddress(obj.getString("address"));
                        info.setAddtime(obj.getString("addtime"));
                    }
                    info.setLog_title(obj.getString("log_title"));
                    info.setLog_description(obj.getString("log_description"));
                    info.setAmount(obj.getString("amount"));
                    info.setAtype(obj.getString("atype"));
                    info.setPaytype(obj.getString("paytype"));
                    info.setPaytime(obj.getString("paytime"));
                    info.setTotal_amount(obj.getString("total_amount"));
                    info.setTotal_Pages(obj.getString("total_Pages"));
                    citList.add(info);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return citList;
    }
}
