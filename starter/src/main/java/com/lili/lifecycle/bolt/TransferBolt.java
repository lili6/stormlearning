package com.lili.lifecycle.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 * Created by lili on 2015/3/2.
 */
public class TransferBolt extends BaseBasicBolt{
	private static final Logger log = LoggerFactory.getLogger(TransferBolt.class);

	public TransferBolt(){
		log.warn("&&&&&&&&&&&&&&&&& TransferBolt constructor method invoked");
	}
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		log.warn("################# TransferBolt prepare() method invoked");
	}
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		log.warn("################# TransferBolt execute() method invoked");
		String word = input.getStringByField("str");
		collector.emit(new Values(word));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		log.warn("################# TransferBolt declareOutputFields() method invoked");
		declarer.declare(new Fields("word"));
	}
}
