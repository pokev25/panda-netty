package com.panda.netty.common.enums;

public enum CommandEnum {
	//1-1000用作系统默认命令，业务命令从1000开始
	LOGIN(0, "登录"), 
	QUIT(1, "退出"), 
	UNKNOW(-1, "未知");

	private int key;
	private String value;

	private CommandEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static CommandEnum keyOf(int key) {
		CommandEnum result = CommandEnum.UNKNOW;
		for (CommandEnum e : CommandEnum.values()) {
			if (e.getKey() == key) {
				result = e;
				break;
			}
		}
		return result;
	}
}
