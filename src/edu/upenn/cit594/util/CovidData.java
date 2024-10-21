package edu.upenn.cit594.util;

public class CovidData {
    private int zipCode;
    private String timeStamp;
    private int partialVaccination;
    private int fullVaccination;

    public CovidData(int zipCode, int partialVaccination, int fullVaccination, String timeStamp) {
        this.zipCode = zipCode;
        this.timeStamp = timeStamp;
        this.partialVaccination = partialVaccination;
        this.fullVaccination = fullVaccination;
    }

    // Getters
    public int getZipCode() {
        return this.zipCode;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public int getPartialVaccination() {
        return this.partialVaccination;
    }

    public int getFullVaccination() {
        return this.fullVaccination;
    }


}
