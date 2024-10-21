package edu.upenn.cit594.util;

public class Property {
    private double marketValue;
    private double totalLivableArea;
    private int zipCode;

    public Property(int zipCode, double marketValue, double totalLivableArea) {
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
        this.zipCode = zipCode;
    }

    // Getters
    public double getMarketValue() {
        return this.marketValue;
    }

    public double getTotalLivableArea() {
        return this.totalLivableArea;
    }

    public int getZipCode() {
        return this.zipCode;
    }
}
