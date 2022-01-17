package com.getir.readingisgood.mapper;

import com.getir.readingisgood.dto.StatisticsDTO;
import com.getir.readingisgood.entity.Statistics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    Statistics toModel(StatisticsDTO statisticsDTO);

    StatisticsDTO toDTO(Statistics statistics);

}