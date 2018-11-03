package com.huacheng.huiservers.shop.bean;

import java.util.List;

public class CateBean {
	private String id;
	private String is_more;
	private String parent_id;
	private String name;
	private String photo;
	private String display;
	private String logo_img;
	private String banner_img;
	private String bg_color;
	private String order_num;
	private String addtime;
	private String uptime;
	private String total_Pages;
	private String cate_name;
	private String icon;
	private String is_limit;
	private List<CateBean> sub_arr;
	private List<CateBean> area_list;
	private boolean isSelect;

	public String getIs_limit() {
		return is_limit;
	}

	public void setIs_limit(String is_limit) {
		this.is_limit = is_limit;
	}

	public String getIs_more() {
		return is_more;
	}

	public void setIs_more(String is_more) {
		this.is_more = is_more;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public List<CateBean> getArea_list() {
		return area_list;
	}

	public void setArea_list(List<CateBean> area_list) {
		this.area_list = area_list;
	}

	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<CateBean> getSub_arr() {
		return sub_arr;
	}
	public void setSub_arr(List<CateBean> sub_arr) {
		this.sub_arr = sub_arr;
	}
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getLogo_img() {
		return logo_img;
	}
	public void setLogo_img(String logo_img) {
		this.logo_img = logo_img;
	}
	public String getBanner_img() {
		return banner_img;
	}
	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}
	public String getBg_color() {
		return bg_color;
	}
	public void setBg_color(String bg_color) {
		this.bg_color = bg_color;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getTotal_Pages() {
		return total_Pages;
	}
	public void setTotal_Pages(String total_Pages) {
		this.total_Pages = total_Pages;
	}
	

}
