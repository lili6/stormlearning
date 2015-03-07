package com.lili.kafka;

import kafka.producer.ProducerConfig;

import java.util.Properties;

import java.util.Random;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

/**
 * Created by lili on 15/3/7.
 * 往Kafka发送消息
 * 再kafka服务器上可以通过这种方式查看发送的数据
 * [lili@master bin]$ ./kafka-console-consumer.sh --zookeeper master:2181 --topic order --from-beginning
 * 通过log查看到的数据是乱码，因为有很多偏移量在里面
 */

public class SendMessage {

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
        for(int i=0;i<10;i++){
            int id = r.nextInt(10000000);
            int memberid = r.nextInt(100000);
            int totalprice = r.nextInt(1000)+100;
            int youhui = r.nextInt(100);
            int sendpay = r.nextInt(3);

            StringBuffer data = new StringBuffer();
            data.append(String.valueOf(id))
                    .append("\t")
                    .append(String.valueOf(memberid))
                    .append("\t")
                    .append(String.valueOf(totalprice))
                    .append("\t")
                    .append(String.valueOf(youhui))
                    .append("\t")
                    .append(String.valueOf(sendpay))
                    .append("\t")
                    .append("2014-04-19");
            System.out.println(data.toString());
            producer.send(new KeyedMessage<String, String>("order",data.toString()));
        }

        producer.close();
        System.out.println("send over ------------------");
    }


}
