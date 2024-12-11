package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TenantIngredientsTest { // These tests rely on the database, they are still useful checks to have

    public MemoryIngredients output = new MemoryIngredients();

    @Test
    public void sanityCheckGeneartedUser(){ // This will test a couple users provided they exist
        int[] tenantIds = new int[]{1, 2, 3, 4};

        for (int tenantId : tenantIds) {
            RecipesApp.printTenantIngredients(output, tenantId);
            if (!output.hasSucceeded()) break; // The tenant doesn't exist, and so no others can exist after this
            List<Ingredient> ingredients = output.getIngredients();

            assertTrue(ingredients.size() > 0); // All generated users in the database should have at least a few ingredients
            for (Ingredient ingredient: ingredients) {
                // Quality should be a value between 0 and 1
                assertTrue(ingredient.getQuality() >= 0.0 && ingredient.getQuality() <= 1.0); 

                // Quantity should be between 0 and 1000 at most
                assertTrue(ingredient.getQuantity() >= 0 && ingredient.getQuantity() <= 1000);

                // We may add a more in depth check later, but this would require a new ingredients class with all the ingredients and their types so we know what a reasonable quantity is.              
                // idea for one more statement, assert true that we find the ingredient in the list of all ingredients (unsure if this is relevant at all)
            }
        }

    }

}

