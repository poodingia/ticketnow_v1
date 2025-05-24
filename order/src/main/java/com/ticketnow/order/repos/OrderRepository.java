package com.ticketnow.order.repos;

import com.ticketnow.order.domain.Order;
import com.ticketnow.order.domain.Status;
import com.ticketnow.order.dto.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByPaymentRef(String ref);

    List<Order> findAllByUserIdAndStatus(String userId, Status status);

    @Query("SELECT new com.ticketnow.order.dto.StatisticDTO" +
            "(count(o.id), sum(o.price), sum(o.quantity)) " +
            "FROM Order o " +
            "where o.userId = ?1 and o.status = ?2")
    StatisticDTO findCustomerStatistic(String userId, Status status);


    @Query("SELECT new com.ticketnow.order.dto.StatisticDTO" +
            "(count(o.id), sum(o.price), sum(o.quantity)) " +
            "FROM Order o " +
            "where o.eventId = ?1 and o.status = ?2")
    StatisticDTO findOrganizerStatistic(Integer eventId, Status status);

    List<Order> findAllByEventIdAndStatus(Integer eventId, Status status);
}