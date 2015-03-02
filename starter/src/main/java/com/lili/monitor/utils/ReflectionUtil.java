package com.lili.monitor.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import backtype.storm.topology.BoltDeclarer;
/**
 * Created by lili on 2015/3/2.
 */

/**
 * 反射工具
 */
public class ReflectionUtil {

	/**
	 * 根据构造方法的参数反射实例化对象
	 * @param className
	 * @param tableName
	 * @param filterConfig
	 * @return
	 * @throws Exception
	 */
	public static Object newInstance(String className, String tableName, String filterConfig) throws Exception {
		Class<?> clazz = Class.forName(className);
		Constructor<?> constructor = clazz.getConstructor(String.class, String.class);
		return constructor.newInstance(tableName, filterConfig);
	}

	/**
	 * 通过反射调用对象的方法
	 * @param clazz
	 * @param declarer
	 * @param methodName
	 * @param arg
	 * @throws Exception
	 */
	public static void invokeMethod(Class<?> clazz, BoltDeclarer declarer, String methodName, String arg) throws Exception {
		Method method = clazz.getMethod(methodName, String.class);
		method.invoke(declarer, arg);
	}
}