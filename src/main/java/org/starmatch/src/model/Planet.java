package org.starmatch.src.model;

/**
 * Represents a planet with a unique ID, name, and associated astrological sign.
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public class Planet {
    private Integer id;
    private StarSign sign;
    private String planetName;

    /**
     * Constructs a Planet instance with the specified name, sign, and ID.
     *
     * @param planetName the name of the planet (e.g., "Mars", "Venus")
     * @param sign       the astrological sign associated with the planet
     * @param id         the unique identifier for the planet
     */
    public Planet(String planetName, StarSign sign, Integer id) {
        this.planetName = planetName;
        this.sign = sign;
        this.id = id;
    }

    /**
     * Gets the name of this planet.
     *
     * @return the name of the planet
     */
    public String getPlanetName() {
        return planetName;
    }

    /**
     * Sets the name of this planet.
     *
     * @param planetName the new name for this planet
     */
    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    /**
     * Gets the astrological sign associated with this planet.
     *
     * @return the astrological sign of the planet
     */
    public StarSign getSign() {
        return sign;
    }

    /**
     * Sets the astrological sign associated with this planet.
     *
     * @param sign the new astrological sign for this planet
     */
    public void setSign(StarSign sign) {
        this.sign = sign;
    }

    /**
     * Returns the unique ID of this planet.
     *
     * @return the ID of the planet
     */

    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique ID for this planet.
     *
     * @param id the new ID for the planet
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns a string representation of this planet, including its ID, name, and associated sign.
     *
     * @return a string representation of the planet
     */
    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", sign=" + sign +
                ", planetName='" + planetName + '\'' +
                '}';
    }
}
