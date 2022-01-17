package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.StockDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.exception.NegativeStockCountException;
import com.getir.readingisgood.exception.ResourceAlreadyExistsException;
import com.getir.readingisgood.exception.ResourceNotFoundException;
import com.getir.readingisgood.mapper.BookMapper;
import com.getir.readingisgood.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    private final BookMapper bookMapper;

    @Transactional
    @Override
    public void persist(BookDTO bookDTO) {
        if (!repository.existsByIsbn(bookDTO.getIsbn())) {
            repository.save(bookMapper.toModel(bookDTO));
        } else {
            throw new ResourceAlreadyExistsException("Book is already exist with provided isbn!");
        }
    }

    @Transactional
    @Override
    public void updateStock(StockDTO stockDTO) {
        Book book = repository.findById(stockDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Any book could not be found with provided id!"));

        final int stockCount = book.getStockCount() + stockDTO.getStockCount();

        if (stockCount < 0){
            throw new NegativeStockCountException();
        }

        book.setStockCount(stockCount);
        repository.save(book);

    }

}
