package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.EventOrganizerDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crud/organizer")
@ResponseStatus(HttpStatus.OK)
public class OrganizerResource {
    private final EventService eventService;

    public OrganizerResource(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public APIResponse<List<EventOrganizerDTO>> getAllEvents() {
        APIResponseBuilder<List<EventOrganizerDTO>> builder = new APIResponseBuilder<>();
        return builder.data(eventService.getEventsByOrganizer()).ok().build();
    }
}
