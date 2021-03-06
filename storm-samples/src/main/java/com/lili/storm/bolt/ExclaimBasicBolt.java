package com.lili.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 接收到数据在后面添加！Exclaim，然后继续发送出去
 * Created by lili on 15/3/8.
 */
public class ExclaimBasicBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String sentence =(String) tuple.getValue(0);
        String out = sentence +"!Exclaim";
        basicOutputCollector.emit(new Values(out));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("excl_sentence"));
    }
}
