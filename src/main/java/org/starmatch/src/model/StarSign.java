package org.starmatch.src.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an astrological star sign with a unique ID, name, element, and associated traits.
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public class StarSign implements HasId{
    private Integer id;
    private String starName;
    private Element element;
    private List<Trait> traits;

    /**
     * Constructs a StarSign with the specified name, element, traits, and ID.
     *
     * @param starName the name of the star sign (e.g., "Aries", "Taurus")
     * @param element  the element associated with the star sign (e.g., Fire, Earth)
     * @param traits   a list of traits associated with the star sign
     * @param id       the unique identifier for the star sign
     */
    public StarSign(String starName, Element element, List<Trait> traits, Integer id) {
        this.starName = starName;
        this.element = element;
        this.traits = traits;
        this.id = id;
    }

    /**
     * Gets the name of this star sign.
     *
     * @return the name of the star sign
     */
    public String getStarName() {
        return starName;
    }

    /**
     * Sets the name of this star sign.
     *
     * @param starName the new name for this star sign
     */
    public void setStarName(String starName) {
        this.starName = starName;
    }

    /**
     * Gets the element associated with this star sign.
     *
     * @return the element of the star sign
     */
    public Element getElement() {
        return element;
    }

    /**
     * Sets the element associated with this star sign.
     *
     * @param element the new element for this star sign
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the list of traits associated with this star sign.
     *
     * @return a list of traits
     */
    public List<Trait> getTraits() {
        return traits;
    }

    /**
     * Sets the list of traits for this star sign.
     *
     * @param traits the new list of traits for this star sign
     */
    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    /**
     * Returns the unique ID of this star sign.
     *
     * @return the ID of the star sign
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique ID for this star sign.
     *
     * @param id the new ID for the star sign
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns a string representation of this star sign, including its ID, name, element, and traits.
     *
     * @return a string representation of the star sign
     */
    @Override
    public String toString() {
        return "StarSign{" +
                "id=" + id +
                ", starName='" + starName + '\'' +
                ", element=" + element +
                ", traits=" + traits +
                '}';
    }

    /**
     * Converts the Trait object to a CSV line.
     *
     * @return the CSV representation of this Trait object.
     */
    @Override
    public String convertObjectToLine() {
        String traitsString = traits.stream()
                .map(Trait::getTraitName)
                .collect(Collectors.joining(","));
        return id + "," + starName + "," + element + "," + traitsString;
    }

    /**
     * Creates a StarSign object from a CSV line.
     *
     * @param fields the CSV fields to create the object
     * @return the StarSign object created from the fields
     */
    public static HasId createObjectFromFields(String[] fields) {
        Integer id = Integer.parseInt(fields[0]);
        String starName = fields[1];
        Element element = Element.valueOf(fields[2]);
        List<Trait> traits = Arrays.stream(fields, 3, fields.length)
                .map(traitName -> new Trait(element, traitName.trim(), null)) // Trim to avoid spacing issues
                .collect(Collectors.toList());

        return new StarSign(starName, element, traits, id);
    }
}
