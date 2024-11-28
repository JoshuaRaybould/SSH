package org.example;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class IngredientsTest {
    @Test
    void testValidValues() {
        Ingredients ingredient = new Ingredients("Tomato", LocalDate.now().minusDays(25), 50); // 25 days out of 50 shelf life
        assertEquals(0.5, ingredient.calculateQuality(), 0.01); // expect quality to be 50%
    }

    @Test
    void testQualityZero() {
        Ingredients ingredient = new Ingredients("Milk", LocalDate.now().minusDays(10), 5); // exceeded shelf life
        assertEquals(0.0, ingredient.calculateQuality(), 0.01); // Q should be 0
    }

    @Test
    void testFreshIngredient() {
        Ingredients ingredient = new Ingredients("Cheese", LocalDate.now(), 10); //new 
        assertEquals(1.0, ingredient.calculateQuality(), 0.01);
    }

    @Test
    void testNearExpiry() {
        Ingredients ingredient = new Ingredients("Bread", LocalDate.now().minusDays(9), 10); // 9 out of 10 days
        assertTrue(ingredient.calculateQuality() < 1.0 && ingredient.calculateQuality() > 0.0);
    }
}
