package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;

/**
 * Description: 分类bean
 * created by wangxiaotao
 * 2018/9/5 0005 下午 6:22
 */
public class CategoryBean implements Serializable {
    /**
     * category : 7
     * category_cn : 清洗玻璃
     */

    private String category;
    private String category_cn;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_cn() {
        return category_cn;
    }

    public void setCategory_cn(String category_cn) {
        this.category_cn = category_cn;
    }
}

