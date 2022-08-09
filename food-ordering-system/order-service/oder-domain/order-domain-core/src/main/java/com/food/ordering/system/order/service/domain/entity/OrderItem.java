package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.sytem.domain.valueobject.Money;
import com.food.ordering.sytem.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.valueobject.OderItemId;
import com.food.ordering.sytem.domain.entity.BaseEntity;

public class OrderItem extends BaseEntity<OderItemId> {
    private OrderId oderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    //need to create a builder object using plugin

    public void initializeOrderItem(OrderId id, OderItemId orderItemId) {
        this.oderId = id;
        super.setId(orderItemId);
    }

    public OrderItem(Product product, int quantity, Money price, Money subTotal) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    public OrderId getOderId() {
        return oderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }
}
