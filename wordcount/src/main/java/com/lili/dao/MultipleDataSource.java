package com.lili.dao;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by liguofang on 2015/1/26.
 * 多数据源切换
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
	private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

	public static void setDataSourceKey(String dataSource) {
		dataSourceKey.set(dataSource);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceKey.get();
	}
}