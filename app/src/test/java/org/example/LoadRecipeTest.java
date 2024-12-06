package org.example;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.recipes_system.LoadRecipes;
import org.example.recipes_system.Recipe;
import org.junit.jupiter.api.Test;

public class LoadRecipeTest {

    @Test
    public void testDataTypes(){
        LoadRecipes loadRecipes = new LoadRecipes();

        Recipe[] recipes = loadRecipes.ReturnRecipes();

        Recipe recipe1 = recipes[0];

        assertTrue(recipe1.getName() instanceof String, "Name should be a String");
        
      
        assertTrue(recipe1.getInstructions() instanceof String, "Instructions should be a String");

       
        assertTrue(recipe1.getIngredients() instanceof String[], "Ingredients should be a String array");

        
        assertTrue(recipe1.getQuantities() instanceof int[], "Quantities should be an int array");
    }
}
