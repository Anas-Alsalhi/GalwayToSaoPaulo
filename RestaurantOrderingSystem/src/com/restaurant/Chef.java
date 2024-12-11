package com.restaurant;

import java.io.Serializable;

/**
 * Represents a chef in the restaurant.
 */
public class Chef extends Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    private String specialty;

    /**
     * Default constructor for deserialization.
     */
    public Chef() {
        super();
        this.specialty = "";
    }

    /**
     * Constructor for creating a Chef.
     *
     * @param name      The name of the chef.
     * @param specialty The chef's area of expertise.
     */
    public Chef(String name, String specialty) {
        super(name);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Implementation of duties performed by the chef.
     */
    @Override
    public void performDuties() {
        System.out.println(getName() + " is preparing delicious " + specialty + " dishes in the kitchen.");
    }
}
