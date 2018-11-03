package com.huacheng.huiservers.wuye.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类：
 * 时间：2017/11/29 10:26
 * 功能描述:Huiservers
 */
public class NewWuyeBean implements Serializable{
    private String id;
    private String fullname;
    private String mobile;
    private String community_name;
    private String community_id;
    private String building_name;
    private String unit;
    private String floor;
    private String code;
    private String room_id;
    private String bind_type;
    private String bind_status;
    private NewWuyeBean bill_list;
    private String tot_sumvalue;
    private List<NewWuyeBean> list;
    private String bill_id;
    private String charge_type;
    private String sumvalue;
    private String startdate;
    private String enddate;
    private String time;
    private String bill_time;
    private WuyeInfo info;

    public WuyeInfo getInfo() {
        return info;
    }

    public void setInfo(WuyeInfo info) {
        this.info = info;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getBill_time() {
        return bill_time;
    }

    public void setBill_time(String bill_time) {
        this.bill_time = bill_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getBind_type() {
        return bind_type;
    }

    public void setBind_type(String bind_type) {
        this.bind_type = bind_type;
    }

    public String getBind_status() {
        return bind_status;
    }

    public void setBind_status(String bind_status) {
        this.bind_status = bind_status;
    }

    public NewWuyeBean getBill_list() {
        return bill_list;
    }

    public void setBill_list(NewWuyeBean bill_list) {
        this.bill_list = bill_list;
    }

    public String getTot_sumvalue() {
        return tot_sumvalue;
    }

    public void setTot_sumvalue(String tot_sumvalue) {
        this.tot_sumvalue = tot_sumvalue;
    }

    public List<NewWuyeBean> getList() {
        return list;
    }

    public void setList(List<NewWuyeBean> list) {
        this.list = list;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
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
}
