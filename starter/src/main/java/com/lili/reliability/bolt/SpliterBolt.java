package com.lili.reliability.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;

/**
 * Created by lili on 2015/3/2.
 */
public class SpliterBolt implements IRichBolt {
	private OutputCollector collector;
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
   	    this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		String[] words = line.split(",");
		for (String word : words) {
			collector.emit(input, new Values(word));
		}
		collector.ack(input);
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
