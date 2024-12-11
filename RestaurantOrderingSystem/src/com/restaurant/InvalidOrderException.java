package com.restaurant;

/**
 * Custom exception for invalid orders.
 */
public class InvalidOrderException extends Exception {

    // Added serialVersionUID to address the serialization warning
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidOrderException with the specified detail message.
     *
     * @param message The detail message explaining the cause of the exception.
     */
    public InvalidOrderException(String message) {
        super(message);
    }
}
