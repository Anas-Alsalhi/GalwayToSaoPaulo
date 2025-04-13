package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;

class OrderHistoryTest {

    @Test
    void testAddOrder() {
        OrderHistory history = new OrderHistory();
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);

        history.addOrder(order);
        assertEquals(1, history.getOrders().size());
    }

    @Test
    void testSaveAndLoadFromFile() {
        OrderHistory history = new OrderHistory();
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);
        history.addOrder(order);

        Path filePath = Path.of("test_order_history.dat");
        history.saveToFile(filePath);

        OrderHistory loadedHistory = new OrderHistory();
        loadedHistory.loadFromFile(filePath);

        assertEquals(1, loadedHistory.getOrders().size());
        assertEquals(order, loadedHistory.getOrders().get(0));
    }

    @Test
    void testDisplayHistory() {
        OrderHistory history = new OrderHistory();
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);

        history.addOrder(order);
        history.displayHistory();

        assertEquals(1, history.getOrders().size());
    }
}
