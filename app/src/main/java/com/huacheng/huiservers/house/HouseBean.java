package com.huacheng.huiservers.house;

import com.huacheng.huiservers.wuye.bean.WuYeBean;

import java.io.Serializable;
import java.util.List;

/**
 * 类：
 * 时间：2018/3/20 21:29
 * 功能描述:Huiservers
 */
public class HouseBean implements Serializable {

    private String is_available;
    private String is_available_cn;
    private String id;
    private String title;
    private String article_image;
    private String url;
    private String url_type_id;
    private String url_type_name;
    private String img_size;
    private List<HouseBean> li;
    private HouseBean list;
    private String is_ym;
    private String img;
    private String url_type;
    private String type_name;
    private String com_id_arr;
    private String adv_url;
    private String adv_inside_url;
    private String room_id;
    private String community_name;
    private String address;
    private String per_count;
    private WuYeBean room_info;
    private List<WuYeBean> per_li;
    private List<WuYeBean> per_li_;
    private String content;
    private List<HouseBean> wuye;
    private String bill_id,
            community_id,
            building_name,
            unit,
            code,
            charge_type,
            sumvalue,
            startdate,
            enddate,
            time;

    private String type, info,type_cn;
    private HouseBean shuifei;
    private HouseBean dianfei;
    private HouseBean infoBean;
    private String Total,
            C_time,
            Saccount,
            SMay_acc,
            Daccount,
            DMay_acc,upper_limit;

    private String uid, username,
            nickname,
            number_suffix,
            bill_time,
            is_pay,
            addtime,
            uptime,
            total_Pages;

    private String house_type;

    public String getIs_ym() {
        return is_ym;
    }

    public void setIs_ym(String is_ym) {
        this.is_ym = is_ym;
    }

    public String getType_cn() {
        return type_cn;
    }

    public void setType_cn(String type_cn) {
        this.type_cn = type_cn;
    }

    public String getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(String upper_limit) {
        this.upper_limit = upper_limit;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getIs_available_cn() {
        return is_available_cn;
    }

    public void setIs_available_cn(String is_available_cn) {
        this.is_available_cn = is_available_cn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNumber_suffix() {
        return number_suffix;
    }

    public void setNumber_suffix(String number_suffix) {
        this.number_suffix = number_suffix;
    }

    public String getBill_time() {
        return bill_time;
    }

    public void setBill_time(String bill_time) {
        this.bill_time = bill_time;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(String total_Pages) {
        this.total_Pages = total_Pages;
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

    public String getArticle_image() {
        return article_image;
    }

    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getImg_size() {
        return img_size;
    }

    public void setImg_size(String img_size) {
        this.img_size = img_size;
    }

    public List<HouseBean> getLi() {
        return li;
    }

    public void setLi(List<HouseBean> li) {
        this.li = li;
    }

    public HouseBean getList() {
        return list;
    }

    public void setList(HouseBean list) {
        this.list = list;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    private String order_num, charte_type_id, pay_time, pay_type;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getCharte_type_id() {
        return charte_type_id;
    }

    public void setCharte_type_id(String charte_type_id) {
        this.charte_type_id = charte_type_id;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public HouseBean getShuifei() {
        return shuifei;
    }

    public void setShuifei(HouseBean shuifei) {
        this.shuifei = shuifei;
    }

    public String getSaccount() {
        return Saccount;
    }

    public void setSaccount(String saccount) {
        Saccount = saccount;
    }

    public String getSMay_acc() {
        return SMay_acc;
    }

    public void setSMay_acc(String SMay_acc) {
        this.SMay_acc = SMay_acc;
    }

    public List<HouseBean> getWuye() {
        return wuye;
    }

    public void setWuye(List<HouseBean> wuye) {
        this.wuye = wuye;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public HouseBean getDianfei() {
        return dianfei;
    }

    public void setDianfei(HouseBean dianfei) {
        this.dianfei = dianfei;
    }

    public HouseBean getInfoBean() {
        return infoBean;
    }

    public void setInfoBean(HouseBean infoBean) {
        this.infoBean = infoBean;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getC_time() {
        return C_time;
    }

    public void setC_time(String c_time) {
        C_time = c_time;
    }

    public String getDaccount() {
        return Daccount;
    }

    public void setDaccount(String daccount) {
        Daccount = daccount;
    }

    public String getDMay_acc() {
        return DMay_acc;
    }

    public void setDMay_acc(String DMay_acc) {
        this.DMay_acc = DMay_acc;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(String charge_type) {
        this.charge_type = charge_type;
    }

    public String getSumvalue() {
        return sumvalue;
    }

    public void setSumvalue(String sumvalue) {
        this.sumvalue = sumvalue;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public WuYeBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(WuYeBean room_info) {
        this.room_info = room_info;
    }

    public List<WuYeBean> getPer_li() {
        return per_li;
    }

    public void setPer_li(List<WuYeBean> per_li) {
        this.per_li = per_li;
    }

    public List<WuYeBean> getPer_li_() {
        return per_li_;
    }

    public void setPer_li_(List<WuYeBean> per_li_) {
        this.per_li_ = per_li_;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPer_count() {
        return per_count;
    }

    public void setPer_count(String per_count) {
        this.per_count = per_count;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }
}
