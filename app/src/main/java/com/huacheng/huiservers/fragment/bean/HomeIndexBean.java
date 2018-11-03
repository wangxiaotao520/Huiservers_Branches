package com.huacheng.huiservers.fragment.bean;

import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

/**
 * 类：
 * 时间：2018/6/2 17:12
 * 功能描述:Huiservers
 */
public class HomeIndexBean {
    private String id;
    private String menu_name;
    private String menu_logo;
    private String url_type;
    private String url_type_cn;
    private String url_id;
    private String addtime;
    private List<HomeIndexBean> menu_list;
    private String bg_img;
    private List<BannerBean> social_list;
    private List<BannerBean> article_list;
    private List<ShopIndexBean> pro_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_logo() {
        return menu_logo;
    }

    public void setMenu_logo(String menu_logo) {
        this.menu_logo = menu_logo;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getUrl_type_cn() {
        return url_type_cn;
    }

    public void setUrl_type_cn(String url_type_cn) {
        this.url_type_cn = url_type_cn;
    }

    public String getUrl_id() {
        return url_id;
    }

    public void setUrl_id(String url_id) {
        this.url_id = url_id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<HomeIndexBean> getMenu_list() {
        return menu_list;
    }

    public void setMenu_list(List<HomeIndexBean> menu_list) {
        this.menu_list = menu_list;
    }

    public String getBg_img() {
        return bg_img;
    }

    public void setBg_img(String bg_img) {
        this.bg_img = bg_img;
    }

    public List<BannerBean> getSocial_list() {
        return social_list;
    }

    public void setSocial_list(List<BannerBean> social_list) {
        this.social_list = social_list;
    }

    public List<BannerBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<BannerBean> article_list) {
        this.article_list = article_list;
    }

    public List<ShopIndexBean> getPro_list() {
        return pro_list;
    }

    public void setPro_list(List<ShopIndexBean> pro_list) {
        this.pro_list = pro_list;
    }
}
