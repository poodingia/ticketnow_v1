package com.ticketnow.crud.repos;

import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.dto.EventOrganizerDTO;
import com.ticketnow.crud.dto.RevenueDTO;
import com.ticketnow.crud.dto.TitleDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByStartSaleDateBetween(LocalDateTime startSaleDateAfter, LocalDateTime startSaleDateBefore);

    @Query("SELECT COALESCE(SUM(e.revenue), 0) FROM Event e WHERE e.userId = ?1 and e.isApproved = true")
    Long findTotalRevenueByUserId(String userId);

    @Query("SELECT new com.ticketnow.crud.dto.RevenueDTO(e.title, e.revenue) from Event e " +
            "WHERE e.userId = ?1 and e.isApproved = true ORDER BY e.revenue DESC")
    List<RevenueDTO> findTopFiveRevenueEventsByUserId(String userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.revenue), 0) FROM Event e where e.isApproved = true")
    Long findTotalRevenue();

    @Query("SELECT new com.ticketnow.crud.dto.RevenueDTO(e.title, e.revenue) from Event e " +
            "where e.isApproved = true ORDER BY e.revenue DESC")
    List<RevenueDTO> findTopFiveRevenueEvents(Pageable pageable);

    @Query("SELECT new com.ticketnow.crud.dto.EventOrganizerDTO(e.id, e.title,  e.location, e.description, e.bgImagePath, e.date, e.max, e.isApproved) " +
            "FROM Event e WHERE e.userId = ?1")
    List<EventOrganizerDTO> findALlEventDTOsByOrganizerId(String userId);

    @Query("select e from Event e where e.isApproved = :approved")
    List<Event> findAllByApproved(Boolean approved);

    @Query("select new com.ticketnow.crud.dto.TitleDTO(e.id, e.title) from Event e where e.id in :ids")
    List<TitleDTO> findAllEventNames(List<Integer> ids);

    @Query("select e.title from Event e where e.id = :id and e.userId = :userId")
    String findEventNameById(Integer id, String userId);
}
