package com.ticketnow.crud.service;

import com.ticketnow.crud.dto.QuantityDTO;
import com.ticketnow.crud.dto.RevenueDTO;
import com.ticketnow.crud.dto.RevenueDashboardDTO;
import com.ticketnow.crud.repos.EventRepository;
import com.ticketnow.crud.repos.TicketTypeRepository;
import com.ticketnow.crud.utils.AuthenticationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public StatisticService(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository) {
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public RevenueDashboardDTO generateOrganizerDashboard() {
        Pageable pageable = PageRequest.of(0, 5); // Top 5 results
        String organizerId = AuthenticationUtils.extractUserId();
        Long totalRevenue = eventRepository.findTotalRevenueByUserId(organizerId);
        Long totalTicketsSold = ticketTypeRepository.findTotalTicketsSoldByUserId(organizerId);
        List<RevenueDTO> topFiveRevenueEvents = eventRepository.findTopFiveRevenueEventsByUserId(organizerId, pageable);
        List<QuantityDTO> topFiveTicketTypes = ticketTypeRepository.findTopTicketTypesRevenueByUserId(organizerId, pageable);
        return new RevenueDashboardDTO(totalRevenue, totalTicketsSold, topFiveRevenueEvents, topFiveTicketTypes);
    }

    public RevenueDashboardDTO generateAdminDashboard() {
        Pageable pageable = PageRequest.of(0, 5); // Top 5 results
        Long totalRevenue = eventRepository.findTotalRevenue();
        Long totalTicketsSold = ticketTypeRepository.findTotalTicketsSold();
        List<RevenueDTO> topFiveRevenueEvents = eventRepository.findTopFiveRevenueEvents(pageable);
        List<QuantityDTO> topFiveTicketTypes = ticketTypeRepository.findTopTicketTypesRevenue(pageable);
        return new RevenueDashboardDTO(totalRevenue, totalTicketsSold, topFiveRevenueEvents, topFiveTicketTypes);
    }
}
