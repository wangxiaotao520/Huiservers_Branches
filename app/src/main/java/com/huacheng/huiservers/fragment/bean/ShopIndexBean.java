package com.huacheng.huiservers.fragment.bean;

import java.util.List;

/**
 * 类：
 * 时间：2018/5/28 20:12
 * 功能描述:Huiservers
 */
public class ShopIndexBean {

    private List<ShopIndexBean> cate_list;
    private List<ShopIndexBean> pro_list;
    private ShopIndexBean pro_discount_list;
    private String id;
    private String parent_id;
    private String cate_name;
    private String cate_img;
    private String icon;
    private String title;
    private String title_img;
    private String title_thumb_img;
    private String cate_tag_id;
    private String send_shop_id;
    private String addtime;
    private String price;
    private String original;
    private String unit;
    private String exist_hours;
    private String is_hot;
    private String is_new;
    private String discount;
    private String distance_start;
    private String distance_end;
    private String tagname;
    private String tagid;
    private String is_limit;
    private String inventory;
    private String shop_cate_stime;
    private String shop_cate_etime;
    private String order_num;
    private List<ShopIndexBean> goods_tag;
    private List<ShopIndexBean> list;
    private String c_img;
    private String c_name;
    private String total_Pages;
    private long  current_times;//倒计时时用来的时间

    public String getDistance_start() {
        return distance_start;
    }

    public void setDistance_start(String distance_start) {
        this.distance_start = distance_start;
    }

    public String getDistance_end() {
        return distance_end;
    }

    public void setDistance_end(String distance_end) {
        this.distance_end = distance_end;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public ShopIndexBean getPro_discount_list() {
        return pro_discount_list;
    }
    public void setPro_discount_list(ShopIndexBean pro_discount_list) {
        this.pro_discount_list = pro_discount_list;
    }
    public String getCate_img() {
        return cate_img;
    }

    public void setCate_img(String cate_img) {
        this.cate_img = cate_img;
    }

    public String getShop_cate_stime() {
        return shop_cate_stime;
    }

    public void setShop_cate_stime(String shop_cate_stime) {
        this.shop_cate_stime = shop_cate_stime;
    }

    public String getShop_cate_etime() {
        return shop_cate_etime;
    }

    public void setShop_cate_etime(String shop_cate_etime) {
        this.shop_cate_etime = shop_cate_etime;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getIs_limit() {
        return is_limit;
    }

    public void setIs_limit(String is_limit) {
        this.is_limit = is_limit;
    }

    public String getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(String total_Pages) {
        this.total_Pages = total_Pages;
    }

    public List<ShopIndexBean> getCate_list() {
        return cate_list;
    }

    public void setCate_list(List<ShopIndexBean> cate_list) {
        this.cate_list = cate_list;
    }

    public List<ShopIndexBean> getPro_list() {
        return pro_list;
    }

    public void setPro_list(List<ShopIndexBean> pro_list) {
        this.pro_list = pro_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getTitle_thumb_img() {
        return title_thumb_img;
    }

    public void setTitle_thumb_img(String title_thumb_img) {
        this.title_thumb_img = title_thumb_img;
    }

    public String getCate_tag_id() {
        return cate_tag_id;
    }

    public void setCate_tag_id(String cate_tag_id) {
        this.cate_tag_id = cate_tag_id;
    }

    public String getSend_shop_id() {
        return send_shop_id;
    }

    public void setSend_shop_id(String send_shop_id) {
        this.send_shop_id = send_shop_id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExist_hours() {
        return exist_hours;
    }

    public void setExist_hours(String exist_hours) {
        this.exist_hours = exist_hours;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public List<ShopIndexBean> getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(List<ShopIndexBean> goods_tag) {
        this.goods_tag = goods_tag;
    }

    public List<ShopIndexBean> getList() {
        return list;
    }

    public void setList(List<ShopIndexBean> list) {
        this.list = list;
    }

    public String getC_img() {
        return c_img;
    }

    public void setC_img(String c_img) {
        this.c_img = c_img;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }


    public long getCurrent_times() {
        return current_times;
    }

    public void setCurrent_times(long current_times) {
        this.current_times = current_times;
    }

}
