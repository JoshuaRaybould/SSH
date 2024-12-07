package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TenantIngredientsTest { // These tests rely on the database and so I'm not sure they class as unit tests exactly but are still useful checks to have

    @Test
    public void sanityCheckGeneartedUser(){ // This will test a couple users provided they exist (for now this will pass but do nothing meaningful)
        int[] tenantIds = new int[]{2, 3, 4};

        for (int tenantId : tenantIds) {
            MemoryIngredients output = new MemoryIngredients();
            RecipesApp.printTenantIngredients(output, tenantId);
            if (output.getTenantName().equals("")) break; // The tenant doesn't exist, and so no others can exist after this
            List<Ingredient> ingredients = output.getIngredients();

            assertTrue(ingredients.size() > 0); // All generated users in the database should have at least a few ingredients
            for (Ingredient ingredient: ingredients) {
                // Quality should be a value between 0 and 1
                assertTrue(ingredient.getQuality() >= 0.0 && ingredient.getQuality() <= 1.0); 

                // Quantity should be greater than 0 (or it shouldnt even appear in their ingredients).
                // We may add a more in depth check later, but this would require a new ingredients class with all the ingredients and their types so we know what a reasonable quantity is.
                assertTrue(ingredient.getQuantity() > 0); 
                
                // idea for one more statement, assert true that we find the ingredient in the list of all ingredients (unsure if this is relevant at all)
            }
        }

    }

    @Test
    public void testNonExistantUser(){
        int tenantId = 1000; // The id of a nonexistant user
        MemoryIngredients output = new MemoryIngredients();
        RecipesApp.printTenantIngredients(output, tenantId);

        List<Ingredient> ingedients = output.getIngredients();
        
        // Check we fail appropriately
        assertEquals("", output.getTenantName());
        assertEquals(0, ingedients.size());

    }

}

