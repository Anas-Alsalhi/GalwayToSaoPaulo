package com.restaurant;

import java.io.Serializable;

/**
 * Represents a staff member in the restaurant.
 */
public abstract class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

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
}
