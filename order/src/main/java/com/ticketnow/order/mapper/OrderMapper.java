package com.ticketnow.order.mapper;

import com.ticketnow.order.domain.Order;
import com.ticketnow.order.domain.OrderItem;
import com.ticketnow.order.dto.CreateOrderDTO;
import com.ticketnow.order.dto.OrderDTO;
import com.ticketnow.order.dto.OrderDetailDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
@Component
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "status",  expression = "java(order.getStatus().name())")
    OrderDTO toOrderDTO(Order order);

    OrderDetailDTO toOrderDetailDTO(Order order);

    OrderDetailDTO.OrderItemDto toOrderItemDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true) // Ignore ID when mapping to entity
    void updateOrderFromDto(CreateOrderDTO orderDTO, @MappingTarget Order order);

    @AfterMapping
    default void mapOrderItems(@MappingTarget OrderDetailDTO orderDetailDTO, Order order) {
        Set<OrderDetailDTO.OrderItemDto> orderItems = order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toSet());
        orderDetailDTO.setOrderItems(orderItems);
    }
}
