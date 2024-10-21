package edu.upenn.proj1.util;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private int zipCode;
    private int population;

    // Constructors

    public Population (int zipCode, int population) {
        this.zipCode = zipCode;
        this.population = population;
    }

    // Getter

    public int getZipCode() {
        return zipCode;
    }

    public int getPopulation() {
        return population;
    }

    // Setter

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
