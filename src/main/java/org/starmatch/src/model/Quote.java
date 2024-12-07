package org.starmatch.src.model;

/**
 * Represents a quote associated with a specific elemental attribute (e.g., Fire, Water, Air, Earth).
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public class Quote implements HasId {
    private Element element;
    private String quoteText;
    private Integer id;

    /**
     * Constructs a Quote with the specified ID, elemental association, and text.
     *
     * @param id        the unique identifier for the quote
     * @param element   the element associated with the quote (e.g., Fire, Water)
     * @param quoteText the text of the quote
     */
    public Quote(Integer id, Element element, String quoteText) {
        this.element = element;
        this.quoteText = quoteText;
        this.id = id;
    }

    /**
     * Gets the element associated with this quote.
     *
     * @return the element of the quote
     */
    public Element getElement() {
        return element;
    }

    /**
     * Sets the element associated with this quote.
     *
     * @param element the new element for this quote
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Gets the text of this quote.
     *
     * @return the text of the quote
     */
    public String getQuoteText() {
        return quoteText;
    }

    /**
     * Sets the text for this quote.
     *
     * @param quoteText the new text for this quote
     */
    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    /**
     * Returns the unique ID of this quote.
     *
     * @return the ID of the quote
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique ID for this quote.
     *
     * @param id the new ID for the quote
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns a string representation of this quote, including its ID, element, and text.
     *
     * @return a string representation of the quote
     */
    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", element=" + element +
                ", quoteText='" + quoteText + '\'' +
                '}';
    }

    /**
     * Converts the Trait object to a CSV line.
     *
     * @return the CSV representation of this Trait object.
     */
    @Override
    public String convertObjectToLine() {
        return id + "," + element + "," + quoteText;
    }

    /**
     * Creates a Quote object from a CSV line.
     *
     * @param fields the CSV fields to create the object
     * @return the Quote object created from the fields
     */
    public static HasId createObjectFromFields(String[] fields) {
        Integer id = Integer.parseInt(fields[0]);
        Element element = Element.valueOf(fields[1]);
        String quoteText = fields[2];
        return new Quote(id, element, quoteText);
    }
}
