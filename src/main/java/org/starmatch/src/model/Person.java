package org.starmatch.src.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a person with an ID, name, birth date, birth time, and birth place.
 * This is an abstract class and can be extended to represent specific types of people in various contexts.
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public abstract class Person implements org.starmatch.src.model.HasId {
    protected int id;
    protected String name;
    protected LocalDate birthDate;
    protected LocalTime birthTime;
    protected String birthPlace;

    /**
     * Constructs a Person instance with the specified details.
     *
     * @param id        the unique identifier for the person
     * @param name      the name of the person
     * @param birthDate the birth date of the person
     * @param birthTime the birth time of the person
     * @param birthPlace the place of birth of the person
     */
    protected Person(int id, String name, LocalDate birthDate, LocalTime birthTime, String birthPlace) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.birthTime = birthTime;
        this.birthPlace = birthPlace;
    }

    /**
     * Gets the unique ID of this person.
     *
     * @return the ID of the person
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID for this person.
     *
     * @param id the new ID for the person
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of this person.
     *
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this person.
     *
     * @param name the new name for the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the birth date of this person.
     *
     * @return the birth date of the person
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of this person.
     *
     * @param birthDate the new birth date for the person
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the birth time of this person.
     *
     * @return the birth time of the person
     */
    public LocalTime getBirthTime() {
        return birthTime;
    }

    /**
     * Sets the birth time of this person.
     *
     * @param birthTime the new birth time for the person
     */
    public void setBirthTime(LocalTime birthTime) {
        this.birthTime = birthTime;
    }

    /**
     * Gets the birth place of this person.
     *
     * @return the birth place of the person
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the birth place of this person.
     *
     * @param birthPlace the new birth place for the person
     */
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

}
