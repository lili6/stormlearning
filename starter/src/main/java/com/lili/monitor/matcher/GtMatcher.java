package com.lili.monitor.matcher;

/**
 * 关系运算比较器
 * Created by lili on 2015/3/2.
 */
public class GtMatcher implements IMatcher {

	private double origValue;

	public GtMatcher(String origValue) {
		this.origValue = Double.parseDouble(origValue);
	}

	public boolean match(String destValue) {
		return Double.parseDouble(destValue) > origValue;
	}
}