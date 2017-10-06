package com.serhiikutsyi.stock.common;

import com.serhiikutsyi.stock.domain.Stock;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * @author Serhii Kutsyi
 */
public class StockSerializer implements Serializer<Stock> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Stock data) {
        return SerializationUtils.serialize(data);
    }

    @Override
    public void close() {
    }
}
