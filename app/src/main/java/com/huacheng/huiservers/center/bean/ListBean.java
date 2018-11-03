package com.huacheng.huiservers.center.bean;

import java.io.Serializable;
import java.util.List;

public class ListBean implements Serializable{
	private String id;
	private String oid;
	private String uid;
	private String status;
	private String topclass_cn;
	private String topclass;
	private String category_cn;
	private String category;
	private String tag_cn;
	private String tag;
	private String address;
	private String latitude;
	private String longitude;
	private String start_time;
	private String end_time;
	private String addtime;
	private String uptime;
	private String juli;
	private String total_Pages;
	private String description;
	private String contacts;
	private String mobile;
	private String count;
	private String fullname;
	private String photo; 
	private String sname;
	private String amount;
	//private String reply;
	private String prepay_amount;
	private String prepay_is_pay;
	private String service_personal_uid;
	private String prepay_pay_time;
	private String pay_time;
	private String pay_name;
	private String is_pay;
	private String total_amount;
	private String order_id	;
	private String parent_id;
	private String name;
	private String is_cancel;
	private String feedback;
	private String service_mobile;
	private String institution;
	private String service_uid;
	private String log_title;
	private String log_description;
	private String paytype;
	private String atype;
	private String paytime;
	private String refuse;
	private ImgBean feek;
	private String text;
	private String prepay_oid;
	private String prepay_pay_type;
	private String link;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	private boolean isSelect;
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPrepay_pay_type() {
		return prepay_pay_type;
	}
	public void setPrepay_pay_type(String prepay_pay_type) {
		this.prepay_pay_type = prepay_pay_type;
	}
	public String getPrepay_oid() {
		return prepay_oid;
	}
	public void setPrepay_oid(String prepay_oid) {
		this.prepay_oid = prepay_oid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ImgBean getFeek() {
		return feek;
	}
	public void setFeek(ImgBean feek) {
		this.feek = feek;
	}
	public String getRefuse() {
		return refuse;
	}
	public void setRefuse(String refuse) {
		this.refuse = refuse;
	}
	public String getLog_title() {
		return log_title;
	}
	public void setLog_title(String log_title) {
		this.log_title = log_title;
	}
	public String getLog_description() {
		return log_description;
	}
	public void setLog_description(String log_description) {
		this.log_description = log_description;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getAtype() {
		return atype;
	}
	public void setAtype(String atype) {
		this.atype = atype;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getService_uid() {
		return service_uid;
	}
	public void setService_uid(String service_uid) {
		this.service_uid = service_uid;
	}
	private List<ImgBean> imgs;

	public String getService_mobile() {
		return service_mobile;
	}
	public void setService_mobile(String service_mobile) {
		this.service_mobile = service_mobile;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getTopclass() {
		return topclass;
	}
	public void setTopclass(String topclass) {
		this.topclass = topclass;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getPrepay_amount() {
		return prepay_amount;
	}
	public void setPrepay_amount(String prepay_amount) {
		this.prepay_amount = prepay_amount;
	}
	public String getPrepay_is_pay() {
		return prepay_is_pay;
	}
	public void setPrepay_is_pay(String prepay_is_pay) {
		this.prepay_is_pay = prepay_is_pay;
	}
	public String getPrepay_pay_time() {
		return prepay_pay_time;
	}
	public void setPrepay_pay_time(String prepay_pay_time) {
		this.prepay_pay_time = prepay_pay_time;
	}
	public String getPay_name() {
		return pay_name;
	}
	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}
	public String getIs_pay() {
		return is_pay;
	}
	public void setIs_pay(String is_pay) {
		this.is_pay = is_pay;
	}

	public List<ImgBean> getImgs() {
		return imgs;
	}
	public void setImgs(List<ImgBean> imgs) {
		this.imgs = imgs;
	}
	public String getIs_cancel() {
		return is_cancel;
	}
	public void setIs_cancel(String is_cancel) {
		this.is_cancel = is_cancel;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getService_personal_uid() {
		return service_personal_uid;
	}
	public void setService_personal_uid(String service_personal_uid) {
		this.service_personal_uid = service_personal_uid;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTopclass_cn() {
		return topclass_cn;
	}
	public void setTopclass_cn(String topclass_cn) {
		this.topclass_cn = topclass_cn;
	}
	public String getCategory_cn() {
		return category_cn;
	}
	public void setCategory_cn(String category_cn) {
		this.category_cn = category_cn;
	}
	public String getTag_cn() {
		return tag_cn;
	}
	public void setTag_cn(String tag_cn) {
		this.tag_cn = tag_cn;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getJuli() {
		return juli;
	}
	public void setJuli(String juli) {
		this.juli = juli;
	}
	public String getTotal_Pages() {
		return total_Pages;
	}
	public void setTotal_Pages(String total_Pages) {
		this.total_Pages = total_Pages;
	}
	@Override
	public boolean equals(Object obj)
	{
		ListBean other = (ListBean) obj;
		return other.id.equals(this.id);
	}

}
