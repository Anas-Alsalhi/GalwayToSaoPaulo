package com.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an order placed in the restaurant.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Table table;
    private final Waiter waiter;
    private final List<Dish> dishes = new ArrayList<>();
    private double finalPrice; // Final price after discount
    private double discountPercentage; // Discount applied

    /**
     * Constructor for creating an Order.
     * 
     * @param table  The table associated with the order.
     * @param waiter The waiter serving the order.
     */
    public Order(Table table, Waiter waiter) {
        this.table = table;
        this.waiter = waiter;
    }

    public Table getTable() {
        return table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }

    public void addDish(Dish dish) throws InvalidOrderException {
        if (dish == null) {
            throw new InvalidOrderException("Dish cannot be null.");
        }
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        if (dishes.contains(dish)) {
            dishes.remove(dish);
            System.out.printf("Removed: %s (€%.2f)%n", dish.name(), dish.price());
        } else {
            System.out.println("Dish not found in the order.");
        }
    }

    public void clearOrder() {
        dishes.clear();
        System.out.println("Order has been cleared.");
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void printDetails() {
        System.out.printf("Order for Table %d:%n", table.getTableNumber());
        for (Dish dish : dishes) {
            System.out.printf(" - %-25s (€%.2f)%n", dish.name(), dish.price());
        }
    }

    /**
     * Parses a string representation of an Order and returns an Order object.
     * Assumes the string is in the format: "TableNumber,WaiterName,WaiterId,Dish1,Dish2,..."
     *
     * @param orderString The string representation of the order.
     * @return The parsed Order object.
     * @throws IllegalArgumentException If the string format is invalid.
     */
    public static Order parse(String orderString) {
        String[] parts = orderString.split(",");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid order string format.");
        }

        int tableNumber = Integer.parseInt(parts[0]);
        String waiterName = parts[1];
        int waiterId = Integer.parseInt(parts[2]);

        Table table = new Table(tableNumber, 4); // Default capacity
        Waiter waiter = new Waiter(waiterName, waiterId);
        Order order = new Order(table, waiter);

        for (int i = 3; i < parts.length; i++) {
            String dishName = parts[i];
            // Assuming a method to find a dish by name exists in the Menu class
            Dish dish = Menu.findDishByName(dishName);
            if (dish != null) {
                try {
                    order.addDish(dish);
                } catch (InvalidOrderException e) {
                    System.err.println("Failed to add dish: " + e.getMessage());
                }
            }
        }

        return order;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return table.equals(order.table) && waiter.equals(order.waiter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, waiter);
    }
}
