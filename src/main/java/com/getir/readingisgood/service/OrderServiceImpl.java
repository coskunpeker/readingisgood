package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.dto.PaginationDTO;
import com.getir.readingisgood.dto.StatisticDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.entity.Order;
import com.getir.readingisgood.exception.NegativeStockCountException;
import com.getir.readingisgood.exception.ResourceNotFoundException;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.repository.BookRepository;
import com.getir.readingisgood.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    private final OrderMapper mapper;

    private final StatisticsService statisticsService;

    @Transactional
    @Override
    public void persist(OrderDTO orderDTO) {
        Book book = bookRepository.findById(orderDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Any book could not be found with provided id!"));

        final int stockCount = book.getStockCount() - orderDTO.getQuantity();
        if (stockCount >= 0) {
            book.setStockCount(stockCount);
            bookRepository.save(book);
        } else {
            throw new NegativeStockCountException();
        }

        Order savedOrder = orderRepository.save(mapper.toModel(orderDTO));

        statisticsService.persist(StatisticDTO.builder()
                .orderCount(1L)
                .orderAmount(savedOrder.getPrice().multiply(BigDecimal.valueOf(savedOrder.getQuantity())))
                .purchasedBookCount((long) savedOrder.getQuantity())
                .build());
    }

    @Transactional
    @Override
    public OrderDTO queryById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Any order could not be found with provided id!"));
        return mapper.toDTO(order);
    }

    @Transactional
    @Override
    public Page<OrderDTO> queryByDate(PaginationDTO pagination) {
        return orderRepository.findByCreationDateBetween(pagination.getStartDate(), pagination.getEndDate(),
                        PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Direction.DESC, "creationDate")))
                .map(mapper::toDTO);
    }

}
