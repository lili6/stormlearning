package com.lili;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import com.lili.bolt.ExclaimBasicBolt;
import com.lili.bolt.PrintBolt;
import com.lili.spout.RandomSpout;

/**
 * Created by lili on 15/3/8.
 */
public class ExclaimBasicTopo {
    public static void main(String[] args) throws Exception {
        // 实例化TopologyBuilder类。
        TopologyBuilder builder = new TopologyBuilder();
        //设置喷发节点并分配并发数（此处无），该并发数将控制该对象再集群中的线程数
        builder.setSpout("spout", new RandomSpout());
        // 设置数据处理节点并分配并发数。指定该节点接收喷发节点的策略为随机方式。
        builder.setBolt("exclaim", new ExclaimBasicBolt()).shuffleGrouping("spout");
        //第二个处理节点
        builder.setBolt("print", new PrintBolt()).shuffleGrouping("exclaim");

        Config conf = new Config();
        conf.setDebug(false);

        if (args != null && args.length > 0) {//提交到集群上运行
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else { //本地集群模式运行

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("exclaim-test", conf, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("exclaim-test");
            cluster.shutdown();
        }
    }

}
