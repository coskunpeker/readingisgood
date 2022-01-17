package com.getir.readingisgood.repository;

import com.getir.readingisgood.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
//@JaversSpringDataAuditable
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCreationDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE (:customerId = o.customer.id)")
    Optional<Page<Order>> findByCustomerId(Long customerId, Pageable pageable);

}
