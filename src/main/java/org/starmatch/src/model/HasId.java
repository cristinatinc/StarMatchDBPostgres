package org.starmatch.src.model;

import java.io.Serializable;

/**
 * A functional interface that represents an object with a unique identifier.
 */
public interface HasId extends Serializable {
    /**
     * Gets the unique identifier of the object.
     *
     * @return The unique identifier.
     */
    Integer getId();

    /**
     * Converts the object to a CSV line format.
     * <p>
     * This method should return a string representation of the object in CSV format, which can be used
     * for exporting the object's data into a CSV file or for serialization purposes.
     * </p>
     *
     * @return A string representing the object in CSV line format.
     */
    String convertObjectToLine(); // Converts object to CSV line

    /**
     * Creates an object from an array of fields.
     * <p>
     * This static method should be implemented by each class that implements this interface to
     * provide a way to create an instance of the object from a given array of strings (e.g., from a CSV file).
     * </p>
     *
     * @param fields An array of strings representing the fields to create the object.
     * @return A new instance of the object created using the given fields.
     */
    static HasId createObjectFromFields(String[] fields) {
        return null; // To be overridden by each implementing class
    }
}

