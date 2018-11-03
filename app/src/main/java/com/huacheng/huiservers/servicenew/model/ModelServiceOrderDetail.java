package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;

/**
 * Description:服务订单详情
 * created by wangxiaotao
 * 2018/9/6 0006 上午 9:46
 */
public class ModelServiceOrderDetail implements Serializable{

    private String id;
    private String order_number;
    private String status;
    private long addtime;
    private String address;
    private String description;
    private String w_id;
    private String w_fullname;
    private String s_id;
    private String s_name;
    private String title_img;
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getW_id() {
        return w_id;
    }

    public void setW_id(String w_id) {
        this.w_id = w_id;
    }

    public String getW_fullname() {
        return w_fullname;
    }

    public void setW_fullname(String w_fullname) {
        this.w_fullname = w_fullname;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
