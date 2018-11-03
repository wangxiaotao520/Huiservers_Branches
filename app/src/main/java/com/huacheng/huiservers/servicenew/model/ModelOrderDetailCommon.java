package com.huacheng.huiservers.servicenew.model;

/**
 * Description:投诉，取消订单
 * created by wangxiaotao
 * 2018/9/5 0005 下午 4:48
 */
public class ModelOrderDetailCommon {
    private boolean isSelected=false;

    private String id;
    private String c_name;
    private String c_text;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_text() {
        return c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }
}
