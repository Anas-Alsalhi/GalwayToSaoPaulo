package com.restaurant;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.concurrent.CountDownLatch;

/**
 * Main application for managing the restaurant system.
 */
public class RestaurantApp {

    private static final List<String> WAITER_NAMES = List.of("Sophia", "Liam", "Olivia", "Noah", "Emma");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();
        OrderHistory orderHistory = new OrderHistory();
        boolean exit = false;

        // Prompt user to select a language
        System.out.println("============================================");
        System.out.println("Select a language:");
        System.out.println("1. English");
        System.out.println("2. Portuguese");
        System.out.println("3. French");
        System.out.println("4. Italian");
        System.out.println("5. Spanish");
        System.out.println("============================================");
        int languageChoice = scanner.nextInt();
        scanner.nextLine();

        // Set locale based on user choice
        Locale locale = switch (languageChoice) {
            case 2 -> Locale.of("pt", "PT");
            case 3 -> Locale.of("fr", "FR");
            case 4 -> Locale.of("it", "IT");
            case 5 -> Locale.of("es", "ES");
            default -> Locale.ENGLISH;
        };

        // Load resource bundle for localized messages
        ResourceBundle messages;
        try {
            messages = ResourceBundle.getBundle("com.restaurant.messages", locale);
        } catch (MissingResourceException e) {
            System.err.println("Missing resource bundle for locale: " + locale + ". Falling back to English.");
            messages = ResourceBundle.getBundle("com.restaurant.messages", Locale.ENGLISH);
        }

        System.out.println();
        System.out.println("============================================");
        System.out.println(messages.getString("welcome").replace("Sabor Brasilero", "Galway to São Paulo"));
        System.out.println(messages.getString("tagline"));
        System.out.println("============================================\n");

        while (!exit) {
            System.out.println("\n" + messages.getString("choose_option"));
            System.out.println("1. " + messages.getString("view_menu"));
            System.out.println("2. " + messages.getString("view_specials"));
            System.out.println("3. " + messages.getString("place_order"));
            System.out.println("4. " + messages.getString("view_history"));
            System.out.println("5. " + messages.getString("save_history"));
            System.out.println("6. " + messages.getString("load_history"));
            System.out.println("7. " + messages.getString("exit"));
            System.out.print(messages.getString("enter_choice"));

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println(messages.getString("invalid_input"));
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> menu.displayMenuByCategory(messages);
                case 2 -> menu.displayDailySpecials();
                case 3 -> processOrder(menu, scanner, orderHistory, messages, locale);
                case 4 -> orderHistory.displayHistory();
                case 5 -> {
                    System.out.print(messages.getString("enter_file_save"));
                    String fileName = scanner.nextLine();
                    Path filePath = Paths.get(fileName);
                    orderHistory.saveToFile(filePath);
                }
                case 6 -> {
                    System.out.print(messages.getString("enter_file_load"));
                    String fileName = scanner.nextLine();
                    Path filePath = Paths.get(fileName);
                    orderHistory.loadFromFile(filePath);
                }
                case 7 -> {
                    System.out.println("\n" + messages.getString("thank_you"));
                    exit = true;
                }
                default -> System.out.println(messages.getString("invalid_choice"));
            }
        }

        System.out.println(messages.getString("goodbye"));
    }

    private static void processOrder(Menu menu, Scanner scanner, OrderHistory orderHistory, ResourceBundle messages, Locale locale) {
        List<Table> tables = List.of(
                new Table(1, 2),
                new Table(2, 4),
                new Table(3, 6),
                new Table(4, 8)
        );
        Random random = new Random();
        Table table = tables.get(random.nextInt(tables.size()));
        int seatedCustomers = random.nextInt(table.getCapacity()) + 1;
        String waiterName = WAITER_NAMES.get(random.nextInt(WAITER_NAMES.size()));
        int waiterId = random.nextInt(1000); // Generate a random waiter ID
        Waiter waiter = new Waiter(waiterName, waiterId);

        Order order = new Order(table, waiter);
        List<Dish> allDishes = menu.getAllDishes();

        System.out.println("\n" + "=".repeat(50));
        System.out.printf("%25s%n", messages.getString("menu"));
        System.out.println("=".repeat(50));

        for (int i = 0; i < allDishes.size(); i++) {
            Dish dish = allDishes.get(i);
            System.out.printf("%-3d %-30s (€ %.2f)%n", i + 1, dish.name(), dish.price());
        }

        System.out.println("=".repeat(50));

        while (true) {
            System.out.print(messages.getString("enter_dish_number"));
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 0) {
                    System.out.println(messages.getString("invalid_number"));
                    continue; // Reject negative numbers
                }
                if (choice == 0) break;
                if (choice > 0 && choice <= allDishes.size()) {
                    Dish selectedDish = allDishes.get(choice - 1);
                    if (menu.isDishAvailable(selectedDish)) {
                        try {
                            order.addDish(selectedDish);
                            System.out.printf(messages.getString("added_dish"), selectedDish.name(), selectedDish.price());
                        } catch (InvalidOrderException e) {
                            System.out.println(messages.getString("error") + ": " + e.getMessage());
                        }
                    } else {
                        System.out.println(messages.getString("dish_not_available"));
                    }
                } else {
                    System.out.println(messages.getString("invalid_number"));
                }
            } else {
                System.out.println(messages.getString("invalid_input"));
                scanner.nextLine();
            }
        }

        System.out.println("\n" + messages.getString("order_summary"));
        System.out.println(messages.getString("table_details"));
        System.out.println(String.format("  %s", String.format(messages.getString("capacity"), table.getCapacity())));
        System.out.println(String.format("  %s", String.format(messages.getString("seated_customers"), seatedCustomers)));
        System.out.println(String.format("  %s", String.format(messages.getString("waiter"), waiter.getName())));

        System.out.println("\n" + messages.getString("ordered_items"));
        order.printDetails();

        double total = order.getDishes().stream().mapToDouble(Dish::price).sum();
        System.out.println(String.format("\n%s", String.format(messages.getString("subtotal"), total)));

        double discount = 0;
        while (true) {
            System.out.print(messages.getString("enter_discount"));
            String discountInput = scanner.nextLine().trim();

            if (discountInput.isEmpty()) {
                break; // No discount
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

        double discountedTotal = total - (total * (discount / 100));
        System.out.println(String.format("\n%s", String.format(messages.getString("total_after_discount"), discountedTotal)));

        order.setDiscountPercentage(discount);
        order.setFinalPrice(discountedTotal);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", locale);
        System.out.println(String.format("\n%s", String.format(messages.getString("order_timestamp"), now.format(formatter))));

        // Use CountDownLatch to simulate dish preparation
        CountDownLatch latch = new CountDownLatch(order.getDishes().size());
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            for (Dish dish : order.getDishes()) {
                executor.submit(() -> {
                    try {
                        System.out.printf(messages.getString("preparing_dish"), dish.name());
                        Thread.sleep(1000 + random.nextInt(2000)); // Simulate preparation time
                        System.out.printf(messages.getString("prepared_dish"), dish.name());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(); // Wait for all dishes to be prepared
            System.out.println("\n" + messages.getString("all_dishes_prepared")); // Added empty line
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(messages.getString("preparation_interrupted"));
        }

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<String> processingTask = executor.submit(() -> {
                Thread.sleep(2000);
                return messages.getString("order_processed");
            });

            System.out.println("\n" + processingTask.get()); // Added empty line
        } catch (Exception e) {
            System.err.println(messages.getString("order_failed") + ": " + e.getMessage());
        }

        if (!orderHistory.getOrders().contains(order)) {
            orderHistory.addOrder(order);
            System.out.println("\n" + messages.getString("order_added_history")); // Added empty line
        }
    }
}

