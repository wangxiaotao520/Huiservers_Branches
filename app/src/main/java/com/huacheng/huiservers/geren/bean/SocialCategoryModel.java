package com.huacheng.huiservers.geren.bean;

import java.util.List;

public class SocialCategoryModel {
	
	/**
     * status : 1
     * msg : 获取数据成功！
     * data : [{"id":"82","c_name":"二手交易"},{"id":"83","c_name":"房屋出售"},{"id":"84","c_name":"婚恋交友"},{"id":"85","c_name":"小区杂谈"}]
     * dialog : 
     */

    private int status;
    private String msg;
    private String dialog;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 82
         * c_name : 二手交易
         */

        private String id;
        private String c_name;

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
    }

}
