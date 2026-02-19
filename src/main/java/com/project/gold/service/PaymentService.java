package com.project.gold.service;

import com.project.gold.client.PaymentGatewayClient;
import com.project.gold.dto.PaymentRequest;
import com.project.gold.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentGatewayClient paymentGatewayClient;

    public PaymentResponse processPayment(String userId, Double amount) {
        log.info("Processing payment for userId: {}, amount: ₹{}", userId, amount);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        paymentRequest.setAmount(amount);
        paymentRequest.setCurrency("INR");

        PaymentResponse paymentResponse = paymentGatewayClient.initiatePayment(paymentRequest);

        if (paymentResponse == null) {
            log.error("Payment response is null from gateway");
            return new PaymentResponse(
                    "FAILED",
                    null,
                    "Payment response is null"
            );
        }

        log.info("Payment processed. Status: {}, PaymentId: {}",
                paymentResponse.getStatus(), paymentResponse.getPaymentId());

        return paymentResponse;
    }
}
