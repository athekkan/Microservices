package com.food.ordering.sytem.domain.events.publisher;

import com.food.ordering.sytem.domain.events.DomainEvents;

public interface DomainEventPublisher<T extends DomainEvents> {

    void publish(T domainEvent);
}
