package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.sytem.domain.events.DomainEvents;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvents<Order> {
    private final Order order;
    private final ZonedDateTime createdTime;

    public OrderEvent(Order order, ZonedDateTime createdTime) {
        this.order = order;
        this.createdTime = createdTime;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }
}
