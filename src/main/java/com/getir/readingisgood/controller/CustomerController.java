package com.getir.readingisgood.controller;

import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(service.login(customerDTO));
    }

    @PostMapping
    @ResponseBody
    public void persist(@Valid @RequestBody CustomerDTO customerDTO) {
        service.persist(customerDTO);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Page<OrderDTO>> queryOrdersById(@PathVariable @NotNull Long id,
                                                          @RequestParam @NotNull int page,
                                                          @RequestParam @NotNull @Size(max = 50) int size) {
        return ResponseEntity.ok(service.queryOrdersById(id,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationDate"))));
    }

}
