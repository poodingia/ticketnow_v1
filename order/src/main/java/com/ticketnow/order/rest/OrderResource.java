package com.ticketnow.order.rest;

import com.ticketnow.order.dto.CreateOrderDTO;
import com.ticketnow.order.dto.OrderDTO;
import com.ticketnow.order.dto.OrderDetailDTO;
import com.ticketnow.order.dto.OrderPaymentDTO;
import com.ticketnow.order.rest.response.APIResponse;
import com.ticketnow.order.rest.response.APIResponseBuilder;
import com.ticketnow.order.service.OrderService;
import com.ticketnow.order.service.PayosService;
import com.ticketnow.order.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderResource {
    private final OrderService orderService;
    private final VNPayService vnPayService;
    private final PayosService payosService;
    private final Logger logger = LoggerFactory.getLogger(OrderResource.class);

    public OrderResource(OrderService orderService, VNPayService vnPayService, PayosService payosService) {
        this.orderService = orderService;
        this.vnPayService = vnPayService;
        this.payosService = payosService;
    }

    @GetMapping
    public APIResponse<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAll();
        APIResponseBuilder<List<OrderDTO>> builder = new APIResponseBuilder<>();
        return builder.data(orders).ok().build();
    }

    @GetMapping("/organizer")
    public APIResponse<List<OrderDTO>> getAllOrders(@RequestParam(name = "eventId") final Integer eventId) {
        List<OrderDTO> orders = orderService.findAllByEventId(eventId);
        APIResponseBuilder<List<OrderDTO>> builder = new APIResponseBuilder<>();
        return builder.data(orders).ok().build();
    }

    @GetMapping("/{id}")
    public APIResponse<OrderDetailDTO> getOrder(@PathVariable(name = "id") final Integer id) {
        OrderDetailDTO order = orderService.getOne(id);
        APIResponseBuilder<OrderDetailDTO> builder = new APIResponseBuilder<>();
        return builder.data(order).ok().build();
    }

    @PostMapping
    public APIResponse<OrderPaymentDTO> createOrder(HttpServletRequest request, @RequestBody @Valid final CreateOrderDTO orderDTO) throws Exception {
        long uuid = System.currentTimeMillis() /1000;
        double price = orderService.validateOrderPrice(orderDTO);
        Integer orderId = orderService.createOrder(orderDTO, String.valueOf(uuid));
        String returnUrl;
        if (orderDTO.paymentMethod().equals("vnpay")) {
            String ip = this.vnPayService.getIpAddress(request);
            returnUrl= vnPayService.generateVNPayURL(price, String.valueOf(uuid), ip);
        } else {
            returnUrl = payosService.generatePaymentUrl(price, uuid);
        }
        logger.info("Payment URL: {} ", returnUrl);

        OrderPaymentDTO orderPaymentDTO = new OrderPaymentDTO(orderId, returnUrl);
        APIResponseBuilder<OrderPaymentDTO> builder = new APIResponseBuilder<>();
        return builder.data(orderPaymentDTO).ok().build();
    }

    @PostMapping("/payment/{code}/{ref}")
    public APIResponse<Void> updatePayment(
            @PathVariable("code") final String code,
            @PathVariable("ref") final String paymentRef) {
        String paymentStatus = code.equals("00") ? "CONFIRMED" : "CANCELLED";
        orderService.updatePaymentStatus(paymentStatus, paymentRef);
        return new APIResponseBuilder<Void>().ok().build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteOrder(@PathVariable(name = "id") final Integer id) {
        orderService.delete(id);
        return new APIResponseBuilder<Void>().ok().build();
    }
}
