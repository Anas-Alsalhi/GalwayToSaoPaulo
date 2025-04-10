package com.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a waiter in the restaurant.
 * A waiter is responsible for serving customers and managing assigned tables.
 */
public final class Waiter extends Staff {

    private int waiterId; // Unique identifier for the waiter
    private List<Table> assignedTables; // List of tables assigned to the waiter

    /**
     * Default constructor for deserialization.
     */
    public Waiter() {
        super();
        this.waiterId = 0;
        this.assignedTables = new ArrayList<>();
    }

    /**
     * Constructor for creating a Waiter.
     *
     * @param name     The name of the waiter.
     * @param waiterId The unique ID of the waiter.
     */
    public Waiter(String name, int waiterId) {
        super(name);
        this.waiterId = waiterId;
        this.assignedTables = new ArrayList<>();
    }

    /**
     * Gets the unique ID of the waiter.
     *
     * @return The waiter ID.
     */
    public int getWaiterId() {
        return waiterId;
    }

    /**
     * Assigns a table to the waiter.
     *
     * @param table The table to assign.
     */
    public void assignTable(Table table) {
        assignedTables.add(table);
    }

    /**
     * Gets the list of tables assigned to the waiter.
     *
     * @return An unmodifiable list of assigned tables.
     */
    public List<Table> getAssignedTables() {
        return Collections.unmodifiableList(assignedTables);
    }

    /**
     * Implementation of duties performed by the waiter.
     * Prints a message describing the waiter's current duties.
     */
    @Override
    public void performDuties() {
        System.out.println(getName() + " (ID: " + waiterId + ") is serving customers and ensuring orders are delivered promptly.");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Waiter waiter = (Waiter) obj;
        return waiterId == waiter.waiterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(waiterId);
    }
}
