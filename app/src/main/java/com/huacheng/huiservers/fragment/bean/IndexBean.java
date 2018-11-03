package com.huacheng.huiservers.fragment.bean;

import com.huacheng.huiservers.shop.bean.BannerBean;

import java.io.Serializable;
import java.util.List;

/**
 * 4.0首页Bean
 * Created by Administrator on 2018/03/28.
 */

public class IndexBean implements Serializable {

    private List<IndexBean> article_list;

    private String
            type_id,
            type_cn,
            article_image,
            istop,
            display,
            order_num,
            uptime,
            url_type_id,
            url_type_name;

    private List<IndexBean> ad_list;

    private String
            img,
            url,
            url_type,
            type_name,
            com_id_arr,
            adv_url,
            adv_inside_url;

    private List<BannerBean> social_list;
    private String
            id,
            uid,
            admin_id,
            c_id,
            c_name,
            title,
            content,
            click,
            reply_num,
            addtime,
            avatars,
            nickname,

    total_Pages;

    public List<BannerBean> img_list;

    public List<BannerBean> getSocial_list() {
        return social_list;
    }

    public void setSocial_list(List<BannerBean> social_list) {
        this.social_list = social_list;
    }

    public List<BannerBean> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<BannerBean> img_list) {
        this.img_list = img_list;
    }

    public List<IndexBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<IndexBean> article_list) {
        this.article_list = article_list;
    }


    public List<IndexBean> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<IndexBean> ad_list) {
        this.ad_list = ad_list;
    }


    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_cn() {
        return type_cn;
    }

    public void setType_cn(String type_cn) {
        this.type_cn = type_cn;
    }

    public String getArticle_image() {
        return article_image;
    }

    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getUrl_type_id() {
        return url_type_id;
    }

    public void setUrl_type_id(String url_type_id) {
        this.url_type_id = url_type_id;
    }

    public String getUrl_type_name() {
        return url_type_name;
    }

    public void setUrl_type_name(String url_type_name) {
        this.url_type_name = url_type_name;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(String total_Pages) {
        this.total_Pages = total_Pages;
    }
}
