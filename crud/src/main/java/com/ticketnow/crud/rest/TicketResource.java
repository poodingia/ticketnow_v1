package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.TicketDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crud/ticket")
@ResponseStatus(HttpStatus.OK)
public class TicketResource {
    private final TicketService ticketService;

    public TicketResource(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public APIResponse<List<TicketDTO>> getAllTickets() {
        APIResponseBuilder<List<TicketDTO>> builder = new APIResponseBuilder<>();
        return builder.data(ticketService.findAll()).ok().build();
    }

    @GetMapping("/{id}")
    public APIResponse<TicketDTO> getTicket(@PathVariable(name = "id") final Integer id) {
        APIResponseBuilder<TicketDTO> builder = new APIResponseBuilder<>();
        return builder.data(ticketService.get(id)).ok().build();
    }

    @PostMapping
    public APIResponse<Integer> createTicket(@RequestBody @Valid final TicketDTO ticketDTO) {
        final Integer createdId = ticketService.create(ticketDTO);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(createdId).ok().build();
    }

    @PutMapping("/book")
    public APIResponse<Void> bookTicket(@RequestBody List<Integer> ids) {
        ticketService.bookTicket(ids);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteTicket(@PathVariable(name = "id") final Integer id) {
        ticketService.delete(id);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }
}