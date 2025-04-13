module RestaurantOrderingSystem {
    exports com.restaurant;
    requires java.logging; // Ensure this is used; remove if unnecessary
    requires java.sql;     // Ensure this is used; remove if unnecessary
    requires java.desktop; // Add if GUI components are used
}