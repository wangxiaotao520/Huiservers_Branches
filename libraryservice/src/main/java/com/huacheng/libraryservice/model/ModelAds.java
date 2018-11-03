package com.huacheng.libraryservice.model;

/**
 * Description: 顶部广告
 * created by wangxiaotao
 * 2018/8/21 0021 上午 11:12
 */
public class ModelAds {
    //首页顶部广告 //商城首页广告
    private String id;
    private String img;
    private String url;
    private String url_type;
    private String type_name;
    private String com_id_arr;
    private String adv_url;
    private String adv_inside_url;
    private String img_size;
    private String url_type_cn;

    public String getUrl_type_cn() {
        return url_type_cn;
    }

    public void setUrl_type_cn(String url_type_cn) {
        this.url_type_cn = url_type_cn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCom_id_arr() {
        return com_id_arr;
    }

    public void setCom_id_arr(String com_id_arr) {
        this.com_id_arr = com_id_arr;
    }

    public String getAdv_url() {
        return adv_url;
    }

    public void setAdv_url(String adv_url) {
        this.adv_url = adv_url;
    }

    public String getAdv_inside_url() {
        return adv_inside_url;
    }

    public void setAdv_inside_url(String adv_inside_url) {
        this.adv_inside_url = adv_inside_url;
    }

    public String getImg_size() {
        return img_size;
    }

    public void setImg_size(String img_size) {
        this.img_size = img_size;
    }
}
