package com.ticketnow.search.rest;

import com.ticketnow.search.domain.EventSearch;
import com.ticketnow.search.service.EventSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search/event")
public class EventSearchResource {
    private final EventSearchService eventSearchService;

    public EventSearchResource(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    @GetMapping
    public List<EventSearch> searchEvents(@RequestParam String title, @RequestParam String category) {
        if (category != null && !category.isEmpty()) {
            return eventSearchService.searchEventsByCategory(category);
        }
        return eventSearchService.searchEvents(title);
    }

    @GetMapping("/similar")
    public List<EventSearch> searchSimilarEvents(@RequestParam String title) {
        return eventSearchService.searchSimilarEvents(title);
    }
}
