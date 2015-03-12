package com.lili.storm.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现计数器的功能，第一次将collector中的元素存放在成员变量counters(Map)中.
 * 如果counters（Map）中已经存在改元素，getValue并对Value进行累加操作
 * Created by lili on 2015/3/9.
 */
public class WordCounter extends BaseBasicBolt {
	Integer id;
	String name;
	//定义Map封装最后的结果
	Map<String,Integer> counters;
	/**
	 * 在spout结束时被调用，将最后的结果显示出来
	 *
	 * 結果:
	 * -- Word Counter [word-counter-2] --
	 * really: 1
	 * but: 1
	 * application: 1
	 * is: 2
	 * great: 2
	 */
	@Override
	public void cleanup() {
		System.out.println("-- Word Counter ["+name+"-"+id+"] --");
		for(Map.Entry<String, Integer> entry : counters.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
		System.out.println("实现计数器的功能 --完毕！");
	}
	/**
	 * 初始化操作
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.counters = new HashMap<String, Integer>();
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}

	/**
	 * 实现计数器的功能，第一次将collector中的元素存放在成员变量counters（Map）中.
	 * 如果counters（Map）中已经存在该元素，getValule并对Value进行累加操作。
	 */
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String str = input.getString(0);
		System.out.println("WordCounter 计数器收到单词："+ str);
		if(!counters.containsKey(str)){
			counters.put(str, 1);
		}else{
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

	}
}
