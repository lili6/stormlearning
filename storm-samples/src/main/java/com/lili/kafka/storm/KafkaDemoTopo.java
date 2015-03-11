package com.lili.kafka.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.*;

/**
 * Created by lili on 2015/3/11.
 * 接受从kafka过来的数据的处理
 * 查看kafka发送的数据，可以通过命令行查询
 * ./kafka-console-consumer.sh  --zookeeper 10.6.155.160:2181 --topic kafkatopic --from-beginning
 */
public class KafkaDemoTopo {
	public static final String topic = "kafkatopic";
	//zookeeper的ip和端口
	private static BrokerHosts brokerHosts = new ZkHosts("10.6.155.160:2181");
	public static void main(String[] args)throws Exception {

		String zkRoot="/brokers/topics";
		String spoutId = "myKafka";
		//worker和spout数
		int workNumber = 1;
		//bolt线程数
		int boltNumber =  1;
		TopologyBuilder builder = new TopologyBuilder();
		SpoutConfig spoutConfig =  new SpoutConfig(brokerHosts,topic,zkRoot,spoutId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		builder.setSpout(spoutId, new KafkaSpout(spoutConfig), 1);
		builder.setBolt("kafkaBolt",new KafkaDemoBolt(),1).shuffleGrouping(spoutId);
		Config config = new Config();
		config.setDebug(false);
		config.setNumWorkers(workNumber);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("kafkaStormTopo", config, builder.createTopology());
		Thread.sleep(300000);
		System.out.println("睡眠3秒后杀掉 kafkaStormTopo。。。。");
		cluster.killTopology("kafkaStormTopo");
		cluster.shutdown();
	}
}
