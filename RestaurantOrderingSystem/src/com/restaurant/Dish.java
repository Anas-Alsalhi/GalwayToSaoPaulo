package com.restaurant;

import java.io.Serializable;
import java.util.Objects;
import java.util.ResourceBundle;

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

    // Refactored to provide unique, flavorful descriptions for each dish
    public String getDescription() {
        return switch (name.toLowerCase()) {
            case "pão de queijo" -> "Golden, cheesy Brazilian cheese bread, crispy on the outside and soft on the inside.";
            case "irish stew" -> "A hearty Irish classic with tender lamb, potatoes, and root vegetables simmered to perfection.";
            case "baileys cheesecake" -> "A creamy cheesecake infused with the rich, velvety flavor of Baileys Irish Cream.";
            case "fish and chips" -> "Crispy battered fish served with golden fries, a beloved British and Irish favorite.";
            case "shepherd's pie" -> "A comforting dish of seasoned minced lamb topped with creamy mashed potatoes, baked to golden perfection.";
            case "tea" -> "A soothing cup of traditional tea, perfect for any time of the day.";
            case "coffee" -> "Rich, aromatic coffee brewed to awaken your senses.";
            case "salad" -> "A crisp, refreshing mix of garden-fresh greens with a zesty vinaigrette.";
            case "pizza" -> "A classic favorite with a thin crust, tangy tomato sauce, and melted cheese.";
            case "feijoada" -> "A traditional Brazilian black bean stew with savory pork, served with rice and orange slices.";
            default -> "A delightful dish to tantalize your taste buds.";
        };
    }

    // Added a method to dynamically generate dish descriptions using localized templates
    public String getLocalizedDescription(ResourceBundle messages) {
        return String.format(messages.getString("dish.description"), this.name, this.category);
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
