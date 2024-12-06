package org.example.backend_SSH.recipe_matching_logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.example.backend_SSH.ingredient_logic.Ingredient;
import org.example.backend_SSH.ingredient_logic.TenantIngredients;
import org.example.recipes_system.LoadRecipes;
import org.example.recipes_system.Recipe;

public class RecipeRanking {

    public static List<RankedRecipe> rankRecipes(int tenantId) {
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        List<Ingredient> userIngredients = tenantIngredients.getIngredients();
        LoadRecipes recipeLoader = new LoadRecipes();
        Recipe[] recipes = recipeLoader.ReturnRecipes();

        List<RankedRecipe> rankedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            double qualityScore = calculateRecipeQuality(recipe, userIngredients);
            if (qualityScore >= 0) {
                rankedRecipes.add(new RankedRecipe(recipe, qualityScore));
            }
        }

        rankedRecipes.sort(Comparator.comparingDouble(RankedRecipe::getQualityScore).reversed());

        return rankedRecipes;
    }

    private static double calculateRecipeQuality(Recipe recipe, List<Ingredient> userIngredients) {
        String[] recipeIngredients = recipe.getIngredients();
        int[] requiredQuantities = recipe.getQuantities();
    
        double totalQuality = 0;
        int matchedCount = 0;
        int totalIngredients = recipeIngredients.length;
    
        for (int i = 0; i < recipeIngredients.length; i++) {
            String requiredIngredient = recipeIngredients[i];
            int requiredQuantity = requiredQuantities[i];
    
            Ingredient userIngredient = findUserIngredient(requiredIngredient, userIngredients);
            if (userIngredient != null && userIngredient.getQuantity() >= requiredQuantity) {
                totalQuality += userIngredient.getQuality();
                matchedCount++;
            }
        }
    
        double proportionMatched = (double) matchedCount / totalIngredients;
    
        // thresholds based on the number of ingredients.
        double threshold = totalIngredients <= 3 ? 0.5 : 
                           totalIngredients <= 6 ? 0.6 : 
                           0.7; // recipes with many ingredients require at least 70% match.
    
        
        if (proportionMatched >= threshold) {
            // calc average quality for matched ingredients.
            return totalQuality / matchedCount * proportionMatched; // weight by proportion matched.
        }
        return 0; 
    }
    

    private static Ingredient findUserIngredient(String ingredientName, List<Ingredient> userIngredients) {
        for (Ingredient ingredient : userIngredients) {
            if (ingredient.getName().equalsIgnoreCase(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }
    

    public static void displayRankedRecipes(int tenantId) {
        List<RankedRecipe> rankedRecipes = rankRecipes(tenantId);

        if (rankedRecipes.isEmpty()) {
            System.out.println("No recipes can be made with the available ingredients.");
        } else {
            System.out.println("Ranked recipes for Tenant ID " + tenantId + ":");
            for (RankedRecipe rankedRecipe : rankedRecipes) {
                System.out.println("Recipe: " + rankedRecipe.getRecipe().getName() + 
                                   ", Quality Score: " + rankedRecipe.getQualityScore());
                System.out.println("Instructions: " + rankedRecipe.getRecipe().getInstructions());
            }
        }
    }

    
} 
