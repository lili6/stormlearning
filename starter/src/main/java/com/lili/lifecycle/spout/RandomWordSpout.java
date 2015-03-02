package com.lili.lifecycle.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Random;

/**
 * 随机从String数组当中读取一个单词发送给下一个Bolt
 * Created by lili on 2015/3/2.
 */
public class RandomWordSpout extends BaseRichSpout{
	private static final Logger log = LoggerFactory.getLogger(RandomWordSpout.class);
	private SpoutOutputCollector collector;
	private String[] words = new String[]{"storm","hadoop","hive","flume"};
	private Random random = new Random();
	public RandomWordSpout() {
		log.warn("++++++++++++++++++++++RandomWordSpout 构造函数被调用..");
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		log.warn("++++++++++++Spout declareOutputFileds invoke....");
		declarer.declare(new Fields("str"));
	}
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		log.warn("################# RandomWordSpout open() method invoked");
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		log.warn("===============nextTuple is executing....");
		Utils.sleep(500);
		String str = words[random.nextInt(words.length)];
		collector.emit(new Values(str));
	}
	@Override
	public void activate(){
		log.warn("################# RandomWordSpout activate method invoke....");
	}
	@Override
	public void deactivate(){
		log.warn("################# RandomWordSpout deactivate method invoke....");
	}
}
