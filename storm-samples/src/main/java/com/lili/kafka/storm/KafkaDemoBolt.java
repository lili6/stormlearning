package com.lili.kafka.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * Created by lili on 2015/3/11.
 */
public class KafkaDemoBolt extends BaseBasicBolt {
	@Override
	public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
		System.out.println("接受到的数据为：--" + tuple.getString(0));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

	}
}
