package com.restaurant;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the history of orders.
 */
public class OrderHistory {

    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void displayHistory() {
        if (orders.isEmpty()) {
            System.out.println("No orders in history.");
        } else {
            orders.stream().forEach(Order::printDetails);
        }
    }

    public void saveToFile(Path filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(orders);
            System.out.println("Order history saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save order history: " + e.getMessage());
        }
    }

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

    // New method: Save order history as plain text using BufferedWriter
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

    // New method: Load order history from plain text using BufferedReader
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
                    orders.add(Order.parse(line)); // Ensure Order.parse is implemented correctly
                } catch (IllegalArgumentException e) {
                    System.err.println("Failed to parse order: " + e.getMessage());
                }
            }
            System.out.println("Order history loaded from text successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load order history from text: " + e.getMessage());
        }
    }

    // New method: Demonstrate path handling
    public void demonstratePathHandling(Path basePath, String relativePath) {
        Path resolvedPath = basePath.resolve(relativePath);
        Path normalizedPath = resolvedPath.normalize();
        Path relativizedPath = basePath.relativize(normalizedPath);

        System.out.println("Base Path: " + basePath);
        System.out.println("Relative Path: " + relativePath);
        System.out.println("Resolved Path: " + resolvedPath);
        System.out.println("Normalized Path: " + normalizedPath);
        System.out.println("Relativized Path: " + relativizedPath);
    }
}
