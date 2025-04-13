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

    // Update the isVegetarian method to exclude dishes with meat or fish unless explicitly marked vegetarian
    public boolean isVegetarian() {
        // Exclude specific non-vegetarian dishes by name or category
        if (this.name.equalsIgnoreCase("Fish Cake") || this.category == Category.MAIN_COURSE) {
            return false;
        }
        // Assume all other appetizers and desserts are vegetarian unless specified otherwise
        return this.category == Category.APPETIZER || this.category == Category.DESSERT;
    }

    // Add a method to provide a description for the dish
    public String getDescription() {
        return "A delicious " + name + " from the " + category.name().toLowerCase() + " category.";
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
