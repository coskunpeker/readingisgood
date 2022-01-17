package com.getir.readingisgood.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StatisticDTO {

    private Long orderCount;

    private BigDecimal orderAmount;

    private Long purchasedBookCount;

}
