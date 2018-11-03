package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;

/**
 * Description:服务列表
 * created by wangxiaotao
 * 2018/9/4 0004 上午 11:44
 */
public class ModelServiceItem implements Serializable {
    private String id;
    private String title;
    private String title_img;
    private String title_thumb_img;
    private String title_img_size;
    private String price;
    private int total_Pages;

    public String getTitle_img_size() {
        return title_img_size;
    }

    public void setTitle_img_size(String title_img_size) {
        this.title_img_size = title_img_size;
    }

    public String getTitle_thumb_img() {
        return title_thumb_img;
    }

    public void setTitle_thumb_img(String title_thumb_img) {
        this.title_thumb_img = title_thumb_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }

}
