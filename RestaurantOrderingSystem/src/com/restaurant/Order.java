package com.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
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
    private OrderStatus status = OrderStatus.PLACED;
    private final ResourceBundle messages;

    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    /**
     * Constructor for creating an Order.
     * 
     * @param table  The table associated with the order.
     * @param waiter The waiter serving the order.
     * @param messages The resource bundle for localized messages.
     */
    public Order(Table table, Waiter waiter, ResourceBundle messages) {
        this.table = table;
        this.waiter = waiter;
        this.messages = messages;
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

    // Removes the restriction that prevents adding the same dish multiple times.
    public void addDish(Dish dish) throws InvalidOrderException {
        if (dish == null) {
            throw new InvalidOrderException("Dish cannot be null.");
        }
        dishes.add(dish);
    }

    // Removes a dish from the order.
    // Displays a message if the dish is not found.
    public void removeDish(Dish dish) {
        if (dish == null || !dishes.contains(dish)) {
            System.out.println("Dish not found in the order.");
            return;
        }
        dishes.remove(dish);
        System.out.printf("Removed: %s (€%.2f)%n", dish.name(), dish.price());
    }

    // Clears all dishes from the order.
    public void clearOrder() {
        dishes.clear();
        System.out.println("Order has been cleared.");
    }

    // Sets the final price of the order after applying discounts.
    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    // Retrieves the final price of the order.
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
    
        String localizedTitle = messages.getString("order_for_table").replace("{tableNumber}", String.valueOf(table.getTableNumber()));
        String titleRow = String.format("| %-58s |", localizedTitle);
        String border = "+-----+------------------------------+------------+----------+";
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
     * @param menu The menu instance to use for finding dishes.
     * @return The parsed Order object.
     * @throws IllegalArgumentException If the string format is invalid.
     */
    public static Order parse(String orderString, Menu menu) {
        if (orderString == null || orderString.isBlank()) {
            throw new IllegalArgumentException("Order string cannot be null or empty.");
        }
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }

        String[] parts = orderString.split(",");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid order string format. Expected format: 'TableNumber,WaiterName,WaiterId,Dish1,Dish2,...'");
        }

        int tableNumber;
        int waiterId;
        try {
            tableNumber = Integer.parseInt(parts[0].trim());
            waiterId = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Table number and waiter ID must be valid integers.", e);
        }

        String waiterName = parts[1].trim();
        if (waiterName.isEmpty()) {
            throw new IllegalArgumentException("Waiter name cannot be empty.");
        }

        Table table = new Table(tableNumber, 4); // Replace with a lookup or configuration
        Waiter waiter = new Waiter(waiterName, waiterId);
        ResourceBundle messages = ResourceBundle.getBundle("com.restaurant.messages");
        Order order = new Order(table, waiter, messages);

        List<String> failedDishes = new ArrayList<>();
        Arrays.stream(parts, 3, parts.length)
              .map(String::trim)
              .filter(dishName -> !dishName.isEmpty())
              .map(menu::findDishByName)
              .forEach(optionalDish -> optionalDish.ifPresentOrElse(
                  dish -> {
                      try {
                          order.addDish(dish);
                      } catch (InvalidOrderException e) {
                          failedDishes.add(dish.name());
                      }
                  },
                  () -> failedDishes.add("Unknown Dish")
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
    public void printSummary() {
        System.out.println();
        System.out.println(messages.getString("start_selecting_menu"));
    }

    // Add method to generate dish descriptions
    public List<String> getDishDescriptions() {
        return dishes.stream()
            .map(dish -> String.format("%s: %s", dish.name(), dish.getDescription()))
            .collect(Collectors.toList());
    }
}
