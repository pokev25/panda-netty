package com.panda.netty.server.config;

import java.util.HashMap;
import java.util.Map;

import com.panda.netty.common.util.PropertiesUtil;

public class ServerConfig {
	public static int PORT;
	public static int HEARTBEAT_TIME;
	public static int SO_BACKLOG;
	public static boolean SO_KEEPALIVE;
	public static int CONNECT_TIMEOUT_MILLIS;
	public static int SO_TIMEOUT;
	public static Map<String, String> configs = new HashMap<String, String>();

	static {
		configs = new PropertiesUtil("server.properties").getAllProperty();
		PORT = Integer.valueOf(configs.get("server.port"));
		HEARTBEAT_TIME = Integer.valueOf(configs.get("server.heartbeat_time"));
		SO_BACKLOG = Integer.valueOf(configs.get("server.so_backlog"));
		SO_KEEPALIVE = Boolean.valueOf(configs.get("server.so_keepalive"));
		CONNECT_TIMEOUT_MILLIS = Integer.valueOf(configs.get("server.connect_timeout_millis"));
		SO_TIMEOUT = Integer.valueOf(configs.get("server.so_timeout"));
	}
}
