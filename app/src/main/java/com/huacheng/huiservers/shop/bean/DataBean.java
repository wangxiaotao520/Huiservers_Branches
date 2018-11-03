package com.huacheng.huiservers.shop.bean;

import java.io.Serializable;

public class DataBean implements Serializable
{
	private String uid;
	private String tagid;
	private String type;
	private String min_price;
	private String p_id;
	private String p_title_img;
	private String title_img;
	private String title_thumb_img;
	private String p_title;
	private int number;
	private String addtime;
	private String inventory;
	private String tagname;
	private String exist_hours;
	private String original;
	private String title;

	int id;

	public String getTitle_thumb_img() {
		return title_thumb_img;
	}

	public void setTitle_thumb_img(String title_thumb_img) {
		this.title_thumb_img = title_thumb_img;
	}

	public String getTitle_img() {
		return title_img;
	}

	public void setTitle_img(String title_img) {
		this.title_img = title_img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
//public int carNum;

	String shopName;

	float price;


	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getExist_hours() {
		return exist_hours;
	}

	public void setExist_hours(String exist_hours) {
		this.exist_hours = exist_hours;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getMin_price() {
		return min_price;
	}

	public void setMin_price(String min_price) {
		this.min_price = min_price;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public String getP_title_img() {
		return p_title_img;
	}

	public void setP_title_img(String p_title_img) {
		this.p_title_img = p_title_img;
	}

	public String getP_title() {
		return p_title;
	}

	public void setP_title(String p_title) {
		this.p_title = p_title;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/*
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	 */
	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}


	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	/*public int getCarNum()
	{
		return carNum;
	}

	public void setCarNum(int carNum)
	{
		this.carNum = carNum;
	}*/

	public String getShopName()
	{
		return shopName;
	}

	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	public float getPrice()
	{
		return price;
	}

	public void setPrice(float price)
	{
		this.price = price;
	}

}
