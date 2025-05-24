package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.TicketTypeValidDTO;
import com.ticketnow.crud.dto.TicketTypeDTO;
import com.ticketnow.crud.dto.TitleDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.TicketTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crud/ticket-type")
@ResponseStatus(HttpStatus.OK)
public class TicketTypeResource {
    private final TicketTypeService ticketTypeService;

    public TicketTypeResource(final TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping
    public APIResponse<List<TicketTypeDTO>> getAllTicketTypes() {
        APIResponseBuilder<List<TicketTypeDTO>> builder = new APIResponseBuilder<>();
        return builder.data(ticketTypeService.findAll()).ok().build();
    }

    @GetMapping("/name")
    public List<TitleDTO> getEventNames(@RequestParam(name = "id") List<Integer> ids) {
        return ticketTypeService.getTypeNames(ids);
    }

    @GetMapping("/{id}")
    public APIResponse<TicketTypeDTO> getTicketType(@PathVariable(name = "id") final Integer id) {
        APIResponseBuilder<TicketTypeDTO> builder = new APIResponseBuilder<>();
        return builder.data(ticketTypeService.get(id)).ok().build();
    }

    @PostMapping
    public APIResponse<Integer> createTicketType(@RequestBody @Valid final TicketTypeDTO ticketTypeDTO) {
        final Integer createdId = ticketTypeService.create(ticketTypeDTO);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(createdId).ok().build();
    }

    @PutMapping("/{id}")
    public APIResponse<Integer> updateTicketType(@PathVariable(name = "id") final Integer id,
                                                 @RequestBody @Valid final TicketTypeDTO ticketTypeDTO) {
        ticketTypeService.update(id, ticketTypeDTO);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(id).ok().build();
    }

    @PutMapping("/list")
    public APIResponse<List<Integer>> updateTicketTypeList(@RequestBody final List<TicketTypeDTO> ticketTypeDTO) {
        List<Integer> ids = ticketTypeService.updateTicketTypeList(ticketTypeDTO);
        APIResponseBuilder<List<Integer>> builder = new APIResponseBuilder<>();
        return builder.data(ids).ok().build();
    }

    @PutMapping("/{id}/reservation/{quantity}")
    public APIResponse<Integer> reserveTicketType(@PathVariable(name = "id") final Integer id,
                                                  @PathVariable(name = "quantity") final Integer quantity) {
        ticketTypeService.reserve(id, quantity);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(id).ok().build();
    }

    @PutMapping("/validate")
    public Boolean reserveTicketType(@RequestBody List<TicketTypeValidDTO> ticketAdjustments) {
        return ticketTypeService.validateTicketTypes(ticketAdjustments);
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteTicketType(@PathVariable(name = "id") final Integer id) {
        ticketTypeService.delete(id);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }
}