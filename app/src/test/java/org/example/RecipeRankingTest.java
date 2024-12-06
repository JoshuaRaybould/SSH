package org.example;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.example.backend_SSH.recipe_matching_logic.RankedRecipe;
import org.example.backend_SSH.recipe_matching_logic.RecipeRanking;
import org.junit.jupiter.api.Test;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;



public class RecipeRankingTest {

    @Test
    public void testRankRecipes() {
        /*TenantIngredients mockTenant = mock(TenantIngredients.class);
        when(mockTenant.getIngredients()).thenReturn((ArrayList<Ingredient>) List.of(
            new Ingredient("Chicken", 10, 1.0),
            new Ingredient("Lemon", 5, 1.0),
            new Ingredient("Garlic", 3, 0.8)
        ));*/

        List<RankedRecipe> rankedRecipes = RecipeRanking.rankRecipes(1); // assuming 1 is the tenant id
        assertFalse(rankedRecipes.isEmpty());
        assertEquals("Lemon Chicken", rankedRecipes.get(0).getRecipe().getName());
    }
}
