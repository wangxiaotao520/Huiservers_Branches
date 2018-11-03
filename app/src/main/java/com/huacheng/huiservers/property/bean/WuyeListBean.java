package com.huacheng.huiservers.property.bean;

/**
 * Description:
 * Author: badge
 * Create: 2018/10/16 17:53
 */
public class WuyeListBean {

    private String startdate;
    private String enddate;
    private String sumvalue;
    private String charge_type_id;
    private String charge_type;
    private String bill_id;
    private String bill_time;
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getBill_time() {
        return bill_time;
    }

    public void setBill_time(String bill_time) {
        this.bill_time = bill_time;
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

    public String getSumvalue() {
        return sumvalue;
    }

    public void setSumvalue(String sumvalue) {
        this.sumvalue = sumvalue;
    }

    public String getCharge_type_id() {
        return charge_type_id;
    }

    public void setCharge_type_id(String charge_type_id) {
        this.charge_type_id = charge_type_id;
    }

    public String getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(String charge_type) {
        this.charge_type = charge_type;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }
}
