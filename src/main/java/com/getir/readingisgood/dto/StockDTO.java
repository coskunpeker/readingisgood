package com.getir.readingisgood.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class StockDTO {

    @NotNull
    private Long bookId;

    @NotNull
    private int stockCount;

}
