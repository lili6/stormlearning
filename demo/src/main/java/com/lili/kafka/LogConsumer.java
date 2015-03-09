package com.lili.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 部署kafka 两个节点 kafka-0,kafka-1
 mkdir /kafka-0/tmp 用来存放日志
 修改配置：/kafka-0/config/server.properties
 broker.id=1
 port=9092
 修改配置：/kafka-1/config/server.properties
 broker.id=1
 port=9093
 修改每台服务器的config/server.properties
 broker.id：  唯一，填数字，本文中分别为132/133/134
 host.name：唯一，填服务器IP，之前配置时，把中间的'.'给忘写了，导致kafka集群形同虚设（基本只有leader机器在起作用），以及一系列莫名其妙的问题，伤啊
 zookeeper.connect=192.168.40.134:2181,192.168.40.132:2181,192.168.40.133:2181

 注意host.name 和zookeeper.connect 必须为ip地址..
启动：
 *  JMS_PORT=9998 ./bin/kafka-server-start.sh ./config/server.properties &
 *  JMS_PORT=9997 ./bin/kafka-server-start.sh ./config/server.properties &
 * Created by liguofang on 2015/2/6.
 */
public class LogConsumer {
	private ConsumerConfig config;
	private String topic;
	private int partitionsNum;
	private MessageExecutor executor;
	private ConsumerConnector connector;
	private ExecutorService threadPool;
	public LogConsumer(String topic,int partitionsNum,MessageExecutor executor) throws Exception{
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("consumer.properties"));
		config = new ConsumerConfig(properties);
		this.topic = topic;
		this.partitionsNum = partitionsNum;
		this.executor = executor;
	}

	public void start() throws Exception{
		connector = Consumer.createJavaConsumerConnector(config);
		Map<String,Integer> topics = new HashMap<String,Integer>();
		topics.put(topic, partitionsNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
		List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);
		threadPool = Executors.newFixedThreadPool(partitionsNum);
		for(KafkaStream<byte[], byte[]> partition : partitions){
			threadPool.execute(new MessageRunner(partition));
		}
	}


	public void close(){
		try{
			threadPool.shutdownNow();
		}catch(Exception e){
			//
		}finally{
			connector.shutdown();
		}

	}

	class MessageRunner implements Runnable{
		private KafkaStream<byte[], byte[]> partition;

		MessageRunner(KafkaStream<byte[], byte[]> partition) {
			this.partition = partition;
		}

		public void run(){
			ConsumerIterator<byte[], byte[]> it = partition.iterator();
			while(it.hasNext()){
				MessageAndMetadata<byte[],byte[]> item = it.next();
				System.out.println("partiton:" + item.partition());
				System.out.println("offset:" + item.offset());
				executor.execute(new String(item.message()));//UTF-8
			}
		}
	}

	interface MessageExecutor {

		public void execute(String message);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogConsumer consumer = null;
		try{
			MessageExecutor executor = new MessageExecutor() {

				public void execute(String message) {
					System.out.println(message);

				}
			};
			consumer = new LogConsumer("test-topic", 2, executor);
			consumer.start();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//            if(consumer != null){
//                consumer.close();
//            }
		}

	}

}
