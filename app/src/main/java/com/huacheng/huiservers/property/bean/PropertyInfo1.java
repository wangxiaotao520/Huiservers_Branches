package com.huacheng.huiservers.property.bean;

import java.util.List;

/**
 * 类描述：
 * 时间：2018/8/11 10:41
 * created by DFF
 */
public class PropertyInfo1 {
    private String name;
    private String mp1;
    private String is_ym;
    private String nickname;
    private String order_num;
    private String address;
    private String addtime;
    private List<List<WuyeListBean>> list;
    private double tot_sumvalue;

    private ModelPropertyWyInfo.RoomInfoBean info;

    public ModelPropertyWyInfo.RoomInfoBean getInfo() {
        return info;
    }

    public void setInfo(ModelPropertyWyInfo.RoomInfoBean info) {
        this.info = info;
    }

    public List<List<WuyeListBean>> getList() {
        return list;
    }

    public void setList(List<List<WuyeListBean>> list) {
        this.list = list;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public double getTot_sumvalue() {
        return tot_sumvalue;
    }

    public void setTot_sumvalue(double tot_sumvalue) {
        this.tot_sumvalue = tot_sumvalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMp1() {
        return mp1;
    }

    public void setMp1(String mp1) {
        this.mp1 = mp1;
    }

    public String getIs_ym() {
        return is_ym;
    }

    public void setIs_ym(String is_ym) {
        this.is_ym = is_ym;
    }
}
