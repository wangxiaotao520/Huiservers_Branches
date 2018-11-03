package com.huacheng.huiservers.geren.bean;

import java.io.Serializable;

public class GroupMemberBean implements Serializable{

    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String id;
    private String is_new;
    private String uid;
    private String city_id;
    private String region_id;
    private String is_ym;//是否为远程0为无，1为亿码，2为汾西

    //0.4.1物业选择小区
    private String company_id;
    private String company_name;
    private String department_id;
    private String department_name;

    public String getIs_ym() {
        return is_ym;
    }

    public void setIs_ym(String is_ym) {
        this.is_ym = is_ym;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

}
