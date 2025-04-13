package com.restaurant;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a staff member in the restaurant.
 * This is an abstract class that serves as a base for specific staff roles.
 */
public abstract class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name; // Name of the staff member

    /**
     * Default constructor for deserialization.
     */
    public Staff() {
        this.name = "";
    }

    /**
     * Constructor for creating a staff member with a name.
     *
     * @param name The name of the staff member.
     */
    public Staff(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the staff member.
     *
     * @return The name of the staff member.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the staff member.
     *
     * @param name The new name of the staff member.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Abstract method to be implemented by all subclasses to define their duties.
     */
    public abstract void performDuties();

    /**
     * Uses a Consumer to print staff details.
     * @param printer Consumer that accepts a Staff object and prints its details.
     */
    public void printDetails(Consumer<Staff> printer) {
        printer.accept(this);
    }

    /**
     * Uses a Supplier to provide the staff name.
     * @param nameSupplier Supplier that provides the staff name.
     * @return The staff name provided by the supplier.
     */
    public String getName(Supplier<String> nameSupplier) {
        return nameSupplier.get();
    }

    /**
     * Uses a Function to transform the staff name.
     * @param nameTransformer Function that transforms the staff name.
     * @return The transformed staff name.
     */
    public String transformName(Function<String, String> nameTransformer) {
        return nameTransformer.apply(this.name);
    }
}
