package com.huacheng.huiservers.center.bean;

import java.util.List;

/**
 * Description: 地区bean
 * created by wangxiaotao
 * 2018/8/10 0010 上午 8:25
 */
public class ModelRegion {

    private int region_id;
    private String region_code;
    private String region_name;
    private List<ModelRegion> s_list;
    private List<ModelRegion> ss_list;

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public List<ModelRegion> getS_list() {
        return s_list;
    }

    public void setS_list(List<ModelRegion> s_list) {
        this.s_list = s_list;
    }

    public List<ModelRegion> getSs_list() {
        return ss_list;
    }

    public void setSs_list(List<ModelRegion> ss_list) {
        this.ss_list = ss_list;
    }



}
