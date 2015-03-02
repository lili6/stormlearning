package com.lili.monitor.topology;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import com.lili.monitor.spout.MetaSpout;
import com.lili.monitor.spout.StringScheme;
import com.lili.monitor.utils.Constant;
import com.lili.monitor.utils.PropertyUtil;
import com.lili.monitor.utils.ReflectionUtil;

import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.consumer.ConsumerConfig;
import com.taobao.metamorphosis.utils.ZkUtils.ZKConfig;

/**
 * Created by lili on 2015/3/2.
 */

/**
 * Storm拓扑主函数
 */
public class TopoMain {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: topoName configName");
			System.err.println("such as : ./storm jar  /home/topo.jar cn.itcast.storm.topology.TopoMain topo-1 topology.xml");
			System.exit(2);
		}
		String topoName = args[0];
		String configName = args[1];
		//设置ZK配置信息
		final ZKConfig zkConfig = new ZKConfig();
		zkConfig.zkConnect = PropertyUtil.getProperty("zkConnect");
		zkConfig.zkSessionTimeoutMs = Integer.parseInt(PropertyUtil.getProperty("zkSessionTimeoutMs"));
		zkConfig.zkConnectionTimeoutMs = Integer.parseInt(PropertyUtil.getProperty("zkConnectionTimeoutMs"));
		zkConfig.zkSyncTimeMs = Integer.parseInt(PropertyUtil.getProperty("zkSyncTimeMs"));

		// 设置MetaQ
		final MetaClientConfig metaClientConfig = new MetaClientConfig();
		metaClientConfig.setZkConfig(zkConfig);

		// 创建TopologyBuilder和设置配置信息
		TopologyBuilder builder = new TopologyBuilder();
		Config conf = new Config();
		conf.setDebug(Boolean.parseBoolean(PropertyUtil.getProperty("debug")));
		conf.setNumWorkers(Integer.parseInt(PropertyUtil.getProperty("numWorkers")));
		buildTopology(builder, metaClientConfig, configName);
		StormSubmitter.submitTopology(topoName, conf, builder.createTopology());
		//LocalCluster local = new LocalCluster();
		//local.submitTopology(topoName, conf, builder.createTopology());

	}

	/**
	 * 根据配置文件构建TopologyBuilder
	 */
	private static void buildTopology(TopologyBuilder topologybuilder, MetaClientConfig metaClientConfig, String configName) throws Exception {

		File configFile = new File(Constant.CONFIG_PATH + configName);
		FileInputStream inputStream = new FileInputStream(configFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document doc = documentBuilder.parse(inputStream);
		NodeList spoutList = doc.getElementsByTagName("spout");
		for (int i = 0; i < spoutList.getLength(); i++) {
			Element spoutElement = (Element) spoutList.item(i);
			String spoutId = spoutElement.getAttribute("id");
			String topic = spoutElement.getAttribute("topic");
			String group = spoutElement.getAttribute("group");
			String spoutNumber = spoutElement.getAttribute("number");
			int spout_number = Integer.parseInt(spoutNumber);
			ConsumerConfig consumerConfig = new ConsumerConfig(group);
			MetaSpout spout = new MetaSpout(metaClientConfig, topic, consumerConfig, new StringScheme());
			topologybuilder.setSpout(spoutId, spout, spout_number);
			NodeList boltList = spoutElement.getElementsByTagName("bolt");
			for (int j = 0; j < boltList.getLength(); j++) {
				Element boltElement = (Element) boltList.item(j);
				String boltId = boltElement.getAttribute("id");
				String boltClass = boltElement.getAttribute("class");
				String tableName = boltElement.getAttribute("tableName");
				String filterConfig = boltElement.getAttribute("filterConfig");
				String method = boltElement.getAttribute("method");
				String arg = boltElement.getAttribute("source");
				String boltNumber = boltElement.getAttribute("number");
				int bolt_number = Integer.parseInt(boltNumber);
				BaseBasicBolt bolt = (BaseBasicBolt) ReflectionUtil.newInstance(boltClass,tableName, filterConfig);
				BoltDeclarer declarer = topologybuilder.setBolt(boltId, bolt, bolt_number);
				ReflectionUtil.invokeMethod(BoltDeclarer.class, declarer, method, arg);
			}
		}

	}

}
