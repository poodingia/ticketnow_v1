package com.ticketnow.order.service;

import com.ticketnow.order.domain.EmailStatus;
import com.ticketnow.order.domain.Order;
import com.ticketnow.order.domain.OrderItem;
import com.ticketnow.order.dto.CouponKafkaDTO;
import com.ticketnow.order.dto.EmailDTO;
import com.ticketnow.order.dto.EmailItemDTO;
import com.ticketnow.order.repos.OrderItemRepository;
import com.ticketnow.order.repos.OrderRepository;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class PaymentProducerService {
    private static final String TOPIC = "payment_success_from_order";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public PaymentProducerService(KafkaTemplate<String, Object> kafkaTemplate, KafkaTemplate<String, String> stringKafkaTemplate,
                                  OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.stringKafkaTemplate = stringKafkaTemplate;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void sendPaymentSuccessMessage(final String ref, final String paymentDate) {
        final Order order = orderRepository.findByPaymentRef(ref);
        if (order.getEmailStatus() != null && order.getEmailStatus().equals(EmailStatus.SENT))
            return;
        order.setEmailStatus(EmailStatus.SENT);
        orderRepository.saveAndFlush(order);
        final List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId()).stream().toList();
        final List<EmailItemDTO> orderEmailItemDTOs = orderItems.stream()
                .map(orderItem -> new EmailItemDTO(
                        orderItem.getTicketId(), orderItem.getQuantity(),
                        orderItem.getPrice(), orderItem.getType()
                        )
                )
                .toList();
        final EmailDTO orderEmailDTO = new EmailDTO(order.getPaymentRef(), orderEmailItemDTOs, paymentDate,
                order.getEmail(), order.getPaymentMethod(), order.getEventId());

        kafkaTemplate.send(TOPIC, orderEmailDTO);
    }

    public void sendUpdateCouponMessage(final String couponCode) {
        CouponKafkaDTO couponKafkaDTO = new CouponKafkaDTO();
        couponKafkaDTO.setCouponCode(couponCode);
        kafkaTemplate.send("update_coupon_from_order", couponKafkaDTO);
    }

    public void sendUpdateTicketMessage(Map<Integer, Integer> ticketMap, Double price, Integer id) {
        StringBuilder ticketStringBuilder = new StringBuilder();
        ticketStringBuilder.append(id);
        ticketStringBuilder.append(",");
        ticketMap.forEach((key, value) -> {
            if (ticketStringBuilder.charAt(ticketStringBuilder.length() - 1) != ',') {
                ticketStringBuilder.append(",");
            }
            ticketStringBuilder.append(key).append(":").append(value);
        });
        ticketStringBuilder.append(",");
        ticketStringBuilder.append(price);
        String message = ticketStringBuilder.toString();
        stringKafkaTemplate.send("update_ticket_from_order", message);
    }


}
