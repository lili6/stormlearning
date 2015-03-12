package com.lili.storm.topo;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.lili.storm.bolt.ReportBolt;
import com.lili.storm.bolt.SplitSentenceBolt;
import com.lili.storm.bolt.WordCountBolt;
import storm.kafka.*;

/**
 * Created by lili on 2015/3/12.
 * 利用kafka发送过来的数据进行处理
 * 将Sentence-spout 换为KafkaSpout
 */
public class KafkaTopo {

	public static final String topic = "kafkatopic";

	private static final String SENTENCE_SPOUT_ID = "sentence-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String REPORT_BOLT_ID = "report-bolt";
	private static final String TOPOLOGY_NAME = "word-count-topology";

	//zookeeper的ip和端口
	private static BrokerHosts brokerHosts = new ZkHosts("10.6.155.160:2181");
	public static void main(String[] args)throws Exception {

		String zkRoot = "/brokers/topics";
		String spoutId = "myKafka";
		//worker和spout数
		int workNumber = 1;
		//bolt线程数
		int boltNumber =  1;

//      定义Spout
		SpoutConfig spoutConfig =  new SpoutConfig(brokerHosts,topic,zkRoot,spoutId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

//		定义Bolt-------
		SplitSentenceBolt splitBolt = new SplitSentenceBolt();
		WordCountBolt countBolt = new WordCountBolt();
		ReportBolt reportBolt = new ReportBolt();

		TopologyBuilder builder = new TopologyBuilder();


		builder.setSpout(spoutId, kafkaSpout);
//		builder.setBolt("kafkaBolt",new KafkaDemoBolt(),1).shuffleGrouping(spoutId);

		builder.setSpout(SENTENCE_SPOUT_ID, kafkaSpout);
		// SentenceSpout --> SplitSentenceBolt
		builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(SENTENCE_SPOUT_ID);
		// SplitSentenceBolt --> WordCountBolt
		builder.setBolt(COUNT_BOLT_ID, countBolt).fieldsGrouping(
				SPLIT_BOLT_ID, new Fields("word"));
		// WordCountBolt --> ReportBolt
		builder.setBolt(REPORT_BOLT_ID, reportBolt).globalGrouping(COUNT_BOLT_ID);

		Config config = new Config();
		config.setDebug(true);

		if (args != null && args.length > 0) {//提交到集群上运行
			config.setNumWorkers(workNumber);
			config.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], config, builder.createTopology());
		} else { //本地集群模式运行
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
			Thread.sleep(100000);
			System.out.println("睡眠3秒后杀掉 kafkaStormTopo。。。。");
			cluster.killTopology(TOPOLOGY_NAME);
			cluster.shutdown();
		}
	}


}
