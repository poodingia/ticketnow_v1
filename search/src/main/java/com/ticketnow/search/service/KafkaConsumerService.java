package com.ticketnow.search.service;

import com.ticketnow.search.domain.EventSearch;
import com.ticketnow.search.dto.EventSearchDTO;
import com.ticketnow.search.repo.EventSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final EventSearchRepository eventSearchRepository;

    public KafkaConsumerService(EventSearchRepository eventSearchRepository) {
        this.eventSearchRepository = eventSearchRepository;
    }

    @KafkaListener(topics = "update_event_search", groupId = "search", containerFactory = "eventSearchKafkaListenerContainerFactory")
    public void update(@Payload EventSearchDTO eventSearchDTO) {
        EventSearch eventSearch = eventSearchRepository.findByEventId(eventSearchDTO.id()).orElse(new EventSearch());
        mapEventSearchDTOtoEntity(eventSearchDTO, eventSearch);
    }

    @KafkaListener(topics = "delete_event_search", groupId = "search", containerFactory = "stringKafkaListenerContainerFactory")
    public void update(@Payload String id) {
        EventSearch eventSearch = eventSearchRepository.findByEventId(Long.valueOf((id))).orElse(new EventSearch());
        eventSearchRepository.delete(eventSearch);
    }

    private void mapEventSearchDTOtoEntity(@Payload EventSearchDTO eventSearchDTO, EventSearch eventSearch) {
        eventSearch.setId(eventSearchDTO.id());
        eventSearch.setTitle(eventSearchDTO.title());
        eventSearch.setBgImagePath(eventSearchDTO.bgImagePath());
        eventSearch.setDescription(eventSearchDTO.description());
        eventSearch.setDate(eventSearchDTO.date());
        eventSearch.setCategory(eventSearchDTO.category());
        eventSearch.setLocation(eventSearchDTO.location());
        eventSearch.setApproved(eventSearchDTO.approved());
        eventSearchRepository.save(eventSearch);
    }

}
