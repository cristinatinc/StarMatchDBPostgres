package org.starmatch.src.model;

import java.util.List;

/**
 * Represents a natal chart, which is an astrological chart mapping the positions of planets at the time of a person's birth.
 */
public class NatalChart {
    private List<Planet> planets;

    /**
     * Constructs a NatalChart with the specified list of planets.
     *
     * @param planets the list of planets and their positions in the natal chart
     */
    public NatalChart(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Gets the list of planets in this natal chart.
     *
     * @return the list of planets in the natal chart
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    /**
     * Sets the list of planets for this natal chart.
     *
     * @param planets the new list of planets to set for this natal chart
     */
    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Returns a string representation of this natal chart, including its list of planets.
     *
     * @return a string representation of the natal chart
     */
    @Override
    public String toString() {
        return "NatalChart{" +
                "planets=" + planets +
                '}';
    }
}
