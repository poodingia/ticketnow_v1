package com.ticketnow.crud.repos;

import com.ticketnow.crud.domain.TicketType;
import com.ticketnow.crud.dto.QuantityDTO;
import com.ticketnow.crud.dto.TicketTypePrepareDTO;
import com.ticketnow.crud.dto.TitleDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {
    List<TicketType> findByEventId(int eventId);

    @Query("SELECT COALESCE(SUM(t.reservedQuantity), 0) FROM TicketType t WHERE t.event.userId = ?1")
    Long findTotalTicketsSoldByUserId(String userId);


    @Query("SELECT new com.ticketnow.crud.dto.QuantityDTO(t.description, t.reservedQuantity) " +
            "FROM TicketType t WHERE t.event.userId = ?1 " +
            "ORDER BY t.reservedQuantity DESC")
    List<QuantityDTO> findTopTicketTypesRevenueByUserId(String userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.reservedQuantity), 0) FROM TicketType t")
    Long findTotalTicketsSold();

    @Query("SELECT new com.ticketnow.crud.dto.QuantityDTO(t.description, t.reservedQuantity) " +
            "FROM TicketType t " +
            "ORDER BY t.reservedQuantity DESC")
    List<QuantityDTO> findTopTicketTypesRevenue(Pageable pageable);

    @Query("SELECT new com.ticketnow.crud.dto.TicketTypePrepareDTO(t.id, t.quantity, t.price, e.id) " +
            "FROM Event e JOIN e.eventTicketTypes t " +
            "WHERE e.startSaleDate BETWEEN :start AND :end")
    List<TicketTypePrepareDTO> findTicketTypesForEventsStartingTomorrow(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    @Query("SELECT new com.ticketnow.crud.dto.TitleDTO(t.id, t.description) FROM TicketType t WHERE t.id IN :ids")
    List<TitleDTO> getTitles(List<Integer> ids);
}