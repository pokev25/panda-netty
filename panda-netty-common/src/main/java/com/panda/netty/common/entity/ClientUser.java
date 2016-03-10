package com.panda.netty.common.entity;

import io.netty.util.AttributeKey;

import java.util.Date;

public class ClientUser {
	public static final AttributeKey<String> CLIENT_ID = AttributeKey.valueOf("clientId");
	public static final AttributeKey<ClientUser> CLIENT_USER = AttributeKey.valueOf("clientUser");
	private String id;
	private String name;
	private String token;
	private final Date loginTime = new Date();

	public ClientUser(String id, String name, String token) {
		this.id = id;
		this.name = name;
		this.token = token;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLoginTime() {
		return loginTime;
	}

}