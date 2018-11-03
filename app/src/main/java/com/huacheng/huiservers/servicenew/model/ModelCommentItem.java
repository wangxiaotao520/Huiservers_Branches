package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;

/**
 * Description: 评论item
 * created by wangxiaotao
 * 2018/9/4 0004 下午 3:43
 */
public class ModelCommentItem implements Serializable {

    private String id;
    private String uid;
    private String i_id;
    private String score;
    private String evaluate;
    private long evaluatime;
    private String anonymous;
    private String nickname;
    private String avatars;
    private int total_Pages;

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

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
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

    public long getEvaluatime() {
        return evaluatime;
    }

    public void setEvaluatime(long evaluatime) {
        this.evaluatime = evaluatime;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }
    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }
}
