package com.huacheng.huiservers.shop.bean;

import java.util.List;

public class ConfirmBean {

	private String merchant_id;
	private String merchant_name;
	private String merchant_telphone;
	private String merchant_address;
	private String half_amount;
	private String number;
	private List<BannerBean> img;
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public String getMerchant_telphone() {
		return merchant_telphone;
	}
	public void setMerchant_telphone(String merchant_telphone) {
		this.merchant_telphone = merchant_telphone;
	}
	public String getMerchant_address() {
		return merchant_address;
	}
	public void setMerchant_address(String merchant_address) {
		this.merchant_address = merchant_address;
	}
	public String getHalf_amount() {
		return half_amount;
	}
	public void setHalf_amount(String half_amount) {
		this.half_amount = half_amount;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<BannerBean> getImg() {
		return img;
	}
	public void setImg(List<BannerBean> img) {
		this.img = img;
	}


}
