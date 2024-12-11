package com.restaurant;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.StructuredTaskScope;
import java.util.Random;

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

        System.out.println("Welcome to Sabor Brasileiro!");
        System.out.println("Authentic Brazilian Cuisine and International Favorites Await You.");
        System.out.println("============================================\n");

        while (!exit) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View Menu");
            System.out.println("2. View Today's Specials");
            System.out.println("3. Place and Process an Order");
            System.out.println("4. View Order History");
            System.out.println("5. Save Order History to File");
            System.out.println("6. Load Order History from File");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> menu.displayMenuByCategory();
                case 2 -> menu.displayDailySpecials();
                case 3 -> processOrder(menu, scanner, orderHistory);
                case 4 -> orderHistory.displayHistory();
                case 5 -> {
                    System.out.print("Enter file name to save history: ");
                    String fileName = scanner.nextLine();
                    orderHistory.saveToFile(fileName);
                }
                case 6 -> {
                    System.out.print("Enter file name to load history: ");
                    String fileName = scanner.nextLine();
                    orderHistory.loadFromFile(fileName);
                }
                case 7 -> {
                    System.out.println("\nThank you for visiting Sabor Brasileiro!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void processOrder(Menu menu, Scanner scanner, OrderHistory orderHistory) {
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

            System.out.println("\n========== MENU ==========");
            for (int i = 0; i < allDishes.size(); i++) {
                Dish dish = allDishes.get(i);
                System.out.printf("%d. %-25s (€%.2f)%n", i + 1, dish.name(), dish.price());
            }
            System.out.println("==========================");

            while (true) {
                System.out.print("Enter the number of a dish to add (or '0' to finish): ");
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice == 0) break;
                    if (choice > 0 && choice <= allDishes.size()) {
                        Dish selectedDish = allDishes.get(choice - 1);
                        try {
                            order.addDish(selectedDish);
                            System.out.printf("Added: %s (€%.2f)%n", selectedDish.name(), selectedDish.price());
                        } catch (InvalidOrderException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid number. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }

            System.out.println("\nOrder Summary:");
            System.out.println("Table Details:");
            System.out.printf(" - Table Number: %d%n", table.getTableNumber());
            System.out.printf(" - Capacity: %d customers%n", table.getCapacity());
            System.out.printf(" - Seated Customers: %d%n", seatedCustomers);
            System.out.printf(" - Waiter: %s%n", waiter.getName());

            System.out.println("\nOrdered Items:");
            order.printDetails();

            double total = order.getDishes().stream().mapToDouble(Dish::price).sum();
            System.out.printf("Subtotal: €%.2f%n", total);

            System.out.print("Enter discount percentage (0-100) or press Enter for no discount: ");
            String discountInput = scanner.nextLine().trim();
            double discount = discountInput.matches("\\d{1,3}") ? Double.parseDouble(discountInput) : 0.0;
            if (discount < 0 || discount > 100) discount = 0.0;

            double discountedTotal = total - (total * (discount / 100));
            System.out.printf("Total after discount: €%.2f%n", discountedTotal);

            order.setDiscountPercentage(discount);
            order.setFinalPrice(discountedTotal);

            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                var processingTask = scope.fork(() -> {
                    Thread.sleep(2000);
                    return "Order processed successfully!";
                });

                scope.join();
                scope.throwIfFailed();
                System.out.println(processingTask.get());
            } catch (Exception e) {
                System.err.println("Order processing failed: " + e.getMessage());
            }

            if (!orderHistory.getOrders().contains(order)) {
                orderHistory.addOrder(order);
                System.out.println("Order added to history.");
            }
        }

    }

