package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Ingredients {
    private final String name;
    private final LocalDate capturedDate;
    private final int estimatedShelfLife;

    public Ingredients(String name, LocalDate capturedDate, int estimatedShelfLife) {
        this.name = name;
        this.capturedDate = capturedDate;
        this.estimatedShelfLife = estimatedShelfLife;
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
}
