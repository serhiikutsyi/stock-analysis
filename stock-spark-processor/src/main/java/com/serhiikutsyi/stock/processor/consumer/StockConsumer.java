package com.serhiikutsyi.stock.processor.consumer;

import kafka.serializer.StringDecoder;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Serhii Kutsyi
 */
@Component
public class StockConsumer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(StockConsumer.class);

    @Autowired
    private JavaStreamingContext javaStreamingContext;

    @Value("${kafka.zookeeper}")
    private String bootstrapServers;

    @Value("${kafka.brokerlist}")
    private String brokerlist;

    @Value("${kafka.topic}")
    private String topic;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put("zookeeper.connect", bootstrapServers);
        kafkaParams.put("metadata.broker.list", brokerlist);
        Set<String> topics = Collections.singleton(topic);

        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParams,
                topics
        );

        directKafkaStream.foreachRDD(rdd -> {
            logger.debug("--- New RDD with " + rdd.partitions().size() + " partitions and " + rdd.count() + " records");
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();

    }

}
