package com.huacheng.huiservers.login.bean;

public class LoginBean {
	private String uid;
	private String utype;
	private String username;
	private String avatars;
	private String token;
	private String is_bind_property;
	private String pwd;
	private String tokenSecret;
	private String community_id;

	public String getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIs_bind_property() {
		return is_bind_property;
	}
	public void setIs_bind_property(String is_bind_property) {
		this.is_bind_property = is_bind_property;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUtype() {
		return utype;
	}
	public void setUtype(String utype) {
		this.utype = utype;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatars() {
		return avatars;
	}
	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}


	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

}
