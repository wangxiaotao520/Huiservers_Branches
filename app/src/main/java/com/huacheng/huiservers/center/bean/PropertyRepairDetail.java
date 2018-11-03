package com.huacheng.huiservers.center.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 * 维修详情Bean
 */

public class PropertyRepairDetail {

    private PropertyRepair propertyRepair;

    private List<Imgs> imgs;

    private List<Reply> r_list;

    public List<Reply> getR_list() {
        return r_list;
    }

    public void setR_list(List<Reply> r_list) {
        this.r_list = r_list;
    }

    public PropertyRepair getPropertyRepair() {
        return propertyRepair;
    }

    public void setPropertyRepair(PropertyRepair propertyRepair) {
        this.propertyRepair = propertyRepair;
    }

    public List<Imgs> getImgs() {
        return imgs;
    }

    public void setImgs(List<Imgs> imgs) {
        this.imgs = imgs;
    }

}

