package com.getir.readingisgood.repository;

import com.getir.readingisgood.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Optional;

@Service
//@JaversSpringDataAuditable
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Optional<Statistics> findByMonth(Month month);

}
