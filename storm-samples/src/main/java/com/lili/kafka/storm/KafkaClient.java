package com.lili.kafka.storm;

import com.lili.kafka.simple.LogProducer;

/**
 * Created by lili on 2015/3/11.
 * 做为kafa数据的生产者
 */
public class KafkaClient {
/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogProducer producer = null;
		try{
			producer = new LogProducer();
			int i=0;
			String msg ;
			while(true){
				msg =    "I love U[" + i +"]"              ;
				producer.send(KafkaDemoTopo.topic, msg);
				System.out.println("开始发送数据：："+ msg);
				i++;
				Thread.sleep(2000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(producer != null){
				producer.close();
			}
		}

	}
}
