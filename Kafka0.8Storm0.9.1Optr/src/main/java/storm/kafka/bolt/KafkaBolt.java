package storm.kafka.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;




/**
 * Bolt implementation that can send Tuple data to Kafka
 * <p/>
 * It expects the producer configuration and topic in storm config under
 * <p/>
 * 'kafka.broker.properties' and 'topic'
 * <p/>
 * respectively.
 */
public class KafkaBolt<K, V> extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaBolt.class);

    public static final String TOPIC = "topic";
    public static final String KAFKA_BROKER_PROPERTIES = "kafka.broker.properties";

    public static final String BOLT_KEY = "key";
    public static final String BOLT_MESSAGE = "message";

    private Producer<K, V> producer;
    private OutputCollector collector;
    private String topic;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        Map configMap = (Map) stormConf.get(KAFKA_BROKER_PROPERTIES);
        Properties properties = new Properties();
        properties.putAll(configMap);
        ProducerConfig config = new ProducerConfig(properties);
        producer = new Producer<K, V>(config);
        this.topic = (String) stormConf.get(TOPIC);
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        K key = null;
        if (input.contains(BOLT_KEY)) {
            key = (K) input.getValueByField(BOLT_KEY);
        }
        V message = (V) input.getValueByField(BOLT_MESSAGE);
        try {
            producer.send(new KeyedMessage<K, V>(topic, key, message));
        } catch (Exception ex) {
            LOG.error("Could not send message with key '" + key + "' and value '" + message + "'", ex);
        } finally {
            collector.ack(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
