package com.ticketnow.order.service;

import com.ticketnow.order.config.PayosPropertiesConfig;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;


@Service
public class PayosService {
    private final PayosPropertiesConfig payosPropertiesConfig;

    public PayosService(PayosPropertiesConfig payosPropertiesConfig) {
        this.payosPropertiesConfig = payosPropertiesConfig;
    }

    public String generatePaymentUrl(Double price, Long orderId) throws Exception {
        PayOS payOS = new PayOS(
                payosPropertiesConfig.clientId(),
                payosPropertiesConfig.apiKey(),
                payosPropertiesConfig.checksum()
        );

        String domain = "http://localhost:28080/payment";

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(orderId)
                .amount(price.intValue())
                .description("Thanh toán đơn hàng")
                .returnUrl(domain)
                .cancelUrl(domain)
                .build();

        CheckoutResponseData result = payOS.createPaymentLink(paymentData);
        return result.getCheckoutUrl();
    }

}
