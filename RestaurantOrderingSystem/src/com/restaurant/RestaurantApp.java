package com.restaurant;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.time.*;
import java.time.format.*;

public class RestaurantApp {

    // A list of waiter names that will be randomly assigned to tables.
    private static final List<String> WAITER_NAMES = List.of("Sophia", "Liam", "Olivia", "Noah", "Emma");

    // The total number of languages supported by the application.
    private static final int LANGUAGE_COUNT = 10;

    public static void main(String[] args) {
        // Scanner is used to read input from the user via the console.
        Scanner scanner = new Scanner(System.in);

        // The Menu object represents the restaurant's menu, containing all available dishes.
        Menu menu = new Menu();

        // The OrderHistory object keeps track of all orders placed by customers.
        OrderHistory orderHistory = new OrderHistory();

        // This flag controls whether the application should keep running or exit.
        boolean exit = false;

        // Step 1: Display the language selection menu to the user.
        printLanguageSelectionMenu();

        // Step 2: Get the user's choice of language and set the corresponding locale (language and region).
        int languageChoice = getValidLanguageChoice(scanner);
        Locale locale = getLocaleForLanguageChoice(languageChoice);

        // Step 3: Load the appropriate resource bundle (translations) for the selected language.
        ResourceBundle messages = loadResourceBundle(locale);

        // Step 4: Display a welcome message in the selected language.
        printWelcomeMessage(messages);

        // Step 5: Main loop of the application. This keeps running until the user chooses to exit.
        while (!exit) {
            // Display the main menu options to the user.
            printMainMenu(messages);

            // Get the user's choice from the main menu.
            int choice = getValidMenuChoice(scanner, messages);

            // Handle the user's choice using a switch statement.
            switch (choice) {
                case 1 -> menu.displayMenuByCategory(messages); // Show the menu grouped by dish type.
                case 2 -> menu.displayDailySpecials(3); // Show today's special dishes with a limit of 3.
                case 3 -> processOrder(menu, scanner, orderHistory, messages, locale); // Allow the user to place an order.
                case 4 -> orderHistory.displayHistory(); // Show the history of all previous orders.
                case 5 -> saveOrderHistory(scanner, orderHistory, messages); // Save the order history to a file.
                case 6 -> loadOrderHistory(scanner, orderHistory, messages); // Load order history from a file.
                case 7 -> bookEvent(scanner, messages); // Allow the user to book an event.
                case 8 -> recommendDishes(orderHistory, menu, messages); // Show AI-powered dish recommendations.
                case 9 -> {
                    // Exit the application.
                    System.out.println("\n" + messages.getString("thank_you"));
                    exit = true;
                }
                case 10 -> saveOrderHistoryAsText(scanner, orderHistory, messages);
                case 11 -> loadOrderHistoryFromText(scanner, orderHistory, messages);
                default -> System.out.println(messages.getString("invalid_choice")); // Handle invalid menu choices.
            }
        }

        // Display a goodbye message before the application exits.
        System.out.println(messages.getString("goodbye"));
    }

    // Displays the language selection menu to the user.
    private static void printLanguageSelectionMenu() {
        System.out.println("============================================");
        System.out.println("Select a language:");
        // List of supported languages.
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

    // Prompts the user to select a language and ensures the input is valid.
    private static int getValidLanguageChoice(Scanner scanner) {
        int languageChoice = -1;
        while (languageChoice < 1 || languageChoice > LANGUAGE_COUNT) {
            System.out.print("Please enter a number between 1 and " + LANGUAGE_COUNT + ": ");
            if (scanner.hasNextInt()) {
                languageChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character.
                if (languageChoice < 1 || languageChoice > LANGUAGE_COUNT) {
                    System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input.
            }
        }
        return languageChoice;
    }

    // Maps the user's language choice to a Locale object, which represents the language and region.
    // This helps in loading the correct translations for the application.
    private static Locale getLocaleForLanguageChoice(int languageChoice) {
        Map<Integer, Locale> localeMap = Map.of(
            1, Locale.ENGLISH,
            2, Locale.of("pt", "PT"), // Portuguese
            3, Locale.of("fr", "FR"), // French
            4, Locale.of("it", "IT"), // Italian
            5, Locale.of("es", "ES"), // Spanish
            6, Locale.GERMAN,
            7, Locale.CHINA, // Simplified Chinese
            8, Locale.of("ru", "RU"), // Russian
            9, Locale.of("no", "NO"), // Norwegian
            10, Locale.JAPAN // Japanese
        );
        return localeMap.getOrDefault(languageChoice, Locale.ENGLISH); // Default to English if the choice is invalid.
    }

    // Loads the appropriate resource bundle (translations) for the selected language.
    // If the resource bundle is missing, it falls back to English.
    private static ResourceBundle loadResourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle("com.restaurant.messages", locale);
        } catch (MissingResourceException e) {
            System.err.println("Missing resource bundle for locale: " + locale + ". Falling back to English.");
            return ResourceBundle.getBundle("com.restaurant.messages", Locale.ENGLISH);
        }
    }

    // Displays a welcome message in the selected language.
    // The message includes the restaurant's name and tagline.
    private static void printWelcomeMessage(ResourceBundle messages) {
        System.out.println();
        System.out.println("============================================");
        // Replace the restaurant name in the welcome message with the actual name.
        System.out.println(messages.getString("welcome").replace("Sabor Brasileiro", "Galway To São Paulo Restaurant"));
        System.out.println(messages.getString("tagline")); // Display the restaurant's tagline.
        System.out.println("============================================\n");
    }

    // Displays the main menu options to the user.
    // Each option corresponds to a specific feature of the application.
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
        System.out.println("10. Save Order History as Text");
        System.out.println("11. Load Order History from Text");
        System.out.print(messages.getString("enter_choice"));
    }

    // Gets a valid menu choice from the user.
    // Ensures that the input is a valid number corresponding to a menu option.
    private static int getValidMenuChoice(Scanner scanner, ResourceBundle messages) {
        int choice = -1;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character.
        } else {
            System.out.println(messages.getString("invalid_input")); // Display an error message for invalid input.
            scanner.nextLine(); // Consume the invalid input.
        }
        return choice;
    }

    // Method to save the order history to a file.
    // Prompts the user for a file name and saves the history in that file.
    private static void saveOrderHistory(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_save"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.saveToFile(filePath);
    }

    // Method to load the order history from a file.
    // Prompts the user for a file name and loads the history from that file.
    private static void loadOrderHistory(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_load"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.loadFromFile(filePath);
    }

    // Method to process a new order.
    // This includes selecting dishes, calculating discounts, and finalizing the order.
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        String formattedTimestamp = dateFormat.format(new Date());
        System.out.println("\n" + messages.getString("order_timestamp") + " " + formattedTimestamp);

        Map<String, Long> dishCounts = order.getDishes().stream()
            .collect(Collectors.groupingBy(Dish::name, Collectors.counting()));

        CountDownLatch latch = new CountDownLatch(dishCounts.size());
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<String> preparationLogs = Collections.synchronizedList(new ArrayList<>());
        try {
            for (Map.Entry<String, Long> entry : dishCounts.entrySet()) {
                String dishName = entry.getKey();
                long count = entry.getValue();
                executor.submit(() -> {
                    try {
                        String preparingMessage = String.format(
                            messages.getString("preparing_dish"), dishName
                        );
                        preparationLogs.add(preparingMessage + " (" + count + ")");
                        Thread.sleep(1000 + random.nextInt(2000));
                        String preparedMessage = String.format(
                            messages.getString("prepared_dish"), dishName
                        );
                        preparationLogs.add(preparedMessage + " (" + count + ")");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(messages.getString("preparation_interrupted"));
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            preparationLogs.forEach(System.out::println);
            System.out.println("\n" + messages.getString("all_dishes_prepared"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(messages.getString("preparation_interrupted"));
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        try (ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor()) {
            Future<String> processingTask = singleThreadExecutor.submit(() -> {
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


    // Method to book an event.
    // Collects event details such as name, date, time, and guest count.
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
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault());
                LocalDate enteredDate = LocalDate.parse(eventDate, formatter);
                if (enteredDate.isAfter(LocalDate.now())) {
                    break;
                } else {
                    System.out.println(messages.getString("invalid_event_date_future"));
                }
            } catch (DateTimeParseException e) {
                System.out.println(messages.getString("invalid_event_date_format"));
            }
        }

        System.out.print(messages.getString("enter_event_time"));
        String eventTime;
        while (true) {
            eventTime = scanner.nextLine().trim();
            if (eventTime.isEmpty()) {
                System.out.println(messages.getString("invalid_event_time_format"));
                continue;
            }
            try {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault());
                LocalTime.parse(eventTime, timeFormatter);
                break; // Valid time format
            } catch (DateTimeParseException e) {
                System.out.println(messages.getString("invalid_event_time_format"));
            }
        }

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

        String yesLocalized = messages.getString("yes").toLowerCase();
        if (confirmation.equals(yesLocalized)) {
            System.out.println(messages.getString("event_booking_success"));
        } else {
            System.out.println(messages.getString("event_booking_cancelled"));
        }
    }

    // Method to recommend dishes based on order history.
    // Uses AI-powered logic to suggest the most frequently ordered dishes.
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

    private static void saveOrderHistoryAsText(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_save"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.saveAsText(filePath);
    }

    private static void loadOrderHistoryFromText(Scanner scanner, OrderHistory orderHistory, ResourceBundle messages) {
        System.out.print(messages.getString("enter_file_load"));
        String fileName = scanner.nextLine();
        Path filePath = Paths.get(fileName);
        orderHistory.loadFromText(filePath);
    }
}
