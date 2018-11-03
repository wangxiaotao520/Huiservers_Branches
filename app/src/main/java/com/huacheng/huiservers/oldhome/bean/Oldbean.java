package com.huacheng.huiservers.oldhome.bean;

import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

/**
 * 类：
 * 时间：2018/6/1 09:40
 * 功能描述:Huiservers
 */
public class Oldbean {

    private List<BannerBean> s_list;
    private Oldbean s_url;
    private Oldbean p_url;
    private List<ShopIndexBean> p_list;
    private String path;
    private Oldbean param;
    private String c_id;
    private String id;
    private String community_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BannerBean> getS_list() {
        return s_list;
    }

    public void setS_list(List<BannerBean> s_list) {
        this.s_list = s_list;
    }

    public Oldbean getS_url() {
        return s_url;
    }

    public void setS_url(Oldbean s_url) {
        this.s_url = s_url;
    }

    public Oldbean getP_url() {
        return p_url;
    }

    public void setP_url(Oldbean p_url) {
        this.p_url = p_url;
    }

    public List<ShopIndexBean> getP_list() {
        return p_list;
    }

    public void setP_list(List<ShopIndexBean> p_list) {
        this.p_list = p_list;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Oldbean getParam() {
        return param;
    }

    public void setParam(Oldbean param) {
        this.param = param;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }
}
