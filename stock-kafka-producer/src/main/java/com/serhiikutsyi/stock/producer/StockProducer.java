package com.serhiikutsyi.stock.producer;

import com.serhiikutsyi.stock.domain.Stock;
import com.serhiikutsyi.stock.service.StockParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Serhii Kutsyi
 */
@Component
public class StockProducer {

    private static final Logger LOG = LoggerFactory.getLogger(StockProducer.class);

    private StockParserService stockParserService;
    private KafkaTemplate<String, Stock> kafkaTemplate;

    @Autowired
    public StockProducer(StockParserService stockParserService, KafkaTemplate<String, Stock> kafkaTemplate) {
        this.stockParserService = stockParserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Stock stock) {
        kafkaTemplate.send(topic, stock);
    }

    @Scheduled(fixedDelay = 1000)
    public void send() throws Exception {
        List<Stock> stockList;
        while (!(stockList = stockParserService.read(30)).isEmpty()) {
            for (Stock stock : stockList) {
                send("stock", stock);
            }
        }
    }
}
