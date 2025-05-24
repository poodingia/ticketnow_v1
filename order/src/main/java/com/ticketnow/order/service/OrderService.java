package com.ticketnow.order.service;

import com.ticketnow.order.domain.EmailStatus;
import com.ticketnow.order.domain.Order;
import com.ticketnow.order.domain.OrderItem;
import com.ticketnow.order.domain.Status;
import com.ticketnow.order.dto.*;
import com.ticketnow.order.exception.CustomOrderException;
import com.ticketnow.order.exception.ErrorMessage;
import com.ticketnow.order.exception.NotFoundException;
import com.ticketnow.order.mapper.OrderMapper;
import com.ticketnow.order.repos.OrderRepository;
import com.ticketnow.order.utils.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final CrudService crudService;
    private final SessionService sessionService;
    private final PaymentProducerService paymentProducerService;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final OrderItemService orderItemService;

    public OrderService(final OrderRepository orderRepository, CrudService crudService, SessionService sessionService, PaymentProducerService paymentProducerService, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.crudService = crudService;
        this.sessionService = sessionService;
        this.paymentProducerService = paymentProducerService;
        this.orderItemService = orderItemService;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAllByUserIdAndStatus(AuthenticationUtils.extractUserId(), Status.CONFIRMED);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Integer, String> eventNames = crudService.getEventsNames(orders.stream().map(Order::getEventId).toList())
                .stream().collect(Collectors.toMap(TitleDTO::id, TitleDTO::title));
        return orders.stream()
                .map(order -> {
                    String eventTitle = eventNames.getOrDefault(order.getEventId(), "Unknown");
                    order.setEventTitle(eventTitle);
                    return orderMapper.toOrderDTO(order);
                })
                .toList();
    }

    public List<OrderDTO> findAllByEventId(Integer eventId) {
        final List<Order> orders = orderRepository.findAllByEventIdAndStatus(eventId, Status.CONFIRMED);
        return orders.stream()
                .map(order -> {
                    String eventTitle = crudService.getEventName(eventId);
                    if (eventTitle == null) {
                        throw new CustomOrderException(ErrorMessage.UNAUTHORIZED);
                    }
                    order.setEventTitle(eventTitle);
                    return orderMapper.toOrderDTO(order);
                })
                .toList();
    }

    public OrderDetailDTO getOne(final Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return orderMapper.toOrderDetailDTO(order);
    }

    @Transactional
    public Integer createOrder(final CreateOrderDTO orderDTO, final String uuid) {
        String sessionId = AuthenticationUtils.extractSession();
        if (!sessionService.checkValidSession(sessionId, orderDTO.eventId())) {
            throw new CustomOrderException(ErrorMessage.SESSION_INVALID);
        }

        List<TicketTypeValidDTO> ticketAdjustments = new ArrayList<>();
        List<CreateOrderDTO.CreateOrderItemDto> validItems = new ArrayList<>();

        for (CreateOrderDTO.CreateOrderItemDto item : orderDTO.orderItems()) {
            if (item.quantity() > 0) {
                validItems.add(item);
                ticketAdjustments.add(new TicketTypeValidDTO(
                        item.ticketId(),
                        item.price().floatValue() / item.quantity())
                );
            }
        }

        if (!crudService.validateTicketTypes(ticketAdjustments)) {
            throw new CustomOrderException(ErrorMessage.TICKET_TYPE_INVALID);
        }

        final Order order = new Order();
        orderMapper.updateOrderFromDto(orderDTO, order);
        order.setPaymentRef(uuid);
        order.setStatus(Status.PENDING);
        order.setEmailStatus(EmailStatus.NOT_SENT);
        order.setUserId(AuthenticationUtils.extractUserId());
        order.setEventId(orderDTO.eventId());

        int id = orderRepository.saveAndFlush(order).getId();

        orderItemService.createOrderItems(id, validItems);
        sessionService.deleteSession(sessionId, orderDTO.eventId());
        return id;
    }



    public void confirmOrder(final Order order) {
        Set<OrderItem> orderItems = order.getOrderItems();
        if (orderItems.isEmpty()) {
            return;
        }

        Map<Integer, Integer> ticketAdjustments = new HashMap<>();
        boolean type = orderItems.iterator().next().getType();

        for (OrderItem orderItem : orderItems) {
            ticketAdjustments.compute(orderItem.getTicketId(),
                    (key, value) -> (value == null || type) ? orderItem.getQuantity() : value + orderItem.getQuantity());
        }
        paymentProducerService.sendUpdateTicketMessage(ticketAdjustments, order.getPrice(), order.getEventId());
    }


    public void delete(final Integer id) {
        orderRepository.deleteById(id);
    }

    public void updatePaymentStatus(final String paymentStatus, final String paymentRef) {
        final Order order = orderRepository.findByPaymentRef(paymentRef);
        if(order != null && order.getStatus().equals(Status.PENDING)) {
            order.setStatus(Status.valueOf(paymentStatus));
            orderRepository.save(order);
            if (paymentStatus.equals("CONFIRMED")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String timestamp = LocalDateTime.now().format(formatter);
                paymentProducerService.sendPaymentSuccessMessage(paymentRef, timestamp);
                confirmOrder(order);
                String couponCode = order.getCouponCode();
                if (couponCode != null && !couponCode.isEmpty()) {
                    paymentProducerService.sendUpdateCouponMessage(couponCode);
                }
            }
        }
    }

    public double validateOrderPrice(CreateOrderDTO orderDTO) {
        if (orderDTO.orderItems() == null || orderDTO.orderItems().isEmpty()) {
            throw new CustomOrderException(ErrorMessage.INVALID_INPUT_DATA);
        }
        double calculatedTotal = orderDTO.orderItems().stream()
                .mapToDouble(CreateOrderDTO.CreateOrderItemDto::price)
                .sum();
        if (orderDTO.couponCode() != null && !orderDTO.couponCode().isEmpty()) {
            return orderDTO.price();
        }
        if (calculatedTotal != orderDTO.price()) {
            log.error("Invalid order price: {} != {}", calculatedTotal, orderDTO.price());
            throw new CustomOrderException(ErrorMessage.INVALID_INPUT_DATA);
        }
        return calculatedTotal;
    }

}
