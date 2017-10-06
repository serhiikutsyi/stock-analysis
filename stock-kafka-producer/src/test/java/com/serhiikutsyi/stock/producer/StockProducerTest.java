package com.serhiikutsyi.stock.producer;

import com.serhiikutsyi.stock.common.StockDeserializer;
import com.serhiikutsyi.stock.domain.Stock;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

/**
 * @author Serhii Kutsyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class StockProducerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockProducerTest.class);

    private static String PRODUCER_TOPIC = "stock.prices";

    private Stock stock;

    @Autowired
    private StockProducer stockProducer;

    private KafkaMessageListenerContainer<String, Stock> container;

    private BlockingQueue<ConsumerRecord<String, Stock>> records;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, PRODUCER_TOPIC);

    @Before
    public void setUp() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        stock = new Stock();
        stock.setDate(LocalDate.parse("2016-01-05 00:00:00", formatter));
        stock.setSymbol("WLTW");
        stock.setOpen(123.43);
        stock.setClose(125.839996);
        stock.setLow(122.309998);
        stock.setHigh(126.25);
        stock.setVolume(2163600.0);

        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StockDeserializer.class);

        DefaultKafkaConsumerFactory<String, Stock> consumerFactory =
                new DefaultKafkaConsumerFactory<String, Stock>(consumerProperties);

        ContainerProperties containerProperties = new ContainerProperties(PRODUCER_TOPIC);

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        records = new LinkedBlockingQueue<>();

        container.setupMessageListener(new MessageListener<String, Stock>() {
            @Override
            public void onMessage(ConsumerRecord<String, Stock> record) {
                records.add(record);
            }
        });

        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }

    @Test
    public void shouldSend() throws Exception {
        stockProducer.send(PRODUCER_TOPIC, stock);
        ConsumerRecord<String, Stock> received = records.poll(10, TimeUnit.SECONDS);
        assertThat(received, hasValue(stock));
    }
}
