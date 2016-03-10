package com.panda.netty.common.entity;

import java.util.HashMap;
import java.util.Map;

import com.panda.netty.common.util.PropertiesUtil;

public class ConfigManager {
	public static Map<String, String> manager = new HashMap<String, String>();
	static {
		manager = new PropertiesUtil("config.properties").getAllProperty();
	}
}
