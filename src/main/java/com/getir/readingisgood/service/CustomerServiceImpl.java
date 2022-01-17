package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.exception.ResourceAlreadyExistsException;
import com.getir.readingisgood.exception.ResourceNotFoundException;
import com.getir.readingisgood.mapper.CustomerMapper;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.repository.CustomerRepository;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    void initCustomerDatabase() {
        List.of(CustomerDTO.builder()
                        .email("test@gmail.com")
                        .password("test")
                        .build(), CustomerDTO.builder()
                        .email("test2@gmail.com")
                        .password("test2")
                        .build())
                .forEach(this::persist);
    }

    @Override
    public String login(CustomerDTO customerDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerDTO.getEmail(),
                customerDTO.getPassword()));
        return jwtTokenProvider.createToken(customerDTO.getEmail());
    }

    @Transactional
    @Override
    public void persist(CustomerDTO customerDTO) {
        if (!customerRepository.existsByEmail(customerDTO.getEmail())) {
            customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            customerRepository.save(customerMapper.toModel(customerDTO));
        } else {
            throw new ResourceAlreadyExistsException("Customer already exists!");
        }
    }

    @Override
    public Page<OrderDTO> queryOrdersById(Long id, Pageable pageable) {
        return orderRepository.findByCustomerId(id, pageable)
                .orElseThrow(() -> new ResourceNotFoundException("Any customer could not be found with provided id!"))
                .map(orderMapper::toDTO);
    }
}
