package com.lili.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by lili on 2015/3/12.
 */
public class SplitSentenceBolt extends BaseRichBolt {
	private static Logger log = LoggerFactory.getLogger(SplitSentenceBolt.class);
	private OutputCollector collector;
	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
	    log.debug("准备拆分...open-SplitSentenceBolt");
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
//		String sentence = tuple.getStringByField("sentence");
		String sentence = tuple.getString(0);
		String[] words = sentence.split(" ");
		for(String word : words){
			this.collector.emit(new Values(word));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}
