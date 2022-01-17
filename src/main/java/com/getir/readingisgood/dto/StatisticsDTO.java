package com.getir.readingisgood.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Date;

@Data
public class StatisticsDTO {

    private Long id;

    private Month month;

    private Long totalCountOfOrders;

    private BigDecimal totalAmountOfOrders;

    private Long totalCountOfPurchasedBooks;

    private Date creationDate;

    private Date updateDate;

}
