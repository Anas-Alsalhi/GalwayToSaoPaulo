package com.restaurant;

import java.util.*;

/**
 * Represents the Brazilian and Irish restaurant's menu.
 */
public class Menu {

    private final List<Dish> dishes;
    private final List<Dish> irishDishes; // List of Irish dishes for today's specials

    /**
     * Constructor for initializing the menu.
     */
    public Menu() {
        // Full menu
        dishes = List.of(
            new Dish("Pão de Queijo", 5.99, Dish.Category.APPETIZER),
            new Dish("Salad", 6.99, Dish.Category.APPETIZER), // Adjusted price
            new Dish("Bruschetta", 6.99, Dish.Category.APPETIZER),
            new Dish("Fish Cake", 6.49, Dish.Category.APPETIZER),
            new Dish("Colcannon", 7.99, Dish.Category.APPETIZER), // Adjusted price
            new Dish("Boxty", 7.99, Dish.Category.APPETIZER),
            new Dish("Feijoada", 14.99, Dish.Category.MAIN_COURSE),
            new Dish("Churrasco", 15.99, Dish.Category.MAIN_COURSE),
            new Dish("Irish Stew", 12.99, Dish.Category.MAIN_COURSE),
            new Dish("Bacon and Cabbage", 13.99, Dish.Category.MAIN_COURSE),
            new Dish("Pasta", 12.99, Dish.Category.MAIN_COURSE),
            new Dish("Pizza", 10.99, Dish.Category.MAIN_COURSE), // Adjusted price
            new Dish("Fish and Chips", 10.99, Dish.Category.MAIN_COURSE),
            new Dish("Shepherd's Pie", 13.49, Dish.Category.MAIN_COURSE),
            new Dish("Irish Full Breakfast", 14.99, Dish.Category.MAIN_COURSE),
            new Dish("Tea", 2.49, Dish.Category.BEVERAGE),
            new Dish("Coffee", 2.99, Dish.Category.BEVERAGE),
            new Dish("Latte", 3.99, Dish.Category.BEVERAGE),
            new Dish("Orange Juice", 3.49, Dish.Category.BEVERAGE),
            new Dish("Apple Juice", 3.99, Dish.Category.BEVERAGE),
            new Dish("Pineapple Juice", 3.99, Dish.Category.BEVERAGE), // Adjusted price
            new Dish("Apple Tart", 4.99, Dish.Category.DESSERT),
            new Dish("Baileys Cheesecake", 5.49, Dish.Category.DESSERT),
            new Dish("Irish Cream Brownie", 3.99, Dish.Category.DESSERT)
        );

        // Irish dishes only (use a mutable list to avoid UnsupportedOperationException)
        irishDishes = new ArrayList<>(List.of(
            new Dish("Irish Stew", 12.99, Dish.Category.MAIN_COURSE),
            new Dish("Bacon and Cabbage", 13.99, Dish.Category.MAIN_COURSE),
            new Dish("Colcannon", 7.99, Dish.Category.APPETIZER), // Adjusted price
            new Dish("Fish and Chips", 10.99, Dish.Category.MAIN_COURSE),
            new Dish("Baileys Cheesecake", 5.49, Dish.Category.DESSERT),
            new Dish("Irish Cream Brownie", 3.99, Dish.Category.DESSERT),
            new Dish("Tea", 2.49, Dish.Category.BEVERAGE),
            new Dish("Boxty", 7.99, Dish.Category.APPETIZER), // New Irish appetizer
            new Dish("Shepherd's Pie", 13.49, Dish.Category.MAIN_COURSE), // New Irish main course
            new Dish("Irish Full Breakfast", 14.99, Dish.Category.MAIN_COURSE), // New Irish main course
            new Dish("Salad", 6.99, Dish.Category.APPETIZER), // Adjusted price
            new Dish("Pizza", 10.99, Dish.Category.MAIN_COURSE), // Adjusted price
            new Dish("Pineapple Juice", 3.99, Dish.Category.BEVERAGE) // Adjusted price
        ));
    }

    /**
     * Displays the menu categorized by dish type in a specific order.
     */
    // Displays the menu categorized by dish type.
    // Each category is displayed in a specific order.
    public void displayMenuByCategory(ResourceBundle messages) {
        Map<Dish.Category, List<Dish>> categorizedMenu = new LinkedHashMap<>();
        for (Dish.Category category : Dish.Category.values()) {
            categorizedMenu.put(category, new ArrayList<>());
        }

        for (Dish dish : dishes) {
            categorizedMenu.get(dish.category()).add(dish);
        }

        if (categorizedMenu == null || categorizedMenu.isEmpty()) {
            System.out.println(messages.getString("menu_empty"));
            return;
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println(" " + messages.getString("menu"));
        System.out.println("=".repeat(40));

        for (Dish.Category category : Dish.Category.values()) {
            if (categorizedMenu.get(category).isEmpty()) continue;
            System.out.println("\n" + messages.getString(category.name()));
            System.out.println("-".repeat(40));
            for (Dish dish : categorizedMenu.get(category)) {
                System.out.printf("%-3s %-25s (€ %.2f)%n", 
                    (dishes.indexOf(dish) + 1) + ".", dish.name(), dish.price());
            }
        }

        System.out.println("=".repeat(40));
    }

    /**
     * Displays today's specials, which are chosen randomly from Irish dishes.
     * The specials remain the same throughout the session.
     */
    // Displays today's specials, which are randomly selected from Irish dishes.
    // The specials remain the same throughout the session.
    public void displayDailySpecials(int limit) {
        Collections.shuffle(irishDishes); // Shuffle the mutable list
        List<Dish> dailySpecials = new ArrayList<>(irishDishes.stream().limit(limit).toList());
        System.out.println("\n========== TODAY'S BRAZILIAN AND IRISH SPECIALS ==========");
        dailySpecials.forEach(dish ->
            System.out.printf(" - %-25s (€%.2f)%n", dish.name(), dish.price())
        );
        System.out.println("==========================================================");
    }

    /**
     * Checks if a specific dish is available in the menu.
     *
     * @param dish The dish to check.
     * @return True if the dish is available, false otherwise.
     */
    // Checks if a specific dish is available in the menu.
    public boolean isDishAvailable(Dish dish) {
        return dishes.contains(dish);
    }

    // Retrieves all dishes in the menu as an unmodifiable list.
    public List<Dish> getAllDishes() {
        return Collections.unmodifiableList(dishes);
    }

    /**
     * Finds a dish by its name.
     *
     * @param name The name of the dish.
     * @return An Optional containing the Dish object if found, otherwise an empty Optional.
     */
    // Finds a dish by its name.
    // Returns an Optional containing the dish if found.
    public Optional<Dish> findDishByName(String name) {
        return dishes.stream()
                     .filter(dish -> dish.name().equalsIgnoreCase(name))
                     .findFirst();
    }
}
