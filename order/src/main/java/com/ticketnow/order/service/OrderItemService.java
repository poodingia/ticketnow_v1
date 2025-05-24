package com.ticketnow.order.service;

import com.ticketnow.order.domain.Order;
import com.ticketnow.order.domain.OrderItem;
import com.ticketnow.order.dto.CreateOrderDTO;
import com.ticketnow.order.dto.OrderItemDTO;
import com.ticketnow.order.dto.TitleDTO;
import com.ticketnow.order.repos.OrderItemRepository;
import com.ticketnow.order.repos.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CrudService crudService;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, CrudService crudService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.crudService = crudService;
    }

    public Set<OrderItemDTO> findAllByOrderId(int id) {
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(id);

        Map<Integer, String> titles = crudService.getTypeNames(orderItems.stream()
                .map(OrderItem::getTicketId)
                .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(TitleDTO::id, TitleDTO::title));

        return orderItems.stream()
                .map(orderItem -> {
                    String title = titles.getOrDefault(orderItem.getTicketId(), "Unknown");
                    orderItem.setTitle(title);
                    return this.mapEntityToDTO(orderItem);
                })
                .collect(Collectors.toSet());
    }

    public OrderItemDTO mapEntityToDTO(OrderItem orderItem){
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getTicketId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getOrder().getId(),
                orderItem.getType(),
                orderItem.getTitle()
        );
    }

    public void createOrderItems(int orderId, List<CreateOrderDTO.CreateOrderItemDto> orderItems) {
        Order order = orderRepository.getReferenceById(orderId);
        Set<OrderItem> items = new HashSet<>(orderItems.size());
        for (CreateOrderDTO.CreateOrderItemDto orderItem : orderItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setTicketId(orderItem.ticketId());
            item.setQuantity(orderItem.quantity());
            item.setPrice(orderItem.price());
            item.setType(orderItem.isType());
            items.add(item);
        }
        orderItemRepository.saveAll(items);
    }
}
