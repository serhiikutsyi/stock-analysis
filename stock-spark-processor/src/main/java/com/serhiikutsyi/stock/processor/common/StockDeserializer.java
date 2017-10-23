package com.serhiikutsyi.stock.processor.common;

import com.serhiikutsyi.stock.processor.domain.Stock;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * @author Serhii Kutsyi
 */
public class StockDeserializer implements Deserializer<Stock> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Stock deserialize(String topic, byte[] data) {
        return (Stock) SerializationUtils.deserialize(data);
    }

    @Override
    public void close() {
    }
}
