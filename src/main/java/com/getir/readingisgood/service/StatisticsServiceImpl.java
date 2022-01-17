package com.getir.readingisgood.service;

import com.getir.readingisgood.dto.StatisticDTO;
import com.getir.readingisgood.dto.StatisticsDTO;
import com.getir.readingisgood.entity.Statistics;
import com.getir.readingisgood.mapper.StatisticsMapper;
import com.getir.readingisgood.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository repository;

    private final StatisticsMapper mapper;

    @Override
    public List<StatisticsDTO> queryMonthlyOrderStatistics() {

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void persist(StatisticDTO statisticDTO) {
        Month currentMonth = LocalDate.now().getMonth();
        repository.findByMonth(currentMonth).ifPresentOrElse(result -> {

            result.setTotalCountOfOrders(result.getTotalCountOfOrders() + statisticDTO.getOrderCount());
            result.setTotalAmountOfOrders(result.getTotalAmountOfOrders().add(statisticDTO.getOrderAmount()));
            result.setTotalCountOfPurchasedBooks(result.getTotalCountOfPurchasedBooks() + statisticDTO.getPurchasedBookCount());
            result.setUpdateDate(new Date());
            repository.save(result);

        }, () -> repository.save(Statistics.builder()
                .month(currentMonth)
                .totalCountOfOrders(statisticDTO.getOrderCount())
                .totalAmountOfOrders(statisticDTO.getOrderAmount())
                .totalCountOfPurchasedBooks(statisticDTO.getPurchasedBookCount())
                .creationDate(new Date())
                .updateDate(new Date())
                .build()));
    }
}
