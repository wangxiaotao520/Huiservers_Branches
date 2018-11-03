package com.huacheng.huiservers.servicenew.model;

import java.util.List;

/**
 * Description: 商家列表Bean
 * Author: badge
 * Create: 2018/9/7 8:52
 */
public class ModelMerchantList {
    /**
     * id : 122
     * name : DNF
     * logo : public/upload/ins/20180905/5b8fa0f747a43.jpg
     */

    private String id;
    private String name;
    private String logo;
    private List<ModelInfoCategoryBean> category;
    private List<ModelServiceItem> service;

    public List<ModelInfoCategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<ModelInfoCategoryBean> category) {
        this.category = category;
    }

    public List<ModelServiceItem> getService() {
        return service;
    }

    public void setService(List<ModelServiceItem> service) {
        this.service = service;
    }

    private int total_Pages;

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
