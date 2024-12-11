package com.restaurant;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maintains a history of all orders placed in the restaurant.
 */
public class OrderHistory {

    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
        }
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void displayHistory() {
        if (orders.isEmpty()) {
            System.out.println("No orders have been placed yet.");
        } else {
            System.out.println("\n========== ORDER HISTORY ==========");
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                System.out.printf("Order #%d:%n", i + 1);
                System.out.printf(" - Table Number: %d%n", order.getTable().getTableNumber());
                System.out.printf(" - Waiter: %s%n", order.getWaiter().getName());
                System.out.printf(" - Discount Applied: %.2f%%%n", order.getDiscountPercentage());
                System.out.printf(" - Final Price Paid: €%.2f%n", order.getFinalPrice());
                order.printDetails();
                System.out.println();
            }
            System.out.println("====================================");
        }
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(orders);
            System.out.println("Order history saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            orders.clear();
            orders.addAll((List<Order>) ois.readObject());
            System.out.println("Order history loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
    }
}
