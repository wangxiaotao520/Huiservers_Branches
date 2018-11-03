package com.huacheng.huiservers.servicenew.model;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/5 15:25
 */
public class ModelInfoBean {
    /**
     * id : 115
     * name : 武当派
     * index_img :
     * logo : public/upload/ins/20180822/5b7cc0b164f83.jpg
     * category : [{"category":"7","category_cn":"清洗玻璃"},{"category":"24","category_cn":"保养车"},{"category":"30","category_cn":"吃药"}]
     */

    private String id;
    private String name;
    private String index_img;
    private String logo;
    private List<ModelInfoCategoryBean> category;

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

    public String getIndex_img() {
        return index_img;
    }

    public void setIndex_img(String index_img) {
        this.index_img = index_img;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ModelInfoCategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<ModelInfoCategoryBean> category) {
        this.category = category;
    }
}
