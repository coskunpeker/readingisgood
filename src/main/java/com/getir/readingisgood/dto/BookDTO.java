package com.getir.readingisgood.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class BookDTO {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String isbn;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private int stockCount;

    private Date creationDate;

}
