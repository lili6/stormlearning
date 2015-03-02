package com.lili.monitor.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则匹配器
 * @author Administrator
 *
 */
public class RegularMatcher implements IMatcher {

	private Pattern pattern;

	public RegularMatcher(String origValue) {
		pattern = Pattern.compile(origValue);
	}

	public boolean match(String destValue) {
		Matcher matcher = pattern.matcher(destValue);
		return matcher.matches();
	}

}
