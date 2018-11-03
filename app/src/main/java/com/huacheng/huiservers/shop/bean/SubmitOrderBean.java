package com.huacheng.huiservers.shop.bean;

import java.io.Serializable;

public class SubmitOrderBean implements Serializable{
	private String tagid;
	private String p_id;
	private String p_title;
	private String p_title_img;
	private String price;
	private String number;
	
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public String getP_title() {
		return p_title;
	}
	public void setP_title(String p_title) {
		this.p_title = p_title;
	}
	public String getP_title_img() {
		return p_title_img;
	}
	public void setP_title_img(String p_title_img) {
		this.p_title_img = p_title_img;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	

}
