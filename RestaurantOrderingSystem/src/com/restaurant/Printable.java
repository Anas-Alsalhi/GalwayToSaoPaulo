package com.restaurant;

/**
 * Interface for classes that can print their details.
 * Provides default and static methods for additional utilities.
 */
public interface Printable {

    /**
     * Prints the details of the implementing class.
     */
    void printDetails();

    /**
     * Prints a formatted header for output.
     *
     * @param header The header text to display.
     */
    default void printHeader(String header) {
        System.out.println("==== " + header + " ====");
    }

    /**
     * Prints a standardized footer.
     */
    static void printFooter() {
        System.out.println("========================");
    }
}
