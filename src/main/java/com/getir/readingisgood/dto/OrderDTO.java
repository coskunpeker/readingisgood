package com.getir.readingisgood.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class OrderDTO {

    @NotNull
    @Min(value = 1L)
    private int quantity;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long customerId;

    @NotNull
    private Long bookId;

    private Date creationDate;

}
