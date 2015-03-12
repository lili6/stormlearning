package com.lili.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * 将接收到的数据打印出来
 * Created by lili on 15/3/8.
 */
public class PrintBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
//        String rec = tuple.getString(0);     //用序号或者字段名的方式都可以获取到数据 OK
        String rec = tuple.getStringByField("excl_sentence");   //OK
        System.err.println("String received:::" + rec);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //end ,do nothing.
    }
}
