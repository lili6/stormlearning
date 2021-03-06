package com.lili.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.*;

/**
 * Created by lili on 2015/3/12.
 * 将收到的数据持久化到数据库中
 */
public class ReportMysqlBolt  extends BaseRichBolt {
	//	private static Logger log = Logger.getLogger(ReportBolt.class);
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReportBolt.class);
	private HashMap<String, Long> counts = null;

	public void prepare(Map config, TopologyContext context, OutputCollector collector) {
		this.counts = new HashMap<String, Long>();
	}

	/**
	 * 将收到的数据写入mysql数据库中....
	 * @param tuple
	 */
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = tuple.getLongByField("count");
		log.debug("ReportBolt---------------word[" + word + "] count=[" + count + "]");
		this.counts.put(word, count);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// this bolt does not emit anything
	}

	/**
	 * 当topology被kill掉的时候执行cleanup....
	 *
	 */
	public void cleanup() {
		log.debug("---------------------最后的计算结果。。。。。。------------------------------");
		System.out.println("--- 最后的计算结果 ---");
		List<String> keys = new ArrayList<String>();
		keys.addAll(this.counts.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			System.out.println(key + " : " + this.counts.get(key));
			log.debug("-------" + key + " : " + this.counts.get(key));
		}
		System.out.println("--------------");
	}
}