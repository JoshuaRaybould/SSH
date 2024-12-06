package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TenantIngredientsTest {
    
    @Test
    public void testGetTestUserIngredients(){
        int tenantId = 1; // The id of our test user
        MemoryIngredients output = new MemoryIngredients();
        App.printTenantIngredients(output, tenantId);

        List<String> ingedientStrings = output.getIngredientStrings();
        
        // Check we get the tenant's name correctly
        assertEquals("Tenant with id 1, Jamal, has the following ingedients", output.getTenantString());

        // Check we get the ingredients (for the test user) correctly
        assertEquals("Name:Milk, Quantity:1000, Quality:0.0", ingedientStrings.get(0));
        assertEquals("Name:Jam, Quantity:500, Quality:0.0", ingedientStrings.get(6));
        assertEquals("Name:Paprika, Quantity:500, Quality:0.0", ingedientStrings.get(ingedientStrings.size()-1));

        /*Expected output is:
        Tenant with id 1, Jamal, has the following ingedients
        Name:Milk, Quantity:1000, Quality:0.0
        Name:Eggs, Quantity:12, Quality:0.0
        Name:Bacon, Quantity:6, Quality:0.0
        Name:Sweetcorn, Quantity:3, Quality:0.0
        Name:Salad, Quantity:500, Quality:0.0
        Name:Honey, Quantity:500, Quality:0.0
        Name:Jam, Quantity:500, Quality:0.0
        Name:Sugar, Quantity:1000, Quality:0.0
        Name:Rice, Quantity:2000, Quality:0.0
        Name:Bread, Quantity:1, Quality:0.0
        Name:Oil, Quantity:2000, Quality:0.0
        Name:Salt, Quantity:500, Quality:0.0
        Name:Black Pepper, Quantity:500, Quality:0.0
        Name:Paprika, Quantity:500, Quality:0.0 

        We just test the first last and some middle value is correct.
        */

    }

    @Test
    public void sanityCheckGeneartedUser(){ // This will test a couple users provided they exist (for now this will pass but do nothing meaningful)
        int[] tenantIds = new int[]{2, 3, 4};

        for (int tenantId : tenantIds) {
            MemoryIngredients output = new MemoryIngredients();
            App.printTenantIngredients(output, tenantId);
            if (output.getTenantString().equals("")) break; // The tenant doesn't exist, and so no others can exist after this
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
        App.printTenantIngredients(output, tenantId);

        List<Ingredient> ingedients = output.getIngredients();
        
        // Check we fail appropriately
        assertEquals("", output.getTenantString());
        assertEquals(0, ingedients.size());

    }

}

