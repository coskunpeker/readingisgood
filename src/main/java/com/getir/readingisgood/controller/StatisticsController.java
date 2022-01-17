package com.getir.readingisgood.controller;

import com.getir.readingisgood.dto.StatisticsDTO;
import com.getir.readingisgood.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping("/monthly")
    public List<StatisticsDTO> queryMonthlyOrderStatistics() {
        return service.queryMonthlyOrderStatistics();
    }

}
