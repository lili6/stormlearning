package com.lili.monitor.matcher;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.lili.monitor.utils.Constant;
import com.lili.monitor.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * 正则匹配工具
 */
public class RecordMatcher {

	private static final Logger log = LoggerFactory.getLogger(RecordMatcher.class);
	private ReadWriteLock configLock = new ReentrantReadWriteLock();
	private List<Map<String, IMatcher>> configList;
	private String configFile;

	public RecordMatcher(String configFile) {
		this.configFile = configFile;
	}

	public RecordMatcher() {

	}

	public void setConfigList(List<Map<String, IMatcher>> configList) {
		this.configList = configList;
	}

	/**
	 * 加载配置
	 */
	private void loadConfig() {
		try {
			configLock.writeLock().lock();
			configList = new ArrayList<Map<String, IMatcher>>();
			FileInputStream inputStream = new FileInputStream(Constant.CONFIG_PATH + configFile);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document doc = documentBuilder.parse(inputStream);

			Map<String, String> matcherMap = new HashMap<String, String>();
			NodeList matcherList = doc.getElementsByTagName("matcher");
			for (int i = 0; i < matcherList.getLength(); i++) {
				Element matcherElement = (Element) matcherList.item(i);
				String operator = matcherElement.getAttribute("operator");
				String className = matcherElement.getAttribute("class");
				matcherMap.put(operator, className);
			}

			NodeList termList = doc.getElementsByTagName("term");
			for (int i = 0; i < termList.getLength(); i++) {
				Element termElement = (Element) termList.item(i);
				NodeList conditionList = termElement.getElementsByTagName("condition");
				LinkedHashMap<String, IMatcher> ruleMap = new LinkedHashMap<String, IMatcher>();
				for (int j = 0; j < conditionList.getLength(); j++) {
					Element conditionElement = (Element) conditionList.item(j);
					String field = conditionElement.getAttribute("field");
					String operator = conditionElement.getAttribute("operator");
					String value = conditionElement.getTextContent();
					String className = matcherMap.get(operator);
					Class<?> clazz = Class.forName(className);
					Constructor<?> constructor = clazz.getConstructor(String.class);
					IMatcher matcher = (IMatcher) constructor.newInstance(value);
					ruleMap.put(field.toLowerCase(), matcher);
				}
				configList.add(ruleMap);
			}
			inputStream.close();
		} catch (Exception e) {
			log.error("load configuration error", e);
		} finally {
			configLock.writeLock().unlock();
		}
	}

	/**
	 * 判断数据是否匹配规则
	 *
	 * @param fieldsList
	 * @param fields
	 * @return
	 */
	public boolean match(List<String> fieldsList, String[] fields) {
		try {
			configLock.readLock().lock();
			int i = 0;
			for (Map<String, IMatcher> ruleMap : configList) {
				int j = 0;
				for (Entry<String, IMatcher> entry : ruleMap.entrySet()) {
					String key = entry.getKey();
					IMatcher m = entry.getValue();
					int index = fieldsList.indexOf(key);
					String destValue = fields[index];
					if (!m.match(destValue)) {
						if ((i + 1) < configList.size()) {
							break;
						} else {
							return false;
						}
					} else {
						if ((j + 1) == ruleMap.size()) {
							return true;
						}
					}
					j++;
				}
				i++;
			}
		} catch (Exception e) {
			log.error("match configuration error", e);
		} finally {
			configLock.readLock().unlock();
		}
		return false;
	}

	/**
	 * 启动规则匹配工具并定时更新配置
	 */
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						loadConfig();
						Long refreshTime = Long.parseLong(PropertyUtil.getProperty("configRefreshTime")) * 1000;
						Thread.sleep(refreshTime);
					} catch (Exception e) {
						log.error("load configuration error", e);
					}
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		RecordMatcher m = new RecordMatcher("filterConfig.xml");
		m.loadConfig();
		m.match(Arrays.asList("a", "user_id", "b"), new String[]{"123", "11111", "1882188.100.100"});
	}
}