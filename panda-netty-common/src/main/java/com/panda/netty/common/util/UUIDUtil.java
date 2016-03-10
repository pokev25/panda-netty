package com.panda.netty.common.util;

import java.util.UUID;

public class UUIDUtil {
	public static String create() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args) {
		System.out.println(UUIDUtil.create().length());
	}
}
