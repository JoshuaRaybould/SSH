package org.example;

import java.util.ArrayList;
import java.util.List;


public class RecipeRanking {

    public static List<RankedRecipe> rankRecipes(int tenantId) {
        
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        List<Ingredient> userIngredients = tenantIngredients.getIngredients();
        LoadRecipes recipeLoader = new LoadRecipes();
        ArrayList<Recipe> recipes = recipeLoader.getRecipesfromJSON();
    
        List<RankedRecipe> rankedRecipes = new ArrayList<>();
    
        for (Recipe recipe : recipes) {
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
    
            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients);
    
            double proportionMatched = (double) availableIngredients.size() / recipe.getIngredients().length;
            double threshold = recipe.getThreshold();
    
            if (proportionMatched >= threshold) {
                rankedRecipes.add(new RankedRecipe(recipe, qualityScore, availableIngredients, missingIngredients));
            }
        }
    
        // Sort ranked recipes by quality score
        rankedRecipes.sort((r1, r2) -> Double.compare(r2.getQualityScore(), r1.getQualityScore()));
    
        return rankedRecipes;
    }
    

    private static double calculateRecipeQuality(Recipe recipe, List<Ingredient> userIngredients,
                                             List<String> availableIngredients, List<String> missingIngredients) {
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
            availableIngredients.add(requiredIngredient);
            totalQuality += userIngredient.getQuality();
            matchedCount++;
        } else {
            // add to missingIngredients only if it's not already there
            if (!missingIngredients.contains(requiredIngredient)) {
                missingIngredients.add(requiredIngredient);
            }
        }
    }

    double proportionMatched = (double) matchedCount / totalIngredients;

    // thresholds for different recipe sizes.
    double threshold = recipe.getThreshold();

    if (proportionMatched >= threshold) {
        return (totalQuality / matchedCount) * proportionMatched; 
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
        // Get user's ingredients
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        List<Ingredient> userIngredients = tenantIngredients.getIngredients();
    
        // Load and rank recipes
        LoadRecipes recipeLoader = new LoadRecipes();
        ArrayList<Recipe> recipes = recipeLoader.getRecipesfromJSON();
        List<RankedRecipe> rankedRecipes = rankRecipes(tenantId); // Use rankRecipes for this logic
    
        // Display recipes the user can make
        System.out.println("Recipes the user can make:");
        System.out.println("------------------------------------------------");
        boolean anyCanMakeRecipes = false;
        for (RankedRecipe rankedRecipe : rankedRecipes) {
            double proportionMatched = (double) rankedRecipe.getAvailableIngredients().size() / rankedRecipe.getRecipe().getIngredients().length;
            double threshold = rankedRecipe.getRecipe().getThreshold();
    
            if (proportionMatched >= threshold) {
                anyCanMakeRecipes = true;
                System.out.println("------------------------------------------------");
                System.out.println("Recipe: " + rankedRecipe.getRecipe().getName());
                System.out.println("Quality Score: " + rankedRecipe.getQualityScore());
                System.out.println("Proportion Matched: " + proportionMatched);
                System.out.println("Threshold: " + threshold);
                System.out.println("Available Ingredients: " + String.join(", ", rankedRecipe.getAvailableIngredients()));
                System.out.println("Missing Ingredients: " + String.join(", ", rankedRecipe.getMissingIngredients()));
            }
        }
    
        if (!anyCanMakeRecipes) {
            System.out.println("No recipes the user can make.");
        }
    
        // Display missing recipes (the user can't make)
        System.out.println("------------------------------------------------");
        System.out.println("Missing recipes the user can't make:");
        System.out.println("------------------------------------------------");
        boolean anyMissingRecipes = false;
        for (Recipe recipe : recipes) {
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
    
            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients);
    
            double proportionMatched = (double) availableIngredients.size() / recipe.getIngredients().length;
            double threshold = recipe.getThreshold();
    
            if (proportionMatched < threshold) {
                anyMissingRecipes = true;
                System.out.println("------------------------------------------------");
                System.out.println("Recipe: " + recipe.getName());
                System.out.println("Quality Score: " + qualityScore);
                System.out.println("Proportion Matched: " + proportionMatched);
                System.out.println("Threshold: " + threshold);
                System.out.println("Available Ingredients: " + String.join(", ", availableIngredients));
                System.out.println("Missing Ingredients: " + String.join(", ", missingIngredients));
            }
        }
    
        if (!anyMissingRecipes) {
            System.out.println("No missing recipes the user can't make.");
        }
        
        System.out.println("------------------------------------------------");
    }
    
    
    
    
    
    
}

class RankedRecipe {
    private final Recipe recipe;
    private final double qualityScore;
    private final List<String> availableIngredients;
    private final List<String> missingIngredients;

    public RankedRecipe(Recipe recipe, double qualityScore, List<String> availableIngredients, List<String> missingIngredients) {
        this.recipe = recipe;
        this.qualityScore = qualityScore;
        this.availableIngredients = availableIngredients;
        this.missingIngredients = missingIngredients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public List<String> getAvailableIngredients() {
        return availableIngredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }
}
