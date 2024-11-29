package org.example;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class IngredientsTest {

    @Test
    void testValidValues() {
        Ingredients ingredient = new Ingredients("Tomato", LocalDateTime.now().minusDays(25), 50);
        assertEquals(0.5, ingredient.calculateQuality(), 0.01); // Expect 50% quality
    }

    @Test
    void testQualityZero() {
        Ingredients ingredient = new Ingredients("Milk", LocalDateTime.now().minusDays(10), 5); 
        assertEquals(0.0, ingredient.calculateQuality(), 0.01); // Quality should be 0
    }

    @Test
    void testFreshIngredient() {
        Ingredients ingredient = new Ingredients("Cheese", LocalDateTime.now(), 10);
        assertEquals(1.0, ingredient.calculateQuality(), 0.01); // Full quality
    }

    @Test
    void testNearExpiry() {
        Ingredients ingredient = new Ingredients("Bread", LocalDateTime.now().minusDays(9), 10);
        assertTrue(ingredient.calculateQuality() < 1.0 && ingredient.calculateQuality() > 0.0); 
    }
}
