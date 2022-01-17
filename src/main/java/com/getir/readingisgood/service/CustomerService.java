package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    String login(CustomerDTO customerDTO);

    void persist(CustomerDTO customerDTO);

    Page<OrderDTO> queryOrdersById(Long customerId, Pageable pageable);
}
