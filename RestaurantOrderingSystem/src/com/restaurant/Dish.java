package com.restaurant;

import java.io.Serializable;

/**
 * Represents a dish in the menu.
 */
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Category {
        APPETIZER,
        MAIN_COURSE,
        DESSERT,
        BEVERAGE
    }

    private final String name;
    private final double price;
    private final Category category;

    /**
     * Constructor for creating a Dish.
     * 
     * @param name     The name of the dish.
     * @param price    The price of the dish.
     * @param category The category of the dish.
     */
    public Dish(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String name() {
        return name;
    }

    public double price() {
        return price;
    }

    public Category category() {
        return category;
    }
}
