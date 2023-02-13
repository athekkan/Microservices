package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentReponse;

public interface PaymentResponseMessageListener {
    public void paymentCompleted(PaymentReponse paymentReponse);
    public void paymentCancelled(PaymentReponse paymentReponse);
}
