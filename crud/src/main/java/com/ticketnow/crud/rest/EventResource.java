package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.*;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.EventService;
import com.ticketnow.crud.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crud/event")
@ResponseStatus(HttpStatus.OK)
public class EventResource {
    private final EventService eventService;
    private final QueueService queueService;

    public EventResource(final EventService eventService, QueueService queueService) {
        this.eventService = eventService;
        this.queueService = queueService;
    }

    @GetMapping
    public APIResponse<List<EventDTO>> getAllEvents() {
        APIResponseBuilder<List<EventDTO>> builder = new APIResponseBuilder<>();
        return builder.data(eventService.findAll()).ok().build();
    }

    @GetMapping("/name")
    public List<TitleDTO> getEventNames(@RequestParam(name = "id") List<Integer> ids) {
        return eventService.getEventNames(ids);
    }

    @GetMapping("/{id}/organizer")
    public String getEventNames(@PathVariable(name = "id") Integer id) {
        return eventService.getEventName(id);
    }

    @GetMapping("/approved")
    public APIResponse<List<EventDTO>> getApprovedEvents() {
        APIResponseBuilder<List<EventDTO>> builder = new APIResponseBuilder<>();
        return builder.data(eventService.findAllApproved()).ok().build();
    }

    @GetMapping("/{id}")
    public APIResponse<EventDTO> getEvent(@PathVariable(name = "id") final int id) {
        APIResponseBuilder<EventDTO> builder = new APIResponseBuilder<>();
        return builder.data(eventService.get(id)).ok().build();
    }

    @GetMapping("/{id}/ticket-type")
    public APIResponse<List<TicketTypeDTO>> getTicketTypesByEvent(@PathVariable(name = "id") final int id) {
        APIResponseBuilder<List<TicketTypeDTO>> builder = new APIResponseBuilder<>();
        return builder.data(eventService.getTicketTypesByEvent(id)).ok().build();
    }

    @PostMapping
    public APIResponse<Integer> createEvent(@RequestBody @Valid final CreateEventDTO eventDTO) {
        final int createdId = eventService.createEvent(eventDTO);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(createdId).ok().build();
    }

    @PutMapping("/{id}/queue")
    public APIResponse<Integer> updateQueue(@PathVariable(name = "id") final int id,
                                            @RequestBody CreateQueueDTO dto) {
        eventService.updateQueue(id, dto);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(id).ok().build();
    }

    @GetMapping("/{id}/queue")
    public APIResponse<Integer> getEventQueuePosition(@PathVariable(name = "id") final int id) {
        Integer queuePosition = queueService.getEventQueuePosition(id);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(queuePosition).ok().build();
    }

    @PutMapping("/{id}")
    public APIResponse<Integer> updateEvent(@PathVariable(name = "id") final int id,
                                               @RequestBody @Valid final EventDTO eventDTO) {
        eventService.update(id, eventDTO);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(id).ok().build();
    }

    @PutMapping("/{id}/approval")
    public APIResponse<Integer> approveEvent(@PathVariable(name = "id") final int id) {
        eventService.approve(id);
        APIResponseBuilder<Integer> builder = new APIResponseBuilder<>();
        return builder.data(id).ok().build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteEvent(@PathVariable(name = "id") final int id) {
        eventService.delete(id);
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        return builder.ok().build();
    }
}
