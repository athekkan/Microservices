package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentReponse;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener{

    private static final String FAILURE_MESSAGE_DELIMETER = ",";
    private final OrderPaymentSaga orderPaymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }

    @Override
    public void paymentCompleted(PaymentReponse paymentReponse) {
        OrderPaidEvent domainEvent = orderPaymentSaga.process(paymentReponse);
        log.info("Publishing OrderPaidEvent for order id: {} ", paymentReponse.getOrderId());
        domainEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentReponse paymentReponse) {
        orderPaymentSaga.rollback(paymentReponse);
        log.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentReponse.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMETER, paymentReponse.getFailureMessages()));
    }
}
