package com.serhiikutsyi.stock.service;

import com.serhiikutsyi.stock.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.ParseLocalDate;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Kutsyi
 */
@Component
public class StockCsvParserServiceImpl implements StockParserService {

    private static final Logger LOG = LoggerFactory.getLogger(StockCsvParserServiceImpl.class);

    private ICsvBeanReader reader;
    private String[] header;
    private final CellProcessor[] processors = new CellProcessor[]{
            new ParseLocalDate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            null,
            new ParseDouble(), new ParseDouble(), new ParseDouble(), new ParseDouble(), new ParseDouble()};

    public StockCsvParserServiceImpl(@Value("${stock.data.file}") String filePath) throws IOException {
        reader = new CsvBeanReader(new FileReader(getClass().getClassLoader().getResource(filePath).getFile()), CsvPreference.STANDARD_PREFERENCE);
        header = reader.getHeader(true);
    }

    public List<Stock> read(int numberOfLines) throws IOException {
        List<Stock> stockList = new ArrayList<>();
        int i = 0;
        Stock nextLine;
        while (i < numberOfLines && (nextLine = reader.read(Stock.class, header, processors)) != null) {
            stockList.add(nextLine);
            i++;
        }
        return stockList;
    }

    public void close() throws IOException {
        reader.close();
    }

}
