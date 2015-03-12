package com.lili.storm.topo;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.lili.storm.bolt.ReportBolt;
import com.lili.storm.bolt.SplitSentenceBolt;
import com.lili.storm.bolt.WordCountBolt;
import com.lili.storm.spout.SentenceSpout;

/**
 * Created by lili on 2015/3/12.
 */
public class WordCountTopo {

//	private static Logger log = LoggerFactory.getLogger(WordCountTopology.class);
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WordCountTopo.class);

	private static final String SENTENCE_SPOUT_ID = "sentence-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String REPORT_BOLT_ID = "report-bolt";
	private static final String TOPOLOGY_NAME = "word-count-topology";
	public static void main(String[] args) throws
			Exception {

		SentenceSpout spout = new SentenceSpout();
		SplitSentenceBolt splitBolt = new
				SplitSentenceBolt();
		WordCountBolt countBolt = new WordCountBolt();
		ReportBolt reportBolt = new ReportBolt();

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(SENTENCE_SPOUT_ID, spout);
		// SentenceSpout --> SplitSentenceBolt
		builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(SENTENCE_SPOUT_ID);
		// SplitSentenceBolt --> WordCountBolt
		builder.setBolt(COUNT_BOLT_ID, countBolt).fieldsGrouping(
				SPLIT_BOLT_ID, new Fields("word"));
		// WordCountBolt --> ReportBolt
		builder.setBolt(REPORT_BOLT_ID, reportBolt).globalGrouping(COUNT_BOLT_ID);
		Config config = new Config();
		LocalCluster cluster = new LocalCluster();
		log.debug("本地提交任务开始....");
		cluster.submitTopology(TOPOLOGY_NAME, config,
				builder.createTopology());
		Utils.sleep(10000);
		log.debug("本地任务执行结束....");
		cluster.killTopology(TOPOLOGY_NAME);
		cluster.shutdown();
	}
}
