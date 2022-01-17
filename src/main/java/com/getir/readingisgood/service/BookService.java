package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.StockDTO;

public interface BookService {

    void persist(BookDTO bookDTO);

    void updateStock(StockDTO stockDTO);

}
