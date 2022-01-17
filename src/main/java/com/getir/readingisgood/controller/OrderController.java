package com.getir.readingisgood.controller;

import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.dto.PaginationDTO;
import com.getir.readingisgood.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    @ResponseBody
    public void persist(@Valid @RequestBody OrderDTO orderDTO) {
        service.persist(orderDTO);
    }

    @GetMapping("/{id}")
    public OrderDTO queryById(@PathVariable @NotNull Long id) {
        return service.queryById(id);
    }

    @PostMapping("/query")
    public ResponseEntity<Page<OrderDTO>> queryByDate(@RequestBody PaginationDTO pagination) {
        return ResponseEntity.ok(service.queryByDate(pagination));
    }

}
