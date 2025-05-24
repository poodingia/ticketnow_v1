package com.ticketnow.crud.service;


import com.ticketnow.crud.domain.Ticket;
import com.ticketnow.crud.domain.TicketType;
import com.ticketnow.crud.dto.TicketDTO;
import com.ticketnow.crud.repos.TicketRepository;
import com.ticketnow.crud.repos.TicketTypeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ticketnow.crud.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TicketService {
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;

    public TicketService(final TicketTypeRepository ticketTypeRepository, final TicketRepository ticketRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<TicketDTO> findAll() {
        final List<Ticket> tickets = ticketRepository.findAll(Sort.by("id"));
        return tickets.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TicketDTO get(final Integer id) {
        return ticketRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);
        return ticketRepository.save(ticket).getId();
    }

    public void createTicketsForEvent(final Integer eventId) {
        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        List<Ticket> tickets = new ArrayList<>();
        for (TicketType ticketType : ticketTypes) {
            if (ticketType.isBoughtDirectly()) { // Only create tickets for ticket types that are bought directly
                for (int i = 0; i < ticketType.getQuantity(); i++) {
                    Ticket ticket = new Ticket();
                    ticket.setType(ticketType);
                    ticket.setStatus(false);
                    tickets.add(ticket);
                }
            }
        }
        ticketRepository.saveAll(tickets);
    }

    public void bookTicket(List<Integer> id) {
        List<Ticket> tickets = ticketRepository.findAllById(id);
        for (Ticket ticket : tickets) {
            ticket.setStatus(true);
        }
        ticketRepository.saveAll(tickets);
    }

    public void delete(final Integer id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket) {
        return new TicketDTO(ticket.getId(), ticket.getStatus(), ticket.getOwnerId(), null, null);
    }

    private void mapToEntity(final TicketDTO ticketDTO, final Ticket ticket) {
        ticket.setStatus(ticketDTO.status());
        ticket.setOwnerId(ticketDTO.ownerId());
    }

    void updateTickets(Map<Integer, Integer> ticketMap, String userId) {
        List<Integer> ticketIds = new ArrayList<>(ticketMap.keySet());
        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        tickets.forEach(ticket -> {
            ticket.setStatus(true);
            ticket.setOwnerId(userId);
        });
        ticketRepository.saveAll(tickets);
    }
}
