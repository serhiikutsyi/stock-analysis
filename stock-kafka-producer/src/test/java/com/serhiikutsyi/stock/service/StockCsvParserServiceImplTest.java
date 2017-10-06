package com.serhiikutsyi.stock.service;

import com.serhiikutsyi.stock.domain.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * @author Serhii Kutsyi
 */
@RunWith(JUnit4.class)
public class StockCsvParserServiceImplTest {

    private StockParserService stockParserService;
    private Stock stock;

    @Before
    public void init() throws IOException {
        stockParserService = new StockCsvParserServiceImpl("stock.csv");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        stock = new Stock();
        stock.setDate(LocalDate.parse("2016-01-05 00:00:00", formatter));
        stock.setSymbol("WLTW");
        stock.setOpen(123.43);
        stock.setClose(125.839996);
        stock.setLow(122.309998);
        stock.setHigh(126.25);
        stock.setVolume(2163600.0);
    }

    @Test
    public void shouldRead() throws Exception {
        assertEquals(stockParserService.read(1).get(0).toString(), stock.toString());
    }

}
