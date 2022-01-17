package com.getir.readingisgood.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class PaginationDTO {

    @NotNull
    @Size(max = 50)
    private int size;

    @NotNull
    private int page;

    private Date startDate;

    private Date endDate;

}
