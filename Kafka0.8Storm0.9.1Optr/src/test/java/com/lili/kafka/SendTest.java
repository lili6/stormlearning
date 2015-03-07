package com.lili.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

/**
 * Created by lili on 15/3/7.
 * 再kafka服务器上可以通过这种方式查看发送的数据
 * [lili@master bin]$ ./kafka-console-consumer.sh --zookeeper master:2181 --topic test --from-beginning

 */
public class SendTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("zookeeper.connect", "master:2181,slave1:2181,slave2:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("producer.type", "async");
        props.put("compression.codec", "1");
        props.put("metadata.broker.list", "master:9092");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        Random r = new Random();
        for(int i=0;i<1;i++){
            int id = r.nextInt(10000000);
            int memberid = r.nextInt(100000);
            int totalprice = r.nextInt(1000)+100;
            int youhui = r.nextInt(100);
            int sendpay = r.nextInt(3);
            String data="helloToday";
            System.out.println(data);
            producer.send(new KeyedMessage<String, String>("test",data.toString()));
        }

        producer.close();
        System.out.println("send over ------------------");
    }

}
