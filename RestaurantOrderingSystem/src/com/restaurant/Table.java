package com.restaurant;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a table in the restaurant.
 * Each table has a unique number and a seating capacity.
 */
public class Table implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int tableNumber; // Unique table number
    private final int capacity; // Number of seats at the table

    /**
     * Constructor for creating a Table.
     * 
     * @param tableNumber The table number.
     * @param capacity    The number of seats at the table.
     */
    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    /**
     * Gets the table number.
     *
     * @return The table number.
     */
    public int getTableNumber() {
        return tableNumber;
    }

    /**
     * Gets the seating capacity of the table.
     *
     * @return The seating capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Table table = (Table) obj;
        return tableNumber == table.tableNumber && capacity == table.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNumber, capacity);
    }
}
