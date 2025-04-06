package com.restaurant;

import java.util.*;

/**
 * Represents the restaurant's menu.
 */
public class Menu {

    private final List<Dish> dishes;
    private final List<Dish> brazilianDishes; // List of Brazilian dishes for today's specials

    /**
     * Constructor for initializing the menu.
     */
    public Menu() {
        // Full menu
        dishes = List.of(
            new Dish("Pão de Queijo", 5.99, Dish.Category.APPETIZER),
            new Dish("Salad", 7.49, Dish.Category.APPETIZER),
            new Dish("Bruschetta", 6.99, Dish.Category.APPETIZER),
            new Dish("Fish Cake", 6.49, Dish.Category.APPETIZER),
            new Dish("Falafel", 8.99, Dish.Category.APPETIZER),
            new Dish("Feijoada", 14.99, Dish.Category.MAIN_COURSE),
            new Dish("Churrasco", 15.99, Dish.Category.MAIN_COURSE),
            new Dish("Irish Stew", 12.99, Dish.Category.MAIN_COURSE),
            new Dish("Bacon and Cabbage", 13.99, Dish.Category.MAIN_COURSE),
            new Dish("Pasta", 12.99, Dish.Category.MAIN_COURSE),
            new Dish("Pizza", 9.99, Dish.Category.MAIN_COURSE),
            new Dish("Fish and Chips", 10.99, Dish.Category.MAIN_COURSE),
            new Dish("Tea", 2.49, Dish.Category.BEVERAGE),
            new Dish("Coffee", 2.99, Dish.Category.BEVERAGE),
            new Dish("Latte", 3.99, Dish.Category.BEVERAGE),
            new Dish("Orange Juice", 3.49, Dish.Category.BEVERAGE),
            new Dish("Apple Juice", 3.99, Dish.Category.BEVERAGE),
            new Dish("Pineapple Juice", 4.49, Dish.Category.BEVERAGE),
            new Dish("Brigadeiro", 3.99, Dish.Category.DESSERT),
            new Dish("Tiramisu", 4.99, Dish.Category.DESSERT),
            new Dish("Baklava", 5.49, Dish.Category.DESSERT)
        );

        // Brazilian dishes only (use a mutable list to avoid UnsupportedOperationException)
        brazilianDishes = new ArrayList<>(List.of(
            new Dish("Pão de Queijo", 5.99, Dish.Category.APPETIZER),
            new Dish("Feijoada", 14.99, Dish.Category.MAIN_COURSE),
            new Dish("Churrasco", 15.99, Dish.Category.MAIN_COURSE),
            new Dish("Brigadeiro", 3.99, Dish.Category.DESSERT),
            new Dish("Pineapple Juice", 4.49, Dish.Category.BEVERAGE)
        ));
    }

    /**
     * Displays the menu categorized by dish type in a specific order.
     */
    public void displayMenuByCategory() {
        Map<Dish.Category, List<Dish>> categorizedMenu = new LinkedHashMap<>();
        for (Dish.Category category : Dish.Category.values()) {
            categorizedMenu.put(category, new ArrayList<>());
        }

        for (Dish dish : dishes) {
            categorizedMenu.get(dish.category()).add(dish);
        }

        System.out.println("\n========== FULL MENU ==========");
        for (Dish.Category category : Dish.Category.values()) {
            if (categorizedMenu.get(category).isEmpty()) continue;
            System.out.println(category.toString().replace("_", " ") + ":");
            for (Dish dish : categorizedMenu.get(category)) {
                System.out.printf(" - %-25s (€%.2f)%n", dish.name(), dish.price());
            }
            System.out.println();
        }
        System.out.println("===============================");
    }

    /**
     * Displays today's specials, which are chosen randomly from Brazilian dishes.
     */
    public void displayDailySpecials() {
        Collections.shuffle(brazilianDishes); // Shuffle the mutable list
        System.out.println("\n========== TODAY'S SPECIALS ==========");
        brazilianDishes.stream().limit(2).forEach(dish ->
            System.out.printf(" - %-25s (€%.2f)%n", dish.name(), dish.price())
        );
        System.out.println("======================================");
    }

    /**
     * Checks if a specific dish is available in the menu.
     *
     * @param dish The dish to check.
     * @return True if the dish is available, false otherwise.
     */
    public boolean isDishAvailable(Dish dish) {
        return dishes.contains(dish);
    }

    public List<Dish> getAllDishes() {
        return Collections.unmodifiableList(dishes);
    }

    /**
     * Finds a dish by its name.
     *
     * @param name The name of the dish.
     * @return The Dish object if found, otherwise null.
     */
    public static Dish findDishByName(String name) {
        for (Dish dish : new Menu().getAllDishes()) {
            if (dish.name().equalsIgnoreCase(name)) {
                return dish;
            }
        }
        return null;
    }
}
