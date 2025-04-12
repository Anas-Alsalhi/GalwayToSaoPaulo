package com.restaurant;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Manages the history of orders.
 */
public class OrderHistory {

    private final List<Order> orders = new CopyOnWriteArrayList<>(); // Thread-safe list

    // Adds a new order to the history.
    public void addOrder(Order order) {
        orders.add(order);
    }

    // Retrieves a copy of the order history to prevent external modifications.
    public List<Order> getOrders() {
        return new ArrayList<>(orders); // Return a copy to avoid external modification
    }

    // Displays the order history in the console.
    public void displayHistory() {
        if (orders.isEmpty()) {
            System.out.println("No orders in history.");
        } else {
            orders.stream().forEach(Order::printDetails);
        }
    }

    // Saves the order history to a binary file.
    public void saveToFile(Path filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(orders);
            System.out.println("Order history saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save order history: " + e.getMessage());
        }
    }

    // Loads the order history from a binary file.
    public void loadFromFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            @SuppressWarnings("unchecked")
            List<Order> loadedOrders = (List<Order>) ois.readObject();
            orders.clear();
            orders.addAll(loadedOrders);
            System.out.println("Order history loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load order history: " + e.getMessage());
        }
    }

    // Saves the order history as plain text for easier readability.
    public void saveAsText(Path filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Order order : orders) {
                writer.write(order.toString());
                writer.newLine();
            }
            System.out.println("Order history saved as text successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save order history as text: " + e.getMessage());
        }
    }

    // Loads the order history from a plain text file.
    public void loadFromText(Path filePath) {
        if (!Files.exists(filePath)) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            orders.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Menu menu = getMenu(); // Replace with the actual method or logic to retrieve the Menu object
                    orders.add(Order.parse(line, menu)); // Ensure Order.parse is implemented correctly
                } catch (IllegalArgumentException e) {
                    System.err.println("Failed to parse order: " + e.getMessage());
                }
            }
            System.out.println("Order history loaded from text successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load order history from text: " + e.getMessage());
        }
    }

    // Demonstrates path handling by resolving and normalizing paths.
    public void demonstratePathHandling(Path basePath, String relativePath) {
        if (relativePath.startsWith("-")) {
            System.out.println("Invalid relative path. Negative values are not allowed.");
            return;
        }
        Path resolvedPath = basePath.resolve(relativePath);
        Path normalizedPath = resolvedPath.normalize();
        Path relativizedPath = basePath.relativize(normalizedPath);

        System.out.println("Base Path: " + basePath);
        System.out.println("Relative Path: " + relativePath);
        System.out.println("Resolved Path: " + resolvedPath);
        System.out.println("Normalized Path: " + normalizedPath);
        System.out.println("Relativized Path: " + relativizedPath);
    }

    // Dynamically determines the date format based on the locale.
    private String getDateFormatForLocale(Locale locale) {
        if (locale.getLanguage().equals("es")) {
            return "dd-MM-yyyy"; // Spanish uses dashes
        } else if (locale.getLanguage().equals("ja")) {
            return "yyyy/MM/dd"; // Japanese uses slashes with year first
        }
        return "dd/MM/yyyy"; // Default format
    }

    // Displays a summary of an order using localized messages.
    public void displayOrderSummary(Order order, ResourceBundle messages, Locale locale) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println(" " + messages.getString("order_summary"));
        System.out.println("=".repeat(40));

        System.out.println(messages.getString("table_details"));
        System.out.printf("  %s: %d%n", messages.getString("table_number"), order.getTable().getTableNumber());
        System.out.printf("  %s: %d %s%n", messages.getString("capacity"), order.getTable().getCapacity(), messages.getString("customers"));
        System.out.printf("  %s: %d %s%n", messages.getString("seated_customers"), order.getTable().getCapacity(), messages.getString("customers"));
        System.out.printf("  %s: %s%n", messages.getString("waiter"), order.getWaiter().getName());

        System.out.println("\n" + messages.getString("ordered_items"));
        System.out.println("-".repeat(40));
        order.getDishes().forEach(dish ->
            System.out.printf("  - %-25s (€%.2f)%n", dish.name(), dish.price())
        );
        System.out.println("-".repeat(40));

        double subtotal = order.getDishes().stream().mapToDouble(Dish::price).sum();
        System.out.printf("  %s: €%.2f%n", messages.getString("subtotal"), subtotal);

        double discount = getValidDiscount(messages, new Scanner(System.in));
        if (discount < 0) return; // Exit if invalid input

        double discountedTotal = subtotal - (subtotal * (discount / 100));
        System.out.println();
        System.out.printf("  %s: €%.2f%n", messages.getString("total_after_discount"), discountedTotal);
        System.out.println();

        order.setDiscountPercentage(discount);
        order.setFinalPrice(discountedTotal);

        LocalDateTime now = LocalDateTime.now();
        String dateFormat = getDateFormatForLocale(locale);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat + " HH:mm", locale);
        System.out.printf("  %s: %s%n", messages.getString("order_timestamp"), now.format(formatter));
        System.out.println();

        System.out.println("=".repeat(40));
        System.out.println(messages.getString("all_dishes_prepared"));
        System.out.println(messages.getString("order_processed"));
        System.out.println(messages.getString("order_added_history"));
        System.out.println("=".repeat(40));
    }

    // Retrieves a valid discount percentage from the user.
    private double getValidDiscount(ResourceBundle messages, Scanner scanner) {
        double discount;
        while (true) {
            System.out.print(messages.getString("enter_discount"));
            String discountInput = scanner.nextLine().trim();
            if (discountInput.isEmpty()) {
                return 0; // No discount
            }
            try {
                discount = Double.parseDouble(discountInput);
                if (discount >= 0 && discount <= 25) {
                    break; // Valid discount
                } else {
                    System.out.println(messages.getString("invalid_discount_range"));
                }
            } catch (NumberFormatException e) {
                System.out.println(messages.getString("invalid_discount_format"));
            }
        }
        return discount;
    }

    // Placeholder method to retrieve the menu instance.
    private Menu getMenu() {
        return new Menu(); // Replace with actual logic to retrieve the Menu instance
    }
}
