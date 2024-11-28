import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Ingredients {
    private String name;
    private LocalDateTime capturedDate;
    private int estimatedShelfLife;


    public Ingredients(String name, LocalDate capturedDate, int estimatedShelfLife){
        this.name = name;
        this.capturedDate = capturedDate.atStartOfDay();
        this.estimatedShelfLife = estimatedShelfLife;
    }

    public double calculateQuality() {
        long daysSinceCaptured = ChronoUnit.MINUTES.between(capturedDate, LocalDate.now()); //in reality this wouldbe days, but for the sake of testing, we will use minutes
        
        if (daysSinceCaptured > estimatedShelfLife * 1440) {
            return 0;
        }
        return Math.max(0, 1 - ((double) daysSinceCaptured / (estimatedShelfLife + 1) * 1440));
    }

    public String getName() {
        return name;
    }
   
}