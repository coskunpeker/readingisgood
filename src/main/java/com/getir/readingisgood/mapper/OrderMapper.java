package com.getir.readingisgood.mapper;

import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target="customer.id", source="customerId"),
            @Mapping(target="book.id", source="bookId"),
    })
    Order toModel(OrderDTO orderDTO);

    @Mappings({
            @Mapping(target="customerId", source="order.customer.id"),
            @Mapping(target="bookId", source="order.book.id"),
    })
    OrderDTO toDTO(Order order);

}
