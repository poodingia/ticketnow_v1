package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Event;
import com.ticketnow.crud.domain.TicketType;
import com.ticketnow.crud.dto.TicketTypeValidDTO;
import com.ticketnow.crud.dto.TicketTypeDTO;
import com.ticketnow.crud.dto.TitleDTO;
import com.ticketnow.crud.exception.NotFoundException;
import com.ticketnow.crud.repos.EventRepository;
import com.ticketnow.crud.repos.TicketTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class TicketTypeService {
    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final JdbcTemplate jdbcTemplate;


    public TicketTypeService(final TicketTypeRepository ticketTypeRepository, EventRepository eventRepository1, JdbcTemplate jdbcTemplate) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventRepository = eventRepository1;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TicketTypeDTO> findAll() {
        final List<TicketType> ticketTypes = ticketTypeRepository.findAll(Sort.by("id"));
        return ticketTypes.stream().map(this::mapToDTO).toList();
    }

    public TicketTypeDTO get(final Integer id) {
        return ticketTypeRepository.findById(id).map(this::mapToDTO).orElseThrow(NotFoundException::new);
    }

    public Integer create(final TicketTypeDTO ticketTypeDTO) {
        final TicketType ticketType = new TicketType();
        mapToEntity(ticketTypeDTO, ticketType);
        return ticketTypeRepository.save(ticketType).getId();
    }

    public void update(final Integer id, final TicketTypeDTO ticketTypeDTO) {
        final TicketType ticketType = ticketTypeRepository.findById(id).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(ticketTypeDTO.eventId()).orElseThrow(NotFoundException::new);
        TicketType newTicketType = mapToEntity(ticketTypeDTO, ticketType);
        event.setApproved(false);
        ticketTypeRepository.save(newTicketType);
    }

    public void delete(final Integer id) {
        ticketTypeRepository.deleteById(id);
    }

    public void reserve(Integer id, int quantity) {
        TicketType ticketType = ticketTypeRepository.findById(id).orElseThrow(NotFoundException::new);
        ticketType.setReservedQuantity(ticketType.getReservedQuantity() + quantity);
        ticketTypeRepository.save(ticketType);
    }

    public List<Integer> updateTicketTypeList(List<TicketTypeDTO> ticketTypeDTO) {
        for (TicketTypeDTO ticketType : ticketTypeDTO) {
            if (ticketType.id() != null &&  ticketTypeRepository.existsById(ticketType.id())) {
                update(ticketType.id(), ticketType);
            } else {
                create(ticketType);
            }
        }
        return null;
    }

    public Boolean validateTicketTypes(List<TicketTypeValidDTO> ticketTypeDTOs) {
        for (TicketTypeValidDTO ticketTypeDTO : ticketTypeDTOs) {
            TicketType ticketType = ticketTypeRepository.findById(ticketTypeDTO.typeId()).orElseThrow(NotFoundException::new);
            if (!Objects.equals(ticketTypeDTO.price(), ticketType.getPrice())) {
                return false;
            }
        }
        return true;
    }

    private TicketTypeDTO mapToDTO(final TicketType ticketType) {
        return new TicketTypeDTO(ticketType.getId(), ticketType.getDescription(), ticketType.getPrice(), ticketType.getQuantity(), ticketType.getReservedQuantity(), ticketType.getEvent().getId());
    }

    private TicketType mapToEntity(final TicketTypeDTO ticketTypeDTO, final TicketType ticketType) {
        ticketType.setDescription(ticketTypeDTO.description());
        ticketType.setPrice(ticketTypeDTO.price());
        ticketType.setQuantity(ticketTypeDTO.quantity());
        ticketType.setEvent(eventRepository.getReferenceById(ticketTypeDTO.eventId()));

        return ticketType;
    }

    @Transactional
    void updateTicketTypes(Map<Integer, Integer> ticketMap) {
        if (ticketMap == null || ticketMap.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "UPDATE ticket_types SET reserved_quantity = reserved_quantity + ? WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        Map.Entry<Integer, Integer> entry = getEntryAtIndex(ticketMap, i);
                        ps.setInt(1, entry.getValue());
                        ps.setInt(2, entry.getKey());
                    }

                    @Override
                    public int getBatchSize() {
                        return ticketMap.size();
                    }
                });

    }

    private Map.Entry<Integer, Integer> getEntryAtIndex(Map<Integer, Integer> map, int index) {
        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    public List<TitleDTO> getTypeNames(List<Integer> ids) {
        return ticketTypeRepository.getTitles(ids);
    }
}
