package com.serhiikutsyi.stock.service;

import com.serhiikutsyi.stock.domain.Stock;

import java.io.IOException;
import java.util.List;

/**
 * @author Serhii Kutsyi
 */
public interface StockParserService {

    List<Stock> read(int numberOfLines) throws IOException;

}
