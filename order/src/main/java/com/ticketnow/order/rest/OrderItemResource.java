package com.ticketnow.order.rest;


import com.ticketnow.order.dto.OrderItemDTO;
import com.ticketnow.order.rest.response.APIResponse;
import com.ticketnow.order.rest.response.APIResponseBuilder;
import com.ticketnow.order.service.OrderItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/order/{id}/item")
public class OrderItemResource {
    private final OrderItemService orderItemService;

    public OrderItemResource(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping()
    public APIResponse<Set<OrderItemDTO>> getAllOrderItems(@PathVariable(name = "id") int id) {
        Set<OrderItemDTO> orderItemDTOS = orderItemService.findAllByOrderId(id);
        APIResponseBuilder<Set<OrderItemDTO>> builder = new APIResponseBuilder<>();
        return builder.data(orderItemDTOS).ok().build();
    }
}
