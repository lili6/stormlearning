package com.lili.lifecycle.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.lili.lifecycle.bolt.TransferBolt;
import com.lili.lifecycle.bolt.WriterBolt;
import com.lili.lifecycle.spout.RandomWordSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lili on 2015/3/2.
 * 运行life-cycle

 将starter-1.0.jar 放到/home/lili/env/deploy下面，然后运行

 storm jar starter-1.0.jar com.lili.lifecycle.topology.TopoMain

 删除一个topology ：storm kill ‘life-cycle’

 */
public class TopoMain {
	private static final Logger log = LoggerFactory.getLogger(TopoMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("random", new RandomWordSpout(), 2);
		builder.setBolt("transfer", new TransferBolt(), 4).shuffleGrouping("random");
		builder.setBolt("writer", new WriterBolt(), 4).fieldsGrouping("transfer", new Fields("word"));
		Config conf = new Config();
		conf.setNumWorkers(2);
		conf.setDebug(true);
		if (args.length == 0) {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("localMain",conf,builder.createTopology());
			Utils.sleep(100000);
		} else { //集群模式运行
			log.warn("$$$$$$$$$$$ submitting topology...");
			StormSubmitter.submitTopology("life-cycle", conf, builder.createTopology());
			log.warn("$$$$$$$4$$$ topology submitted !");
		}
	}
}
