package com.huacheng.huiservers.shop.bean;

import java.util.List;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class ShopMainBean {

	private List<BannerBean> list;
	private String category_img;
	private String category_name;
	private String category_id;
	private String category_banner_img;
	private String bg_color;
	private String score;
	private String username;
	private String description;
	private String text_color;
	private String total_Pages;
	

	public String getText_color() {
		return text_color;
	}
	public void setText_color(String text_color) {
		this.text_color = text_color;
	}
	public String getBg_color() {
		return bg_color;
	}
	public void setBg_color(String bg_color) {
		this.bg_color = bg_color;
	}
	public String getCategory_banner_img() {
		return category_banner_img;
	}
	public void setCategory_banner_img(String category_banner_img) {
		this.category_banner_img = category_banner_img;
	}
	public String getCategory_img() {
		return category_img;
	}
	public void setCategory_img(String category_img) {
		this.category_img = category_img;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getTotal_Pages() {
		return total_Pages;
	}
	public void setTotal_Pages(String total_Pages) {
		this.total_Pages = total_Pages;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BannerBean> getList() {
		return list;
	}
	public void setList(List<BannerBean> list) {
		this.list = list;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

}
