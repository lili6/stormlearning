package com.lili.monitor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
/**
 * 属性配置读取工具
 * Created by lili on 2015/3/2.
 */
public class PropertyUtil {
	private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);
	private static Properties pros = new Properties();

	//加载属性文件
	static {
		try {
			File configFile = new File(Constant.CONFIG_PATH + "config.properties");
			FileInputStream inputStream = new FileInputStream(configFile);
			pros.load(inputStream);
		} catch (Exception e) {
			log.error("load configuration error", e);
		}
	}

	/**
	 * 读取配置文中的属性值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
		return pros.getProperty(key);
	}

}
