package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 店铺首页
 * created by wangxiaotao
 * 2018/9/5 0005 下午 6:20
 */
public class ModelStore implements Serializable{

    private String id;
    private String logo;
    private String name;
    private String telphone;
    private String index_img;
    private String serviceCount;
    private String commentsCount;
    private List<CategoryBean> category;
    private List<ModelServiceItem> service;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getIndex_img() {
        return index_img;
    }

    public void setIndex_img(String index_img) {
        this.index_img = index_img;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public List<ModelServiceItem> getService() {
        return service;
    }

    public void setService(List<ModelServiceItem> service) {
        this.service = service;
    }

}
