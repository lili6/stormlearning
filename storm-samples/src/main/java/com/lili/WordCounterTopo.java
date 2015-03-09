package com.lili;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.lili.bolt.WordCounter;
import com.lili.bolt.WordNormalizer;
import com.lili.spout.WordReader;

/**
 * 功能说明：
 * 设计一个topology，来实现对一个句子里面的单词出现的频率进行统计。
 * 整个topology分为三个部分：
 *                 WordReader:数据源，负责发送单行文本记录（句子）
 *                 WordNormalizer:负责将单行文本记录（句子）切分成单词
 *                 WordCounter:负责对单词的频率进行累加
 *
 * Created by lili on 2015/3/9.
 */
public class WordCounterTopo {

	/**
	 * @param args 文件路径
	 */
	public static void main(String[] args)throws Exception {

		String path =        WordCounterTopo.class.getClassLoader().getResource("").getPath();
		System.out.print("path:::" +path);
		// Storm框架支持多语言，在JAVA环境下创建一个拓扑，需要使用TopologyBuilder进行构建
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader",new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter(),1).fieldsGrouping("word-normalizer", new Fields("word"));
		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(2);
//		conf.put("wordsFile","/root/workspace1/com.jd.storm.demo/src/main/resources/words.txt");
		conf.put("wordsFile",path +"words.txt");
		conf.setDebug(true);
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        /*
         * 定义一个LocalCluster对象来定义一个进程内的集群。提交topology给这个虚拟的集群和提交topology给分布式集群是一样的。
         * 通过调用submitTopology方法来提交topology， 它接受三个参数：要运行的topology的名字，一个配置对象以及要运行的topology本身。
         * topology的名字是用来唯一区别一个topology的，这样你然后可以用这个名字来杀死这个topology的。前面已经说过了， 你必须显式的杀掉一个topology， 否则它会一直运行。
         */
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("wordCounterTopology", conf, builder.createTopology());
		Thread.sleep(3000);
		System.out.println("睡眠3秒后杀掉Topology。。。。");
		cluster.killTopology("wordCounterTopology");
		cluster.shutdown();

	}
}
