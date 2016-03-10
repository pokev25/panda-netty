package com.panda.netty.common.message;

import java.util.Date;

public class Header {
	// 数据包长度,4 byte
	private int length;
	// 版本号,2 byte
	private short version;
	// 命令,4 byte
	private int command;
	// 消息id,32 byte
	private String messageId;
	// 创建时间,8 byte
	private Date createTime = new Date();

	public Header() {
	}

	public Header(short version, int command, String messageId) {
		this.version = version;
		this.command = command;
		this.messageId = messageId;
	}

	public Header(int length, short version, int command, String messageId, Date createTime) {
		this.length = length;
		this.version = version;
		this.command = command;
		this.messageId = messageId;
		this.createTime = createTime;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
