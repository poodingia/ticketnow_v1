package com.ticketnow.order.service;

import com.ticketnow.order.domain.Status;
import com.ticketnow.order.dto.StatisticDTO;
import com.ticketnow.order.exception.CustomOrderException;
import com.ticketnow.order.repos.OrderRepository;
import com.ticketnow.order.utils.AuthenticationUtils;
import org.springframework.stereotype.Service;

import static com.ticketnow.order.exception.ErrorMessage.UNAUTHORIZED;

@Service
public class StatisticService {
    private final OrderRepository orderRepository;
    private final CrudService crudService;

    public StatisticService(OrderRepository orderRepository, CrudService crudService) {
        this.orderRepository = orderRepository;
        this.crudService = crudService;
    }

    public StatisticDTO getCustomerStatistic() {
        String userId = AuthenticationUtils.extractUserId();
        return orderRepository.findCustomerStatistic(userId, Status.CONFIRMED);
    }

    public StatisticDTO getOrganizerStatistic(Integer eventId) {
        if (crudService.getEventName(eventId) == null) {
            throw new CustomOrderException(UNAUTHORIZED);
        }
        return orderRepository.findOrganizerStatistic(eventId, Status.CONFIRMED);
    }
}
