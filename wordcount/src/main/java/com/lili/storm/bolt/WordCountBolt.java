package com.lili.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lili on 2015/3/12.
 */
public class WordCountBolt  extends BaseRichBolt {

//	private static Logger log = LoggerFactory.getLogger(WordCountBolt.class);
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WordCountBolt.class);

	private OutputCollector collector;
	private HashMap<String, Long> counts = null;
	@Override
	public void prepare(Map config, TopologyContext
			context, OutputCollector collector) {
		this.collector = collector;
		this.counts = new HashMap<String, Long>();
	}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = this.counts.get(word);
		if(count == null){
			count = 0L;
		}
		count++;
		this.counts.put(word, count);
		this.collector.emit(new Values(word, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}
}
