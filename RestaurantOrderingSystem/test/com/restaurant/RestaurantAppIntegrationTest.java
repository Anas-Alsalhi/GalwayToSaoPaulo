package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

class RestaurantAppIntegrationTest {

    @Test
    void testProcessOrder() {
        Menu menu = new Menu();
        OrderHistory history = new OrderHistory();
        ResourceBundle messages = ResourceBundle.getBundle("com.restaurant.messages", Locale.ENGLISH);
        Scanner scanner = new Scanner("1\n0\n");

        RestaurantApp.processOrder(menu, scanner, history, messages, Locale.ENGLISH);

        assertEquals(1, history.getOrders().size());
    }

    @Test
    void testBookEvent() {
        ResourceBundle messages = ResourceBundle.getBundle("com.restaurant.messages", Locale.ENGLISH);
        Scanner scanner = new Scanner("Birthday Party\n25/12/2025\n18:00\n10\nyes\n");

        RestaurantApp.bookEvent(scanner, messages);

        // Validate output manually or mock System.out for assertions
        assertTrue(true); // Placeholder for actual validation
    }
}
