package com.huacheng.huiservers.fragment.bean;

import com.huacheng.huiservers.servicenew.model.ModelCategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelCategoryServiceBean;
import com.huacheng.huiservers.servicenew.model.ModelInfoBean;
import com.huacheng.huiservers.servicenew.model.ModelItemBean;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/4 16:39
 */
public class ModelServiceIndex {
    private List<ModelCategoryBean> category;
    private List<?> activity;
    private List<ModelCategoryServiceBean> category_service;
    private List<ModelInfoBean> info;
    private List<ModelItemBean> item;

    public List<ModelCategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<ModelCategoryBean> category) {
        this.category = category;
    }

    public List<?> getActivity() {
        return activity;
    }

    public void setActivity(List<?> activity) {
        this.activity = activity;
    }

    public List<ModelCategoryServiceBean> getCategory_service() {
        return category_service;
    }

    public void setCategory_service(List<ModelCategoryServiceBean> category_service) {
        this.category_service = category_service;
    }

    public List<ModelInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<ModelInfoBean> info) {
        this.info = info;
    }

    public List<ModelItemBean> getItem() {
        return item;
    }

    public void setItem(List<ModelItemBean> item) {
        this.item = item;
    }

}
