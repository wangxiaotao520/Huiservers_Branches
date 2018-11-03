package com.huacheng.huiservers.shop.bean;

import com.huacheng.huiservers.fragment.bean.ShopIndexBean;

import java.util.List;

/**
 * Description: 限时抢购 列表
 * created by wangxiaotao
 * 2018/11/1 000 下午 5:03
 */
public class ModelSeckillBean {
    List <ShopIndexBean>   list;
    List <CateBean>     class_name;

    public List<ShopIndexBean> getList() {
        return list;
    }

    public void setList(List<ShopIndexBean> list) {
        this.list = list;
    }

    public List<CateBean> getClass_name() {
        return class_name;
    }

    public void setClass_name(List<CateBean> class_name) {
        this.class_name = class_name;
    }



}
