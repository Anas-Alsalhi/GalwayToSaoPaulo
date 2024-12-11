package com.restaurant;

import java.io.Serializable;

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
}
