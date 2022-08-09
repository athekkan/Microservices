package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.order.service.domain.valueobject.OderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import com.food.ordering.sytem.domain.DomainException;
import com.food.ordering.sytem.domain.entity.AggregateRoot;
import com.food.ordering.sytem.domain.valueobject.*;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }
    private void initializeOrderItems() {
        long itemId = 1;
        for(OrderItem item : orderItems){
            item.initializeOrderItem(super.getId(),new OderItemId(itemId++));
        }
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateTotalPrice() {
    }

    private void validateItemsPrice() {
        
    }

    private void validateInitialOrder() {

    }

    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new DomainException("Order state is not correct for performing pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }
    public void approve(){
        if(orderStatus != OrderStatus.PAID){
            throw new DomainException("Order state is not correct for performing approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }
    public void initCancel(){
        if(orderStatus != OrderStatus.PAID){
            throw new DomainException("Order state is not correct for performing cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
    }
    public void cancel(List<String> failureMessages){
        if(orderStatus != OrderStatus.CANCELLING || orderStatus != OrderStatus.PENDING ){
            throw new DomainException("Order state is not correct for performing cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null){
            this.failureMessages.addAll(failureMessages);
        }
    }

    private Order(Builder builder) {
        super.setId(builder.oderId);
        this.customerId = builder.customerId;
        this.restaurantId = builder.restaurantId;
        this.deliveryAddress = builder.deliveryAddress;
        this.price = builder.price;
        this.orderItems = builder.orderItems;
        this.trackingId = builder.trackingId;
        this.orderStatus = builder.orderStatus;
        this.failureMessages = builder.failureMessages;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private OrderId oderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder(){

        }

        public Builder oderId(OrderId val){
            oderId = val;
            return this;
        }
        public Builder customerId(CustomerId val){
            customerId = val;
            return this;
        }
        public Builder restaurantId(RestaurantId val){
            restaurantId = val;
            return this;
        }
        public Builder deliveryAddress(StreetAddress val){
            deliveryAddress = val;
            return this;
        }
        public Builder price(Money val){
            price = val;
            return this;
        }
        public Builder oderItems(List<OrderItem> val){
            orderItems = val;
            return this;
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getOderItems() {
        return orderItems;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
