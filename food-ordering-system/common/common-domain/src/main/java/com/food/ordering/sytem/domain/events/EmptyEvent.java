package com.food.ordering.sytem.domain.events;

public final class EmptyEvent implements DomainEvents<Void> {

    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent(){

    }


}
