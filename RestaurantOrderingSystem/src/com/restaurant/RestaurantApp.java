package com.restaurant;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.time.*;
import java.time.format.*;

public class RestaurantApp {

    // List of waiter names used for assigning random waiters to tables
    private static final List<String> WAITER_NAMES = List.of("Sophia", "Liam", "Olivia", "Noah", "Emma");

    // Number of supported languages for the application
    private static final int LANGUAGE_COUNT = 10;

    public static void main(String[] args) {
        // Scanner is used to read user input from the console
        Scanner scanner = new Scanner(System.in);

        // Menu object represents the restaurant's menu
        Menu menu = new Menu();

        // OrderHistory object manages the history of all orders
        OrderHistory orderHistory = new OrderHistory();

        boolean exit = false; // Flag to control the main loop

        // Display the language selection menu to the user
        printLanguageSelectionMenu();

        // Get the user's choice of language and set the corresponding locale
        int languageChoice = getValidLanguageChoice(scanner);
        Locale locale = getLocaleForLanguageChoice(languageChoice);

        // Load the appropriate resource bundle for the selected language
        ResourceBundle messages = loadResourceBundle(locale);

        // Display a welcome message in the selected language
        printWelcomeMessage(messages);

        // Main loop for the application
        while (!exit) {
            // Display the main menu options
            printMainMenu(messages);

            // Get the user's choice from the main menu
            int choice = getValidMenuChoice(scanner, messages);

            // Handle the user's choice using a switch statement
            switch (choice) {
                case 1 -> menu.displayMenuByCategory(messages); // Display the menu categorized by dish type
                case 2 -> menu.displayDailySpecials(); // Show today's specials
                case 3 -> processOrder(menu, scanner, orderHistory, messages, locale); // Process a new order
                case 4 -> orderHistory.displayHistory(); // Display the order history
                case 5 -> saveOrderHistory(scanner, orderHistory, messages); // Save order history to a file
                case 6 -> loadOrderHistory(scanner, orderHistory, messages); // Load order history from a file
                case 7 -> bookEvent(scanner, messages); // Book an event
                case 8 -> recommendDishes(orderHistory, menu, messages); // Show AI-powered dish recommendations
                case 9 -> {
                    // Exit the application
                    System.out.println("\n" + messages.getString("thank_you"));
                    exit = true;
                }
                default -> System.out.println(messages.getString("invalid_choice")); // Handle invalid menu choices
            }
        }

        // Display a goodbye message before exiting
        System.out.println(messages.getString("goodbye"));
    }

    // Method to display the language selection menu
    private static void printLanguageSelectionMenu() {
        System.out.println("============================================");
        System.out.println("Select a language:");
        // List of supported languages
        System.out.println("1. English");
        System.out.println("2. Portuguese");
        System.out.println("3. French");
        System.out.println("4. Italian");
        System.out.println("5. Spanish");
        System.out.println("6. German");
        System.out.println("7. Chinese");
        System.out.println("8. Russian");
        System.out.println("9. Norwegian");
        System.out.println("10. Japanese");
        System.out.println("============================================");
    }

    // Method to get a valid language choice from the user
    private static int getValidLanguageChoice(Scanner scanner) {
        int languageChoice = -1;
        while (languageChoice < 1 || languageChoice > LANGUAGE_COUNT) {
            System.out.print("Please enter a number between 1 and " + LANGUAGE_COUNT + ": ");
            if (scanner.hasNextInt()) {
                languageChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (languageChoice < 1 || languageChoice > LANGUAGE_COUNT) {
                    System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return languageChoice;
    }

    // Method to map the user's language choice to a Locale object
    private static Locale getLocaleForLanguageChoice(int languageChoice) {
        Map<Integer, Locale> localeMap = Map.of(
            1, Locale.ENGLISH,
            2, Locale.of("pt", "PT"),
            3, Locale.of("fr", "FR"),
            4, Locale.of("it", "IT"),
            5, Locale.of("es", "ES"),
            6, Locale.GERMAN,
            7, Locale.CHINA,
            8, Locale.of("ru", "RU"),
            9, Locale.of("no", "NO"),
            10, Locale.JAPAN
        );
        return localeMap.getOrDefault(languageChoice, Locale.ENGLISH);
    }

    // Method to load the appropriate resource bundle for the selected language
    private static ResourceBundle loadResourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle("com.restaurant.messages", locale);
        } catch (MissingResourceException e) {
            System.err.println("Missing resource bundle for locale: " + locale + ". Falling back to English.");
            return ResourceBundle.getBundle("com.restaurant.messages", Locale.ENGLISH);
        }
    }

    // Method to display a welcome message in the selected language
    private static void printWelcomeMessage(ResourceBundle messages) {
        System.out.println();
        System.out.println("============================================");
        System.out.println(messages.getString("welcome").replace("Sabor Brasileiro", "Galway To São Paulo Restaurant"));
        System.out.println(messages.getString("tagline"));
        System.out.println("============================================\n");
    }

    // Method to display the main menu options
    private static void printMainMenu(ResourceBundle messages) {
        System.out.println("\n" + messages.getString("choose_option"));
        System.out.println("1. " + messages.getString("view_menu"));
        System.out.println("2. " + messages.getString("view_specials"));
        System.out.println("3. " + messages.getString("place_order"));
        System.out.println("4. " + messages.getString("view_history"));
        System.out.println("5. " + messages.getString("save_history"));
        System.out.println("6. " + messages.getString("load_history"));
        System.out.println("7. " + messages.getString("book_event"));
        System.out.println("8. " + messages.getString("ai_recommendations_header"));
        System.out.println("9. " + messages.getString("exit"));
        System.out.print(messages.getString("enter_choice"));
    }

    // Method to get a valid menu choice from the user
    private static int getValidMenuChoice(Scanner scanner, ResourceBundle messages) {
        int choice = -1;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println(messages.getString("invalid_input"));
            scanner.nextLine();
        }
        return choice;
    }

    // Method to save the order history to a file
    private static void saveOrderHistory(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_save"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.saveToFile(filePath);
    }

    // Method to load the order history from a file
    private static void loadOrderHistory(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_load"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.loadFromFile(filePath);
    }

    // Method to process a new order
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
        int waiterId = random.nextInt(1000);
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
                    continue;
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
                break;
            }

            try {
                discount = Double.parseDouble(discountInput);
                if (discount >= 0 && discount <= 25) {
                    break;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", locale);
        System.out.println(String.format("\n%s", String.format(messages.getString("order_timestamp"), now.format(formatter))));

        CountDownLatch latch = new CountDownLatch(order.getDishes().size());
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            for (Dish dish : order.getDishes()) {
                executor.submit(() -> {
                    try {
                        System.out.printf(messages.getString("preparing_dish"), dish.name());
                        Thread.sleep(1000 + random.nextInt(2000));
                        System.out.printf(messages.getString("prepared_dish"), dish.name());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            System.out.println("\n" + messages.getString("all_dishes_prepared"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(messages.getString("preparation_interrupted"));
        }

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<String> processingTask = executor.submit(() -> {
                Thread.sleep(2000);
                return messages.getString("order_processed");
            });

            System.out.println("\n" + processingTask.get());
        } catch (Exception e) {
            System.err.println(messages.getString("order_failed") + ": " + e.getMessage());
        }

        if (!orderHistory.getOrders().contains(order)) {
            orderHistory.addOrder(order);
            System.out.println("\n" + messages.getString("order_added_history"));
        }
    }

    // Method to book an event
    private static void bookEvent(Scanner scanner, ResourceBundle messages) {
        System.out.println("\n" + messages.getString("event_booking_header"));
        System.out.print(messages.getString("enter_event_name"));
        String eventName = scanner.nextLine();

        String eventDate;
        while (true) {
            System.out.print(messages.getString("enter_event_date"));
            eventDate = scanner.nextLine().trim();
            if (eventDate.isEmpty()) {
                System.out.println(messages.getString("invalid_event_date_format"));
                continue;
            }
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
                // Add a fallback formatter for locales with different date formats
                if (Locale.getDefault().getLanguage().equals("es")) {
                    formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
                }
                LocalDateTime enteredDate = LocalDate.parse(eventDate, formatter).atStartOfDay();
                if (enteredDate.isAfter(LocalDateTime.now())) {
                    break;
                } else {
                    System.out.println(messages.getString("invalid_event_date_future"));
                }
            } catch (DateTimeParseException e) {
                System.out.println(messages.getString("invalid_event_date_format"));
            }
        }

        System.out.print(messages.getString("enter_event_time"));
        String eventTime = scanner.nextLine();

        System.out.print(messages.getString("enter_guest_count"));
        int guestCount = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n" + messages.getString("event_booking_summary"));
        System.out.printf(messages.getString("event_name"), eventName);
        System.out.printf(messages.getString("event_date"), eventDate);
        System.out.printf(messages.getString("event_time"), eventTime);
        System.out.printf(messages.getString("guest_count"), guestCount);

        System.out.print("\n" + messages.getString("confirm_booking"));
        String confirmation = scanner.nextLine().trim().toLowerCase();

        String yesLocalized = messages.getString("yes").toLowerCase(); // Add localized "yes"
        if (confirmation.equals(yesLocalized)) {
            System.out.println(messages.getString("event_booking_success"));
        } else {
            System.out.println(messages.getString("event_booking_cancelled"));
        }
    }

    // Method to recommend dishes based on order history
    private static void recommendDishes(OrderHistory orderHistory, Menu menu, ResourceBundle messages) {
        System.out.println("\n" + messages.getString("ai_recommendations_header").replace("IA", "AI"));

        var dishFrequency = orderHistory.getOrders().stream()
            .flatMap(order -> order.getDishes().stream())
            .collect(Collectors.groupingBy(Dish::name, Collectors.counting()));

        List<Dish> recommendedDishes = dishFrequency.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(3)
            .map(entry -> menu.getAllDishes().stream() // Use menu instance to access dishes
                .filter(dish -> dish.name().equals(entry.getKey()))
                .findFirst().orElse(null))
            .filter(dish -> dish != null)
            .collect(Collectors.toList());

        if (recommendedDishes.isEmpty()) {
            System.out.println(messages.getString("no_recommendations"));
        } else {
            System.out.println(messages.getString("recommended_dishes"));
            recommendedDishes.forEach(dish ->
                System.out.printf(" - %-25s (€%.2f)%n", dish.name(), dish.price())
            );
        }
    }
}
