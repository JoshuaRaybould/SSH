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

    public Ingredient() {
    }

    public double calculateQuality() {
        if (capturedDate == null) {
            throw new IllegalStateException("Captured date is not set for quality calculation.");
        }

        long daysSinceCaptured = ChronoUnit.DAYS.between(capturedDate, LocalDate.now());
        if (daysSinceCaptured > estimatedShelfLife) {
            return 0.0;
        }

        return Math.max(0.0, 1.0 - ((double) daysSinceCaptured / estimatedShelfLife));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getQuality() {
        return quality;
    }

    public void setCapturedDate(LocalDate capturedDate) {
        this.capturedDate = capturedDate;
    }

    public LocalDate getCapturedDate() {
        return capturedDate;
    }

    public void setEstimatedShelfLife(int estimatedShelfLife) {
        this.estimatedShelfLife = estimatedShelfLife;
    }
}
