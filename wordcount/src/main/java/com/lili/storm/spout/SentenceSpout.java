package com.lili.storm.spout;

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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lili on 2015/3/12.
 * 可靠的Spout，成功和失败后都会进行反馈
 */
public class SentenceSpout extends BaseRichSpout{
//	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SentenceSpout.class);

	private static Logger log = LoggerFactory.getLogger(SentenceSpout.class);
	/*记录发送的数据，对象来存储未处理完的元组*/
	private ConcurrentHashMap<UUID,Values> pending;
	private SpoutOutputCollector collector;

	private String[] sentences = {
			"my dog has fleas",
			"i like cold beverages",
			"the dog ate my homework",
			"don't have a cow man",
			"i don't think i like fleas"
	};
	private int index = 0;
	private Random random = new Random();
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}

	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		log.debug("SentenceSpout open===================");
		this.collector = collector;
		this.pending = new ConcurrentHashMap<UUID, Values>();
	}

	@Override
	public void nextTuple() {
		index = random.nextInt(sentences.length);
		Values values = new Values(sentences[index]);
		UUID msgId= UUID.randomUUID();
		this.pending.put(msgId,values);
//		log.debug("发射数据：msgId[{}]===value[{}]",msgId,values.get(0));
		this.collector.emit(values, msgId);
		Utils.sleep(3000);
		//可以暂停几秒...

	}

	public void ack(Object msgId) {
		log.debug("msgId【{}】发送成功！",msgId);
		this.pending.remove(msgId);
	}

	public void fail(Object msgId) {
		log.debug("msgId【{}】发送失败！重新发送...",msgId);
		this.collector.emit(this.pending.get(msgId), msgId);
	}
}
