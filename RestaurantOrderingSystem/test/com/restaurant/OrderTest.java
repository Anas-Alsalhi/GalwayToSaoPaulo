package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class OrderTest {

    @Test
    void testAddDish() throws InvalidOrderException {
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);

        Dish dish = new Dish("Pão de Queijo", 5.99, Dish.Category.APPETIZER);
        order.addDish(dish);

        assertEquals(1, order.getDishes().size());
        assertTrue(order.getDishes().contains(dish));
    }

    @Test
    void testRemoveDish() throws InvalidOrderException {
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);

        Dish dish = new Dish("Pão de Queijo", 5.99, Dish.Category.APPETIZER);
        order.addDish(dish);
        order.removeDish(dish);

        assertEquals(0, order.getDishes().size());
    }

    @Test
    void testSetFinalPrice() {
        Table table = new Table(1, 4);
        Waiter waiter = new Waiter("John", 101);
        Order order = new Order(table, waiter);

        order.setFinalPrice(50.0);
        assertEquals(50.0, order.getFinalPrice());
    }

    @Test
    void testParseOrder() {
        Menu menu = new Menu();
        String orderString = "1,John,101,Pão de Queijo,Salad";
        Order order = Order.parse(orderString, menu);

        assertEquals(1, order.getTable().getTableNumber());
        assertEquals("John", order.getWaiter().getName());
        assertEquals(2, order.getDishes().size());
    }
}
