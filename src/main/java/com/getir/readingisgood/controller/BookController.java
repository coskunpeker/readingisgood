package com.getir.readingisgood.controller;

import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.StockDTO;
import com.getir.readingisgood.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    @ResponseBody
    public void persist(@Valid @RequestBody BookDTO bookDTO) {
        service.persist(bookDTO);
    }

    @PatchMapping
    @ResponseBody
    public void updateStock(@Valid @RequestBody StockDTO stockDTO) {
        service.updateStock(stockDTO);
    }

}
