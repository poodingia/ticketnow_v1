package com.ticketnow.order.service;

import com.ticketnow.order.dto.CreateOrderDTO;
import com.ticketnow.order.dto.TicketTypeValidDTO;
import com.ticketnow.order.dto.TitleDTO;
import com.ticketnow.order.utils.AuthenticationUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CrudService {
    private final RestClient restClient;
    private final RedisTemplate<String, String> redisTemplate;


    public CrudService(RestClient restClient, RedisTemplate<String, String> redisTemplate) {
        this.restClient = restClient;
        this.redisTemplate = redisTemplate;
    }

    public Boolean validateTicketTypes(List<TicketTypeValidDTO> ticketAdjustments) {

        final String jwt = AuthenticationUtils.extractJwt();

        final URI url = URI.create("http://crud/api/crud/ticket-type/validate");

        return restClient.put()
                .uri(url)
                .headers(h -> h.setBearerAuth(jwt))
                .body(ticketAdjustments)
                .retrieve()
                .toEntity(Boolean.class)
                .getBody();
    }

    public List<TitleDTO> getEventsNames(List<Integer> ids) {

        final URI url = URI.create("http://crud/api/crud/event/name?" + String.join("&", ids.stream().map(id -> "id="+id ).toList()));

        return restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TitleDTO>>() {})
                .getBody();
    }

    public List<TitleDTO> getTypeNames(List<Integer> ids) {

        final URI url = URI.create("http://crud/api/crud/ticket-type/name?" + String.join("&", ids.stream().map(id -> "id="+id ).toList()));

        return restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TitleDTO>>() {})
                .getBody();
    }

    public String getEventName(int id) {
        final String jwt = AuthenticationUtils.extractJwt();

        final URI url = URI.create("http://crud/api/crud/event/" + id + "/organizer");
        return restClient.get()
                .uri(url)
                .headers(h -> h.setBearerAuth(jwt))
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }

    public boolean checkTicketTypesAvailability(int type, int quantity) {
        String key = "ticket_left:" + type;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return true;
        }
        return Integer.parseInt(value) >= quantity;
    }

    public boolean checkTicketTypesEvent(int eventId, int typeId) {
        String key = "event_ticket_types:" + eventId;
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, typeId));
    }

    @Deprecated
    public boolean validateOrderItems(Set<CreateOrderDTO.CreateOrderItemDto> orderItems, int eventId) {
        List<TicketTypeValidDTO> ticketAdjustments = new ArrayList<>();
        for (CreateOrderDTO.CreateOrderItemDto orderItem : orderItems) {
            if (!checkTicketTypesAvailability(orderItem.ticketId(), orderItem.quantity())
                    || !checkTicketTypesEvent(eventId, orderItem.ticketId())) {
                return false;
            }
            if (orderItem.quantity() > 0) {
                ticketAdjustments.add(new TicketTypeValidDTO(orderItem.ticketId(),
                        orderItem.price().floatValue() / orderItem.quantity()));
            }
        }
        return validateTicketTypes(ticketAdjustments);
    }

    @Deprecated
    public boolean redisCheckTicketPrice(List<TicketTypeValidDTO> ticketAdjustments) {
        for (TicketTypeValidDTO ticketAdjustment : ticketAdjustments) {
            String key = "price:" + ticketAdjustment.typeId();
            String redisValue = redisTemplate.opsForValue().get(key);
            if (redisValue == null || Float.parseFloat(redisValue) != ticketAdjustment.price()) {
                return false;
            }
        }

        return true;
    }
}