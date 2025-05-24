package com.ticketnow.crud.service;

import com.ticketnow.crud.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final TicketTypeService ticketTypeService;
    private final EventService eventService;

    public EmailService(TicketTypeService ticketTypeService, EventService eventService) {
        this.ticketTypeService = ticketTypeService;
        this.eventService = eventService;
    }

    public EmailDTO handleEmailContent(OrderEmailDTO orderEmailDTO) {
        List<EmailItemDTO> emailItemDTOs = orderEmailDTO.getOrderEmailItemDTOs().stream().map(orderEmailItemDTO -> {
            TicketTypeDTO ticketTypeDTO = ticketTypeService.get(orderEmailItemDTO.getTicketId());
            return new EmailItemDTO(orderEmailItemDTO.getQuantity(), orderEmailItemDTO.getPrice(), ticketTypeDTO.description());
        }).toList();

        EventDTO eventDTO = eventService.get(orderEmailDTO.getEventId());
        return new EmailDTO(orderEmailDTO.getPaymentRef(),
                orderEmailDTO.getPaymentDate(), eventDTO.location(), eventDTO.date(), orderEmailDTO.getEmail(),
                emailItemDTOs, eventDTO.title(), orderEmailDTO.getMethod());
    }
}
