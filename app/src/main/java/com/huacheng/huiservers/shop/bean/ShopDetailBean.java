package com.huacheng.huiservers.shop.bean;

import java.util.List;

public class ShopDetailBean {
    private String id;//商品id
    private String title;//产品标题
    private String description;//商品描述
    private String inventory;//库存
    private String send_shop;//配送超市
    private String tagid;//商品标签id
    private String tagname;//商品标签名称
    private String title_img;//商品头图
    private String price;//标签对应的价格
    private String original;//原价价格
    private String is_favorite;//是否 收藏
    private String cart_num;//购物车 未支付数
    private String s_o_id;//购物订单id
    private String address;//地址
    private String address_id;//地址
    private String contact;//联系人
    private String mobile;//联系方式
    private String num;//订单中商品件数
    private String amount;//总金额
    private String exist_hours;//师傅打烊
    private String is_coupon;//判断是否有优惠券
    private String pro_num;//包裹件数
    private String send_amount;//配送费
    private List<BannerBean> goods_tag;//中部商家保证
    private List<BannerBean> imgs;//详情商品轮播图片路径
    private List<BannerBean> recommend;//推荐商品
    private List<DataBean> list;//购物车列表
    private String unit;
    private String order_id;
    private List<ConfirmBean> pro_data;
    private List<ShopMainBean> score;
    private String advertorial;
    private String shop_id_str;
    private String shop_cate_id;
    private String shop_cate_stime;
    private String shop_cate_etime;
    private String is_hot, is_new, order_num;
    private String discount, distance_start, distance_end;
    private long current_times;//倒计时用来的时间
    private long current_times1;//倒计时用的时间，开启第二个倒计时的time

    public long getCurrent_times1() {
        return current_times1;
    }

    public void setCurrent_times1(long current_times1) {
        this.current_times1 = current_times1;
    }

    public long getCurrent_times() {
        return current_times;
    }

    public void setCurrent_times(long current_times) {
        this.current_times = current_times;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

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

    private int total_Pages;

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
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

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }


    public String getShop_cate_id() {
        return shop_cate_id;
    }

    public void setShop_cate_id(String shop_cate_id) {
        this.shop_cate_id = shop_cate_id;
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

    public String getShop_id_str() {
        return shop_id_str;
    }

    public void setShop_id_str(String shop_id_str) {
        this.shop_id_str = shop_id_str;
    }

    public String getAdvertorial() {
        return advertorial;
    }

    public void setAdvertorial(String advertorial) {
        this.advertorial = advertorial;
    }


    public List<ShopMainBean> getScore() {
        return score;
    }

    public void setScore(List<ShopMainBean> score) {
        this.score = score;
    }


    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getIs_coupon() {
        return is_coupon;
    }

    public void setIs_coupon(String is_coupon) {
        this.is_coupon = is_coupon;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPro_num() {
        return pro_num;
    }

    public void setPro_num(String pro_num) {
        this.pro_num = pro_num;
    }

    public String getSend_amount() {
        return send_amount;
    }

    public void setSend_amount(String send_amount) {
        this.send_amount = send_amount;
    }

    public List<ConfirmBean> getPro_data() {
        return pro_data;
    }

    public void setPro_data(List<ConfirmBean> pro_data) {
        this.pro_data = pro_data;
    }

    private boolean isSelect;


    public String getExist_hours() {
        return exist_hours;
    }

    public void setExist_hours(String exist_hours) {
        this.exist_hours = exist_hours;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getS_o_id() {
        return s_o_id;
    }

    public void setS_o_id(String s_o_id) {
        this.s_o_id = s_o_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSend_shop() {
        return send_shop;
    }

    public void setSend_shop(String send_shop) {
        this.send_shop = send_shop;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getCart_num() {
        return cart_num;
    }

    public void setCart_num(String cart_num) {
        this.cart_num = cart_num;
    }

    public List<BannerBean> getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(List<BannerBean> goods_tag) {
        this.goods_tag = goods_tag;
    }

    public List<BannerBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<BannerBean> recommend) {
        this.recommend = recommend;
    }

    public List<BannerBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<BannerBean> imgs) {
        this.imgs = imgs;
    }


}
