package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Ingredient {
    private String name;
    private LocalDate capturedDate;
    private int estimatedShelfLife;
    private int quantity;
    private double quality;

    public Ingredient(String name, LocalDate capturedDate, int estimatedShelfLife) {
        this.name = name;
        this.capturedDate = capturedDate;
        this.estimatedShelfLife = estimatedShelfLife;
    }

    public Ingredient(String name, int quantity, double quality) {
        this.name = name;
        this.quantity = quantity;
        this.quality = quality;
    }

    public double calculateQuality() {
        long daysSinceCaptured = ChronoUnit.DAYS.between(capturedDate, LocalDate.now()); // testing in days
        //long shelfLifeInMinutes = estimatedShelfLife * 1440L; // using days

        if (daysSinceCaptured > estimatedShelfLife) {
            return 0.0;
        }

        return Math.max(0.0, 1.0 - ((double) daysSinceCaptured / estimatedShelfLife));
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getQuality() {
        return quality;
    }
}
