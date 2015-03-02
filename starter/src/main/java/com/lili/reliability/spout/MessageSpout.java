package com.lili.reliability.spout;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送数据 lines数组中的数据
 * 发送的数据为，值+索引
 * Created by lili on 2015/3/2.
 */
public class MessageSpout implements IRichSpout {
	private static final Logger log = LoggerFactory.getLogger(MessageSpout.class);
	private int index = 0;

	private String[] lines;

	private SpoutOutputCollector collector;

	public MessageSpout(){
		lines = new String[]{
				"0,zero",
				"1,one",
				"2,two",
				"3,three",
				"4,four",
				"5,five",
				"6,six",
				"7,seven",
				"8,eight",
				"9,nine"
		};
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void close() {

	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public void nextTuple() {
		if(index < lines.length) {
			String l = lines[index];
			collector.emit(new Values(l),index);
			index++;
		}
	}

	@Override
	public void ack(Object msgId) {
	 	log.debug("message sends successfully (msgId = " + msgId +")");
	}

	@Override
	public void fail(Object msgId) {
		log.debug("error : message sends unsuccessfully (msgId = " + msgId + ")");
		log.debug("resending...");
		collector.emit(new Values(lines[(Integer) msgId]), msgId);
		log.debug("resend successfully");
	}
}
