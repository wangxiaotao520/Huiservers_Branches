package com.huacheng.huiservers.servicenew.model;

/**
 * 类描述：
 * 时间：2018/9/5 20:27
 * created by DFF
 */
public class ModelOrderList {

    /**
     * dfw : 1
     * dpj : 1
     * wc : 1
     */
    private String dfw;
    private String dpj;
    private String wc;
    /**
     * id : 42
     * uid : 105
     * address : 汇通路太铁一处
     * s_id : 30
     * s_name : 太极擦玻璃
     * status : 1
     * score : 2
     * evaluate : 阿达嘎嘎嘎灌灌灌灌灌嘎嘎嘎嘎嘎嘎嘎嘎嘎灌灌灌灌灌个
     * evaluatime : 1536131452
     * description : 服务订单描述
     * title_img : public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png
     * total_Pages : 1
     */

    private String id;
    private String uid;
    private String address;
    private String s_id;
    private String s_name;
    private String status;
    private String score;
    private String evaluate;
    private String evaluatime;
    private String description;
    private String title_img;
    private int total_Pages;

    private int event_type;//0是取消订单//1.是评价完成


    public String getDfw() {
        return dfw;
    }

    public void setDfw(String dfw) {
        this.dfw = dfw;
    }

    public String getDpj() {
        return dpj;
    }

    public void setDpj(String dpj) {
        this.dpj = dpj;
    }

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluatime() {
        return evaluatime;
    }

    public void setEvaluatime(String evaluatime) {
        this.evaluatime = evaluatime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

}
