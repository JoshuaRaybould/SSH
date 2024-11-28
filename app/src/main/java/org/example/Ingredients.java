import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Ingredients {
    private String name;
    private LocalDate capturedDate;
    private int estimatedShelfLife;


    public Ingredients(String name, LocalDate capturedDate, int estimatedShelfLife){
        this.name = name;
        this.capturedDate = capturedDate;
        this.estimatedShelfLife = estimatedShelfLife;
    }


   
}