package com.huacheng.huiservers.wuye.bean;

import java.io.Serializable;

/**
 * 物业水、电 信息Bean
 * Created by Administrator on 2018/3/9.
 */

public class WuyeInfo implements Serializable {
    private String Xq;//小区id
    private String Cust_id;//用户编号/房间号
    private String Cust_name;//用户名称/业主名字

    private WuyeInfo d_list;
    private String Total;//最新表底/指数
    private String C_time;//抄表时间
    private String Daccount;//电表总买金额
    private String DMay_acc;//电表剩余金额/结余

    private WuyeInfo s_list;
    private String Saccount;//水表总买金额
    private String SMay_acc;//水表剩余金额/结余

    private String cust_addr;//用户地址
    private String Tel1;//电话1
    private String Account;//总买金额
    private String May_acc;//剩余金额

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

    public WuyeInfo getS_list() {
        return s_list;
    }

    public void setS_list(WuyeInfo s_list) {
        this.s_list = s_list;
    }

    public WuyeInfo getD_list() {
        return d_list;
    }

    public void setD_list(WuyeInfo d_list) {
        this.d_list = d_list;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getXq() {
        return Xq;
    }

    public void setXq(String xq) {
        Xq = xq;
    }

    public String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    public String getCust_name() {
        return Cust_name;
    }

    public void setCust_name(String cust_name) {
        Cust_name = cust_name;
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


    public String getCust_addr() {
        return cust_addr;
    }


    public void setCust_addr(String cust_addr) {
        this.cust_addr = cust_addr;
    }

    public String getTel1() {
        return Tel1;
    }

    public void setTel1(String tel1) {
        Tel1 = tel1;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getMay_acc() {
        return May_acc;
    }

    public void setMay_acc(String may_acc) {
        May_acc = may_acc;
    }
}
