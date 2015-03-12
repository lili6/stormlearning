package com.lili.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 *
 * Created by lili on 15/3/8.
 * 随机发送sentences里面的数据
 */
public class RandomSpout extends BaseRichSpout{
    private SpoutOutputCollector collector;

    private Random rand;

    private static String[] sentences = new String[] {"edi:I'm happy", "marry:I'm angry", "john:I'm sad", "ted:I'm excited", "laden:I'm dangerous"};

    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.collector = collector;
        this.rand = new Random();

    }


    @Override
    public void nextTuple() {
        //持续不断的发送随机语句
        String toSay = sentences[rand.nextInt(sentences.length)];
        this.collector.emit(new Values(toSay));

    }

    /**
     * 用来给发射的value在整个stream中定义一个别名，可以理解为key
     * 此key 再整个topology中唯一
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }

}
