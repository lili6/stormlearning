package com.lili.lifecycle.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by lili on 2015/3/2.
 */
public class WriterBolt extends BaseBasicBolt{
	private static final Logger log = LoggerFactory.getLogger(WriterBolt.class);
	private FileWriter writer = null;
	public WriterBolt() {
		log.warn("&&&&&&&&&&&&&&&&& WriterBolt constructor method invoked");
	}
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		log.warn("################# WriterBolt prepare() method invoked");
		try {
			writer = new FileWriter("/home/" + this);
		} catch (IOException e) {
			log.error("prepare exception:",e);
			throw new RuntimeException(e);
		}
	}
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		log.warn("################# WriterBolt execute() method invoked");
		String s = input.getString(0);
		try {
			writer.write(s);
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			log.error("execute exception:",e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		log.warn("################# WriterBolt declareOutputFields() method invoked");
	}
}
