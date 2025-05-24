package com.ticketnow.crud.service;

import com.ticketnow.crud.dto.EmailDTO;
import com.ticketnow.crud.dto.EventSearchDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerService {
    private static final String TOPIC_PAYMENT_SUCCESS = "payment_success_from_crud";
    private static final String TOPIC_EVENT_SEARCH_CHANGE = "update_event_search";
    private static final String TOPIC_EVENT_SEARCH_DELETE = "delete_event_search";
    private static final String TOPIC_EVENT_CHANGE = "event_change_from_crud";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate, KafkaTemplate<String, String> stringKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.stringKafkaTemplate = stringKafkaTemplate;
    }

    public void producePaymentSuccessMessage(EmailDTO emailDTO) {
        kafkaTemplate.send(TOPIC_PAYMENT_SUCCESS, emailDTO);
    }

    public void produceEventSearchChange(EventSearchDTO eventSearchDTO) {
        kafkaTemplate.send(TOPIC_EVENT_SEARCH_CHANGE, eventSearchDTO);
    }

    public void produceEventSearchDelete(String id) {
        stringKafkaTemplate.send(TOPIC_EVENT_SEARCH_DELETE, id);
    }

    public void produceEventChange(Integer eventId, String eventTitle) {
        String message = eventId.toString().concat(":").concat(eventTitle);
        stringKafkaTemplate.send(TOPIC_EVENT_CHANGE, message);
    }
}
