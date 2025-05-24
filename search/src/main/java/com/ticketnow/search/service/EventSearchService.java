package com.ticketnow.search.service;

import com.ticketnow.search.repo.EventSearchRepository;
import com.ticketnow.search.domain.EventSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventSearchService {
    private final EventSearchRepository eventSearchRepository;

    public EventSearchService(EventSearchRepository eventSearchRepository) {
        this.eventSearchRepository = eventSearchRepository;
    }

    public List<EventSearch> searchEvents(String title) {
        return eventSearchRepository.searchAllFields(title);
    }

    public List<EventSearch> searchEventsByCategory(String category) {
        return eventSearchRepository.findAllByCategoryAndApproved(category, true);
    }

    public List<EventSearch> searchSimilarEvents(String title) {
        return eventSearchRepository.searchSimilarEvents(title, Pageable.ofSize(5));
    }
}
