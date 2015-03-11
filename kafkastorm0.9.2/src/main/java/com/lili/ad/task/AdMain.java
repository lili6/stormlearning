package com.lili.ad.task;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
/**
 *
 *
 */
 
public class AdMain {
	
    public static final String STORM_PATH = "/data/apache-storm-0.9.2-incubating/conf/";
	
	//配置文件
	public static final String FILE_PATH = "adjdbc.properties";
	
	
	public static final String ZK_ROOT = "/ad";
	
	
	public static Properties props = null;
	
	//加载JDBC配置
	static {
		props = new Properties();
		try {
			String configPath = STORM_PATH  + FILE_PATH;
		    InputStream in = new BufferedInputStream(new FileInputStream(configPath));
			props.load(in);
		    in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

   public static void main(String[] args) throws Exception {
	   
	   
	   /**
	    * 第三方jar包
	    */
	   if(args.length < 1){
		   System.out.println("主题不能为空..");
		   System.exit(0);
	   }
	   
	   if(args.length < 2 ){
		   System.out.println("worker数不能为空..");
		   System.exit(0);
	   }
	   
	   String topicName = args[0];
	   
	   //worker和spout数
	   int workNumber = new Integer(args[1]);
	   
	   //bolt线程数
	   int boltNumber =  workNumber * 3;
	   
	   String allTopics[] = props.getProperty("alltopic").split(",");
	    
	   //条数
	   int  countThreshold =  new Integer(props.getProperty("count")) ;
		
	   //时间
	   int  countSed =  new Integer(props.getProperty("timeout")) ;
		
	   if(countThreshold < 1 || countSed < 1){
		   System.out.println("时间或者条数设置不对.....");
		   System.exit(0);
	   }
	   
	   boolean ifContainTopic = false;
	   
	   for(String st : allTopics){
		   
		   if(st.equals(topicName)){
			   
			   ifContainTopic = true;
			   
			   break;
		   
		   }
		   
	   }
	   //配置文件中alltopic属性中不包括此主题
	   if(! ifContainTopic){
		   System.out.println("请确认 ["+STORM_PATH+FILE_PATH+"] 配置文件中有此主题 ");
		   System.exit(0);
	   }
	   
	  /**
	   * storm主题名, jdbc url,  zk url
	   */
	   //zk消费下标
	   try {
		   /*GlobalPartitionInformation info = new GlobalPartitionInformation();  
           info.addPartition(0, new Broker("tank19",9092));  
           BrokerHosts brokerHosts = new StaticHosts(info);   */
           TopologyBuilder builder = new TopologyBuilder();
           BrokerHosts brokerHosts = new ZkHosts(props.getProperty("zk"));
           //topic
           String topic = topicName;  
           //zk path
           String zkRoot = ZK_ROOT;
           //znode记录消费下标
           String spoutId = topicName;  
          // builder.setSpout("1", new SimpleSpout(), 2);
           SpoutConfig spoutConfig  =  new SpoutConfig(brokerHosts, topic, zkRoot, spoutId);
           spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
           builder.setSpout("adspout", new KafkaSpout(spoutConfig), workNumber);
           builder.setBolt("adbolt",new AdLogBolt(),boltNumber).shuffleGrouping("adspout");
           Config config = new Config();
           config.setDebug(false);
           config.setNumWorkers(workNumber);
           //设置主题名 在加载 jdbc url时用到
           config.put("topic", topicName);
           config.put("count", countThreshold);
           config.put("timeout", countSed);
           Iterator<Entry<Object, Object>> it = props.entrySet().iterator();  
           while (it.hasNext()) {  
               Entry<Object, Object> entry = it.next();  
               String key = (String)entry.getKey();  
               String value = (String)entry.getValue();
               config.put(key, value);
           }  
           
           StormSubmitter.submitTopology(topicName, config, builder.createTopology());
            		
           } 
    	   catch (Exception e) {
               e.printStackTrace(); 
           }
 

              //stormCluster();

   }

 


      
   }   