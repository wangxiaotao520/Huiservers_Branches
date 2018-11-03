package com.huacheng.huiservers.center.bean;

import java.util.List;

public class CouponBean {

    List<CouponBean> my_coupon_list;//我的优惠券
    List<CouponBean> coupon_list;//优惠券列表
    private String id,
            display,
            rule_id,
            rule_name,
            shop_cate,
            shop_id,
            fulfil_amount,
            used_num,
            repeat,
            topclass,
            category,
            subclass,
            share_amount,
            orderby,
            addtime;

    private String c_id;
    private String m_c_id;
    private String name;
    private String amount;
    private String category_id;
    private String category_name;
    private String total_num;
    private String photo;
    private String topclass_cn;
    private String category_cn;
    private String subclass_cn;
    private String startime;
    private String endtime;
    private String use_status;
    private String total_Pages;
    private String address;
    private String condition;
    private String userule;
    private String box_img;

    public String getImg_size() {
        return img_size;
    }

    public void setImg_size(String img_size) {
        this.img_size = img_size;
    }

    private String img_size;

    private String coupon_status;

    private String status, uid, utype,
            username,usedtime,status_cn;

    public String getStatus_cn() {
        return status_cn;
    }

    public void setStatus_cn(String status_cn) {
        this.status_cn = status_cn;
    }

    public String getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(String usedtime) {
        this.usedtime = usedtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }

    public List<CouponBean> getMy_coupon_list() {
        return my_coupon_list;
    }

    public void setMy_coupon_list(List<CouponBean> my_coupon_list) {
        this.my_coupon_list = my_coupon_list;
    }

    public List<CouponBean> getCoupon_list() {
        return coupon_list;
    }

    public void setCoupon_list(List<CouponBean> coupon_list) {
        this.coupon_list = coupon_list;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public String getShop_cate() {
        return shop_cate;
    }

    public void setShop_cate(String shop_cate) {
        this.shop_cate = shop_cate;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getFulfil_amount() {
        return fulfil_amount;
    }

    public void setFulfil_amount(String fulfil_amount) {
        this.fulfil_amount = fulfil_amount;
    }

    public String getUsed_num() {
        return used_num;
    }

    public void setUsed_num(String used_num) {
        this.used_num = used_num;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTopclass() {
        return topclass;
    }

    public void setTopclass(String topclass) {
        this.topclass = topclass;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public String getShare_amount() {
        return share_amount;
    }

    public void setShare_amount(String share_amount) {
        this.share_amount = share_amount;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getBox_img() {
        return box_img;
    }

    public void setBox_img(String box_img) {
        this.box_img = box_img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getUserule() {
        return userule;
    }

    public void setUserule(String userule) {
        this.userule = userule;
    }

    public String getM_c_id() {
        return m_c_id;
    }

    public void setM_c_id(String m_c_id) {
        this.m_c_id = m_c_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTopclass_cn() {
        return topclass_cn;
    }

    public void setTopclass_cn(String topclass_cn) {
        this.topclass_cn = topclass_cn;
    }

    public String getCategory_cn() {
        return category_cn;
    }

    public void setCategory_cn(String category_cn) {
        this.category_cn = category_cn;
    }

    public String getSubclass_cn() {
        return subclass_cn;
    }

    public void setSubclass_cn(String subclass_cn) {
        this.subclass_cn = subclass_cn;
    }

    public String getStartime() {
        return startime;
    }

    public void setStartime(String startime) {
        this.startime = startime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getUse_status() {
        return use_status;
    }

    public void setUse_status(String use_status) {
        this.use_status = use_status;
    }

    public String getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(String total_Pages) {
        this.total_Pages = total_Pages;
    }


}
