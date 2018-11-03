package com.huacheng.huiservers.servicenew.model;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/5 15:24
 */
public class ModelCategoryServiceBean {
    /**
     * id : 24
     * name : 包养车
     * service : [{"id":"31","title":"太极保养车","title_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png"}]
     */

    private String id;
    private String name;
    private List<ModelServiceItem> service;

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

    public List<ModelServiceItem> getService() {
        return service;
    }

    public void setService(List<ModelServiceItem> service) {
        this.service = service;
    }
}
