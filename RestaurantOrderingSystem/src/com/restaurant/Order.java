package com.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<Dish, Long> dishCounts = dishes.stream()
            .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
    
        String border = "+-----+------------------------------+------------+----------+";
        String titleRow = String.format("| %-58s |", "Order for Table " + table.getTableNumber());
        String header = String.format("| %-3s | %-28s | %10s | %8s |", "#", "Dish", "Price", "Quantity");
    
        // Print table
        System.out.println(border);
        System.out.println(titleRow);
        System.out.println(border);
        System.out.println(header);
        System.out.println(border);
    
        int index = 1;
        for (Map.Entry<Dish, Long> entry : dishCounts.entrySet()) {
            Dish dish = entry.getKey();
            long quantity = entry.getValue();
            System.out.printf("| %-3d | %-28s | %10s | %8d |%n", index++, dish.name(), String.format("€%.2f", dish.price()), quantity);
        }
    
        System.out.println(border);
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

        List<String> failedDishes = new ArrayList<>();
        Arrays.stream(parts, 3, parts.length)
              .map(Menu::findDishByName)
              .forEach(optionalDish -> optionalDish.ifPresentOrElse(
                  dish -> {
                      try {
                          order.addDish(dish);
                      } catch (InvalidOrderException e) {
                          failedDishes.add(dish.name());
                      }
                  },
                  () -> System.err.println("Dish not found in the menu.")
              ));

        if (!failedDishes.isEmpty()) {
            System.err.println("Failed to add the following dishes: " + String.join(", ", failedDishes));
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
