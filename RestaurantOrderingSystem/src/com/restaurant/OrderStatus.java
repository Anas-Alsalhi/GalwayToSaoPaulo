package com.restaurant;

public enum OrderStatus {
    PLACED("Order has been placed"),
    IN_PROGRESS("Order is being prepared"),
    SERVED("Order has been served");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + " - " + description;
    }
}