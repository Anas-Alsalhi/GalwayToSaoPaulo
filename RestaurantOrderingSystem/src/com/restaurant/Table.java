package com.restaurant;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a table in the restaurant.
 */
public class Table implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int tableNumber;
    private final int capacity;

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

    public int getTableNumber() {
        return tableNumber;
    }

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
