package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.StatisticDTO;
import com.getir.readingisgood.dto.StatisticsDTO;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> queryMonthlyOrderStatistics();

    void persist(StatisticDTO statisticDTO);
}
