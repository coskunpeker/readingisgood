package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.dto.PaginationDTO;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface OrderService {

    void persist(OrderDTO orderDTO);

    OrderDTO queryById(Long id);

    Page<OrderDTO> queryByDate(PaginationDTO pagination);
}
