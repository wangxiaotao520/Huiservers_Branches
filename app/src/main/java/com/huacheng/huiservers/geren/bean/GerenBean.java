package com.huacheng.huiservers.geren.bean;

import java.util.List;

public class GerenBean {
	private String id;
	private String order_details;
	private String state;
	private String num;
	private String oid;
	private String community_cn;
	private List<AddressBean> com_list;
	private String prepay;
	private AddressBean address;
	private String c_name;

	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getCommunity_cn() {
		return community_cn;
	}
	public void setCommunity_cn(String community_cn) {
		this.community_cn = community_cn;
	}
	public List<AddressBean> getCom_list() {
		return com_list;
	}
	public void setCom_list(List<AddressBean> com_list) {
		this.com_list = com_list;
	}
	public String getOrder_details() {
		return order_details;
	}
	public void setOrder_details(String order_details) {
		this.order_details = order_details;
	}
	public String getPrepay() {
		return prepay;
	}
	public void setPrepay(String prepay) {
		this.prepay = prepay;
	}
	public AddressBean getAddress() {
		return address;
	}
	public void setAddress(AddressBean address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}


}
