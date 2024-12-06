package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.example.backend_SSH.ingredient_logic.MemoryIngredients;
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
    public void testNonExistantUser(){
        int tenantId = 1000; // The id of a nonexistant user
        MemoryIngredients output = new MemoryIngredients();
        App.printTenantIngredients(output, tenantId);

        List<String> ingedientStrings = output.getIngredientStrings();
        
        // Check we fail appropriately
        assertEquals("", output.getTenantString());
        assertEquals(0, ingedientStrings.size());


    }

}

