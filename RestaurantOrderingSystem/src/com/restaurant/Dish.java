package com.restaurant;

import java.io.Serializable;
import java.util.Objects;

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

    public boolean isVegetarian() {
        // Assuming vegetarian dishes are determined by their category
        return this.category == Category.APPETIZER || this.category == Category.DESSERT;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Dish dish = (Dish) obj;
        return Objects.equals(name, dish.name) &&
               Objects.equals(price, dish.price) &&
               category == dish.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }
}
