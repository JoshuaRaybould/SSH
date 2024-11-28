package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Ingredients {
    private final String name;
    private final LocalDateTime capturedDate;
    private final int estimatedShelfLife;

    public Ingredients(String name, LocalDate capturedDate, int estimatedShelfLife) {
        this.name = name;
        this.capturedDate = capturedDate.atStartOfDay();
        this.estimatedShelfLife = estimatedShelfLife;
    }

    public double calculateQuality() {
        long minutesSinceCaptured = ChronoUnit.MINUTES.between(capturedDate, LocalDateTime.now()); // testing in minutes
        long shelfLifeInMinutes = estimatedShelfLife * 1440L; // convert days to minutes

        if (minutesSinceCaptured > shelfLifeInMinutes) {
            return 0.0;
        }

        return Math.max(0.0, 1.0 - ((double) minutesSinceCaptured / shelfLifeInMinutes));
    }

    public String getName() {
        return name;
    }
}
