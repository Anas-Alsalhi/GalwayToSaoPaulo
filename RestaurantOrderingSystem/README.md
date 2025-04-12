# Restaurant Ordering System

The Restaurant Ordering System is a console-based application designed to streamline the operations of a restaurant. It supports multiple languages and provides features for managing orders, viewing menus, booking events, and more.

## Features

- Multi-language Support: The application supports 10 languages, including English, Portuguese, French, Italian, Spanish, German, Chinese, Russian, Norwegian, and Japanese.
- Menu Management: View the restaurant's menu categorized by dish type (Appetizers, Main Courses, Desserts, Beverages).
- Daily Specials: Display a selection of daily specials chosen randomly from the menu.
- Order Processing: Place and process orders, including applying discounts, generating order summaries, and saving dish preparation logs.
- Order History: Save, load, and view the history of all orders. History can be saved/loaded as text or binary files.
- Event Booking: Book events with details such as event name, date, time, and guest count.
- AI-Powered Recommendations: Get dish recommendations based on order history.
- Error Handling: Handles invalid inputs, missing resource bundles, and other edge cases gracefully.

## How to Use

1. Language Selection: Upon starting the application, select your preferred language from the list of supported languages.
2. Main Menu: Choose from various options such as viewing the menu, placing an order, or booking an event.
3. Order Management: Add dishes to your order, apply discounts, and finalize the order. Dish preparation logs are saved in the order summary.
4. Event Booking: Provide event details to book an event at the restaurant.
5. Recommendations: View AI-powered dish recommendations based on past orders.
6. Order History: Save or load order history as text or binary files.

## Technologies Used

- Java: The application is implemented in Java.
- Resource Bundles: Used for multi-language support.
- Concurrency: Utilized for simulating dish preparation in parallel.
- Serialization: Used for saving and loading order history.

## Getting Started

1. Clone the repository or download the project files.
2. Open the project in an IDE such as Eclipse or IntelliJ IDEA.
3. Run the `RestaurantApp` class to start the application.

## Project Structure

- `src/com/restaurant`: Contains the main application code, including classes for menu, orders, staff, and more.
- `src/com/restaurant/messages_<locale>.properties`: Language-specific resource files for translations.
- `README.md`: This file, providing an overview of the project.

## License

This project is licensed under the MIT License.
