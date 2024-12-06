package org.example;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.backend_SSH.ingredient_logic.Ingredient;
import org.junit.jupiter.api.Test;

class IngredientsTest {

    @Test
    void testValidValues() {
        Ingredient ingredient = new Ingredient("Tomato", LocalDate.now().minusDays(25), 50);
        assertEquals(0.5, ingredient.calculateQuality(), 0.01); // Expect 50% quality
    }

    @Test
    void testQualityZero() {
        Ingredient ingredient = new Ingredient("Milk", LocalDate.now().minusDays(10), 5); 
        assertEquals(0.0, ingredient.calculateQuality(), 0.01); // Quality should be 0
    }

    @Test
    void testFreshIngredient() {
        Ingredient ingredient = new Ingredient("Cheese", LocalDate.now(), 10);
        assertEquals(1.0, ingredient.calculateQuality(), 0.01); // Full quality
    }

    @Test
    void testNearExpiry() {
        Ingredient ingredient = new Ingredient("Bread", LocalDate.now().minusDays(9), 10);
        assertTrue(ingredient.calculateQuality() < 1.0 && ingredient.calculateQuality() > 0.0); 
    }
}
