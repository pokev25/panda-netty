package com.panda.netty.common.message.req;

import java.io.Serializable;

public class LoginMessage implements Serializable {
	private static final long serialVersionUID = 3443904597922808701L;
	private String userId;
	private String userName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
