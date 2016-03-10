package com.panda.netty.common.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private Properties props;

	public PropertiesUtil(String fileName) {
		_readProperties(fileName);
	}

	/**
	 * 路径基于classpath
	 * 
	 * @author chenlj
	 * @Date 2016 下午6:33:43
	 */
	private void _readProperties(String fileName) {
		try {
			props = new Properties();
			InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			props.load(is);
			is.close();
		} catch (Exception e) {
			logger.error("加载文件" + fileName + "失败", e);
		}
	}

	/**
	 * 根据key获取值
	 * 
	 * @author chenlj
	 * @Date 2016 下午6:11:29
	 */
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	/**
	 * 获取所有属性
	 * 
	 * @author chenlj
	 * @Date 2016 下午6:05:28
	 */
	public Map<String, String> getAllProperty() {
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> it = props.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			map.put(key, props.getProperty(key));
		}
		return map;
	}

}