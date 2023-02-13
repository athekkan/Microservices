package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentReponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.saga.SagaStep;
import com.food.ordering.sytem.domain.events.EmptyEvent;
import com.food.ordering.sytem.domain.valueobject.OrderId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentReponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    public OrderPaymentSaga(OrderDomainService orderDomainService,
                            OrderRepository orderRepository,
                            OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
    }

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentReponse paymentReponse) {
        log.info("Completing payment for order with id: {}", paymentReponse.getOrderId());
        Order order = findOrder(paymentReponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
        orderRepository.save(order);
        log.info("Order with id: {} is paid!", order.getId().getValue());
        return orderPaidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentReponse paymentReponse) {
        log.info("Cancelling order with id:{}",paymentReponse.getOrderId());
        Order order = findOrder(paymentReponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentReponse.getFailureMessages());
        orderRepository.save(order);
        log.info("Order with id: {} is cancelled!", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }


    private Order findOrder(String orderId) {
        Optional<Order> order = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (order.isEmpty()){
            log.error("Order not found for id: {}", orderId);
            //throw new OrderNotFoundException("Order not found for id:"+orderId);
        }
        return order.get();
    }
}
