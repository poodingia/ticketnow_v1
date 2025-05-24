package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.domain.TicketType;
import com.ticketnow.crud.dto.*;
import com.ticketnow.crud.exception.CustomCrudException;
import com.ticketnow.crud.exception.ErrorMessage;
import com.ticketnow.crud.exception.NotFoundException;
import com.ticketnow.crud.mapper.EventMapper;
import com.ticketnow.crud.repos.CityRepository;
import com.ticketnow.crud.repos.CouponRepository;
import com.ticketnow.crud.repos.EventRepository;
import com.ticketnow.crud.repos.TicketTypeRepository;
import com.ticketnow.crud.utils.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService {
    private final StringRedisTemplate redisTemplate;
    private final EventRepository eventRepository;
    private final CityRepository cityRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final ProducerService producerService;
    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private final CouponRepository couponRepository;

    public EventService(StringRedisTemplate redisTemplate, EventRepository eventRepository, CityRepository cityRepository, TicketTypeRepository ticketTypeRepository, ProducerService producerService, CouponRepository couponRepository) {
        this.redisTemplate = redisTemplate;
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.producerService = producerService;
        this.couponRepository = couponRepository;
    }

    public List<EventDTO> findAll() {
        return eventRepository.findAll(Sort.by("id"))
                .stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    public EventDTO get(final int id) {
        return eventRepository.findById(id)
                        .map(eventMapper::toDTO)
                        .orElseThrow(NotFoundException::new);

    }

    public List<EventOrganizerDTO> getEventsByOrganizer() {
        final String userId = AuthenticationUtils.extractUserId();
        return eventRepository.findALlEventDTOsByOrganizerId(userId);
    }

    public int createEvent(CreateEventDTO createEventDTO) {
        if (eventNotValid(createEventDTO.event())) {
            throw new CustomCrudException(ErrorMessage.INVALID_INPUT_DATA);
        }
        Event event = new Event();
        eventMapper.partialUpdate(createEventDTO.event(), event);
        event.setCity(cityRepository.getReferenceById(createEventDTO.event().cityId()));
        event.setUserId(AuthenticationUtils.extractUserId());
        event.setRevenue(0.0f);
        event.setApproved(false);
        event = eventRepository.save(event);

        Set<CreateEventDTO.TicketTypeDto> ticketTypeDTOs = createEventDTO.ticketTypes();
        List<TicketType> ticketTypeList = new ArrayList<>(ticketTypeDTOs.size());

        for (CreateEventDTO.TicketTypeDto ticketTypeDto : ticketTypeDTOs) {
            TicketType ticketType = new TicketType();
            ticketType.setDescription(ticketTypeDto.description());
            ticketType.setPrice(ticketTypeDto.price());
            ticketType.setQuantity(ticketTypeDto.quantity());
            ticketType.setReservedQuantity(0);
            ticketType.setEvent(event);
            ticketType.setBoughtDirectly(ticketTypeDto.directly());
            ticketTypeList.add(ticketType);
        }

        ticketTypeRepository.saveAll(ticketTypeList);
        return event.getId();
    }

    public void updateQueue(final int id, CreateQueueDTO queueDTO) {
        final Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        event.setBookingCapacity(queueDTO.number());
        redisTemplate.opsForValue().set("event:capacity:" + id, String.valueOf(queueDTO.number()));
        eventRepository.save(event);
    }

    public void approve(final int id) {
        final Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        if (event.getApproved()) {
            return;
        }
        event.setApproved(true);
        EventSearchDTO eventSearchDTO = new EventSearchDTO(event.getId().longValue(), event.getTitle(), event.getBgImagePath(),
                event.getDescription(), event.getDate().toString(), event.getCategory().name(), event.getCity().getName(), true);
        producerService.produceEventSearchChange(eventSearchDTO);
        producerService.produceEventChange(event.getId(), event.getTitle());
        eventRepository.save(event);
    }

    @Transactional
    public void update(final int id, final EventDTO eventDTO) {
        if (eventNotValid(eventDTO)) {
            throw new CustomCrudException(ErrorMessage.INVALID_INPUT_DATA);
        }
        final Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        eventMapper.partialUpdate(eventDTO, event);
        event.setCity(cityRepository.getReferenceById(eventDTO.cityId()));
        event.setApproved(false);
    }

    public void delete(final int id) {
        couponRepository.deleteAll(couponRepository.findAllByEventId(id));
        ticketTypeRepository.deleteAll(ticketTypeRepository.findByEventId(id));
        eventRepository.deleteById(id);
        producerService.produceEventSearchDelete(String.valueOf(id));
    }

    public List<TicketTypeDTO> getTicketTypesByEvent(int id) {
        return ticketTypeRepository.findByEventId(id).stream().map(ticketType ->
                new TicketTypeDTO(ticketType.getId(), ticketType.getDescription(), ticketType.getPrice(),
                        ticketType.getQuantity(),id, ticketType.getReservedQuantity())).toList();
    }

    public boolean eventNotValid(EventDTO eventDTO) {
        return eventDTO.startSaleDate().isAfter(eventDTO.endSaleDate()) ||
                eventDTO.date().isBefore(eventDTO.endSaleDate());
    }

    public List<EventDTO> findAllApproved() {
        return eventRepository.findAllByApproved((true))
                .stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    public List<TitleDTO> getEventNames(List<Integer> ids) {
        return eventRepository.findAllEventNames(ids);
    }

    public String getEventName(Integer id) {
        return eventRepository.findEventNameById(id, AuthenticationUtils.extractUserId());
    }

    public void updateRevenue(Integer eventId, Float revenue) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
        event.setRevenue(event.getRevenue() + revenue);
        eventRepository.save(event);
    }
}
