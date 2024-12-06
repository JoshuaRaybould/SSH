package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RecipeRanking {

    public static List<RankedRecipe> rankRecipes(int tenantId) {
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        List<Ingredient> userIngredients = tenantIngredients.getIngredients();
        LoadRecipes recipeLoader = new LoadRecipes();
        Recipe[] recipes = recipeLoader.ReturnRecipes();

        List<RankedRecipe> rankedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();

            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients);

            if (qualityScore > 0) {
                rankedRecipes.add(new RankedRecipe(recipe, qualityScore, availableIngredients, missingIngredients));
            }
        }

        rankedRecipes.sort(Comparator.comparingDouble(RankedRecipe::getQualityScore).reversed());

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
    double threshold = totalIngredients <= 3 ? 0.5 :
                       totalIngredients <= 6 ? 0.6 : 
                       0.7;

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
        // get user's ingredients
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        List<Ingredient> userIngredients = tenantIngredients.getIngredients();
    
        // load and rank recipes
        LoadRecipes recipeLoader = new LoadRecipes();
        Recipe[] recipes = recipeLoader.ReturnRecipes();
        List<RankedRecipe> rankedRecipes = new ArrayList<>();
        List<RankedRecipe> allRecipes = new ArrayList<>();
    
        for (Recipe recipe : recipes) {
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
    
            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients);
    
            if (qualityScore > 0) { 
                rankedRecipes.add(new RankedRecipe(recipe, qualityScore, availableIngredients, missingIngredients));
            }
    
            // save all recipes, even if they don't meet the threshold
            allRecipes.add(new RankedRecipe(recipe, qualityScore, availableIngredients, missingIngredients));
        }
    
        // display results
        if (rankedRecipes.isEmpty()) {
            System.out.println("No recipes can be made with the available ingredients.");
            System.out.println("------------------------------------------------");
        } else {
            System.out.println("Ranked recipes the user can make:");
            for (RankedRecipe rankedRecipe : rankedRecipes) {
                double proportionMatched = (double) rankedRecipe.getAvailableIngredients().size() / rankedRecipe.getRecipe().getIngredients().length;
                double threshold = rankedRecipe.getRecipe().getIngredients().length <= 3 ? 0.5 :
                                  rankedRecipe.getRecipe().getIngredients().length <= 6 ? 0.6 : 
                                  0.7;
    
                System.out.println("------------------------------------------------");
                System.out.println("Recipe: " + rankedRecipe.getRecipe().getName());
                System.out.println("Quality Score: " + rankedRecipe.getQualityScore());
                System.out.println("Proportion Matched: " + proportionMatched);
                System.out.println("Threshold: " + threshold);
                System.out.println("Available Ingredients: " + String.join(", ", rankedRecipe.getAvailableIngredients()));
                System.out.println("Missing Ingredients: " + String.join(", ", rankedRecipe.getMissingIngredients()));
                
            }
        }
    
        // display all recipes (for which the user might not have sufficient ingredients)
        System.out.println("------------------------------------------------");
        System.out.println("Details for all recipes:");
        for (RankedRecipe rankedRecipe : allRecipes) {
            double proportionMatched = (double) rankedRecipe.getAvailableIngredients().size() / rankedRecipe.getRecipe().getIngredients().length;
            double threshold = rankedRecipe.getRecipe().getIngredients().length <= 3 ? 0.5 :
                              rankedRecipe.getRecipe().getIngredients().length <= 6 ? 0.6 : 
                              0.7;
    
            System.out.println("------------------------------------------------");
            System.out.println("Recipe: " + rankedRecipe.getRecipe().getName());
            System.out.println("Quality Score: " + rankedRecipe.getQualityScore());
            System.out.println("Proportion Matched: " + proportionMatched);
            System.out.println("Threshold: " + threshold);
            System.out.println("Available Ingredients: " + String.join(", ", rankedRecipe.getAvailableIngredients()));
            System.out.println("Missing Ingredients: " + String.join(", ", rankedRecipe.getMissingIngredients()));
           
        }
        System.out.println("------------------------------------------------");
    }
    
    
}

class RankedRecipe {
    private Recipe recipe;
    private double qualityScore;
    private List<String> availableIngredients;
    private List<String> missingIngredients;

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
