package com.getir.readingisgood.mapper;

import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toModel(CustomerDTO customerDto);

    CustomerDTO toDTO(Customer customer);

}
