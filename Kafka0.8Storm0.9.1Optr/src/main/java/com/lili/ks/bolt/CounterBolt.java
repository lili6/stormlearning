package com.lili.ks.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterBolt extends BaseBasicBolt {
	private static final Logger log = LoggerFactory.getLogger(CounterBolt.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5508421065181891596L;
	
	private static long counter = 0;
	
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		log.debug("CounterBolt msg=[{}]----------------counter=[{}]",tuple.getString(0),counter++);
		System.out.println("msg = "+tuple.getString(0)+" -------------counter = "+(counter++));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
