package com.huacheng.huiservers.center.bean;

import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;

public class ShopOrderBeanType {

	private String id;
	private String uid;
	private String order_number;
	private String price_total;
	private String pro_num;
	private String status;
	private String addtime;
	private String amount;
	private List<BannerBean> img;//详情商品轮播图片路径
	private List<BannerBean> order_info;

	public List<BannerBean> getImg() {
		return img;
	}

	public void setImg(List<BannerBean> img) {
		this.img = img;
	}

	private String total_Pages;

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getPrice_total() {
		return price_total;
	}

	public void setPrice_total(String price_total) {
		this.price_total = price_total;
	}

	public String getPro_num() {
		return pro_num;
	}

	public void setPro_num(String pro_num) {
		this.pro_num = pro_num;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<BannerBean> getOrder_info() {
		return order_info;
	}
	public void setOrder_info(List<BannerBean> order_info) {
		this.order_info = order_info;
	}
	public String getTotal_Pages() {
		return total_Pages;
	}
	public void setTotal_Pages(String total_Pages) {
		this.total_Pages = total_Pages;
	}

}
