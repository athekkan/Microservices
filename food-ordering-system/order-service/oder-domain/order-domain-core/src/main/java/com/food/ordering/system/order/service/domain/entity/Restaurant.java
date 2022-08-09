package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.sytem.domain.entity.AggregateRoot;
import com.food.ordering.sytem.domain.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private boolean active;

    // implement builder design pattern
    public Restaurant(List<Product> products) {
        this.products = products;
    }
}
