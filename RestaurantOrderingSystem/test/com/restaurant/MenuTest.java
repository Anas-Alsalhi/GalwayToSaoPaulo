package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MenuTest {

    @Test
    void testGetAllDishes() {
        Menu menu = new Menu();
        List<Dish> dishes = menu.getAllDishes();

        assertNotNull(dishes);
        assertTrue(dishes.size() > 0);
    }

    @Test
    void testFindDishByName() {
        Menu menu = new Menu();
        Dish dish = menu.findDishByName("Pão de Queijo").orElse(null);

        assertNotNull(dish);
        assertEquals("Pão de Queijo", dish.name());
    }

    @Test
    void testDisplayDailySpecials() {
        Menu menu = new Menu();
        menu.displayDailySpecials(3);

        assertNotNull(menu.getCachedSpecials());
        assertEquals(3, menu.getCachedSpecials().size());
    }
}
