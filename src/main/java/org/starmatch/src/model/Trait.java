package org.starmatch.src.model;

/**
 * Represents a trait associated with an element and a unique identifier.
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public class Trait implements HasId {
    private Integer id;
    private Element element;
    private String traitName;

    /**
     * Constructs a Trait with the specified element, name, and ID.
     *
     * @param element   the element associated with this trait (e.g., Fire, Water)
     * @param traitName the name of the trait
     * @param id        the unique identifier for this trait
     */
    public Trait(Element element, String traitName, Integer id) {
        this.element = element;
        this.traitName = traitName;
        this.id = id;
    }

    /**
     * Gets the element associated with this trait.
     *
     * @return the element of the trait
     */
    public Element getElement() {
        return element;
    }

    /**
     * Sets the element for this trait.
     *
     * @param element the new element for the trait
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the name of this trait.
     *
     * @return the name of the trait
     */
    public String getTraitName() {
        return traitName;
    }

    /**
     * Sets the name for this trait.
     *
     * @param traitName the new name for this trait
     */
    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    /**
     * Returns the unique ID of this trait.
     *
     * @return the ID of the trait
     */
    @Override
    public String toString() {
        return "Trait{id=" + id +
                ", element=" + element +
                ", traitName='" + traitName + '\'' +
                '}';
    }

    /**
     * Returns a string representation of this trait, including its element and name.
     *
     * @return a string representation of the trait
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Converts the Trait object to a CSV line.
     *
     * @return the CSV representation of this Trait object.
     */
    @Override
    public String convertObjectToLine() {
        return id + "," + element + "," + traitName;
    }

    /**
     * Creates a Trait object from a CSV line.
     *
     * @param fields the CSV fields to create the object
     * @return the Trait object created from the fields
     */
    public static HasId createObjectFromFields(String[] fields) {
        Integer id = Integer.parseInt(fields[0]);
        Element element = Element.valueOf(fields[1]);
        String traitName = fields[2];
        return new Trait(element, traitName, id);
    }
}
