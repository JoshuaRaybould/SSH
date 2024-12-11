package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class RecipeRanking {

    public static List<RankedRecipe> rankRecipes(int tenantId) {
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        ArrayList<Ingredient> userIngredients = tenantIngredients.getIngredients();
        LoadRecipes recipeLoader = new LoadRecipes();
        ArrayList<Recipe> recipes = recipeLoader.LoadMatchedRecipes(userIngredients);

        List<RankedRecipe> rankedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
            List<Integer> ingedientQuantities = new ArrayList<>();

            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients, ingedientQuantities);
            double proportionMatched = (double) availableIngredients.size() / recipe.getIngredients().length;
            double threshold = recipe.getThreshold();

            if (proportionMatched >= threshold) {
                rankedRecipes.add(new RankedRecipe(recipe, qualityScore, proportionMatched, availableIngredients, missingIngredients, ingedientQuantities));
            }
        }

        rankedRecipes.sort(Comparator.comparingDouble(RankedRecipe::getQualityScore).reversed());
        rankedRecipes.sort(Comparator.comparingDouble(RankedRecipe::getProportionMatched).reversed());

        return rankedRecipes;
    }

    private static double calculateRecipeQuality(Recipe recipe, List<Ingredient> userIngredients,
                                             List<String> availableIngredients, List<String> missingIngredients, 
                                             List<Integer> ingredientQuantities) {
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
                ingredientQuantities.add(userIngredient.getQuantity());
            } else {
                // add to missingIngredients only if it's not already there
                if (!missingIngredients.contains(requiredIngredient)) {
                    missingIngredients.add(requiredIngredient);
                    ingredientQuantities.add(0);
                }
            }
        }

        double proportionMatched = (double) matchedCount / totalIngredients;

        if (matchedCount > 0) {
            return (totalQuality / matchedCount) * proportionMatched; 
        } else {
            return 0;
        }
    }


    private static Ingredient findUserIngredient(String ingredientName, List<Ingredient> userIngredients) {
        for (Ingredient ingredient : userIngredients) {
            if (ingredient.getName().equalsIgnoreCase(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }

    private static String[] intListToStringArr(List<Integer> intList) {
        int s = intList.size();
        String[] stringArr = new String[s];

        for (int i = 0; i < s; i++) {
            stringArr[i] = String.valueOf(intList.get(i));
        }

        return stringArr;
    }

    public static void displayRankedRecipes(int tenantId) {
        List<RankedRecipe> rankedRecipes = rankRecipes(tenantId);
        System.out.println("Recommended recipes:");
        if (rankedRecipes.isEmpty()) {
            System.out.println("Insufficient ingredients to make any recipes");
        }

        for (RankedRecipe rankRecipe : rankedRecipes) {
            Recipe theRecipe = rankRecipe.getRecipe();
            List<Integer> quantitiesList = Arrays.stream(theRecipe.getQuantities()).boxed().toList();
            String[] recipeQuantities = intListToStringArr(quantitiesList);
            String[] quantities = intListToStringArr(rankRecipe.getUserQuantities());
            System.out.println("------------------------------------------------");
            System.out.println("Recipe: " + theRecipe.getName());
            System.out.println("Ingredients: " + String.join(", ", theRecipe.getIngredients()));
            System.out.println("Required quantities: " + String.join(", ", recipeQuantities));
            System.out.println("Your quantities: " + String.join(", ", quantities));
            System.out.println("Recipe instructions: " + theRecipe.getInstructions());
            System.out.println("Proportion Matched: " + String.format("%.2f", rankRecipe.getProportionMatched()));
            System.out.println("Quality Score: " + String.format("%.2f", rankRecipe.getQualityScore()));
        }
    }

    public static void displayAllRecipesInfo(int tenantId) {
        // Get user's ingredients
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        ArrayList<Ingredient> userIngredients = tenantIngredients.getIngredients();
    
        // Load recipes
        LoadRecipes recipeLoader = new LoadRecipes();
        ArrayList<Recipe> allRecipes = recipeLoader.getRecipesfromJSON();
    
        // Display recipes the user can make
        System.out.println("Recipes the user can make:");
        System.out.println("------------------------------------------------");
        boolean anyCanMakeRecipes = false;
    
        for (Recipe recipe : allRecipes) {
            // Prepare lists for available and missing ingredients
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
            List<Integer> ingredientQuantities = new ArrayList<>();
    
            // Calculate the proportion matched and quality score
            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients, ingredientQuantities);
            double proportionMatched = (double) availableIngredients.size() / recipe.getIngredients().length;
            double threshold = recipe.getThreshold();
    
            // Check if the user can make this recipe (proportion matched >= threshold)
            if (proportionMatched >= threshold) {
                anyCanMakeRecipes = true;
                List<Integer> quantitiesList = Arrays.stream(recipe.getQuantities()).boxed().toList();
                String[] recipeQuantities = intListToStringArr(quantitiesList);
                String[] quantities = intListToStringArr(ingredientQuantities);
    
                // Display recipe details
                System.out.println("------------------------------------------------");
                System.out.println("Recipe: " + recipe.getName());
                System.out.println("Ingredients: " + String.join(", ", recipe.getIngredients()));
                System.out.println("Required quantities: " + String.join(", ", recipeQuantities));
                System.out.println("Your quantities: " + String.join(", ", quantities));
                System.out.println("Recipe instructions: " + recipe.getInstructions());
                System.out.println("Threshold: " + threshold);
                System.out.println("Proportion Matched: " + String.format("%.2f", proportionMatched));
                System.out.println("Quality Score: " + String.format("%.2f", qualityScore));
                System.out.println("Available Ingredients: " + String.join(", ", availableIngredients));
                System.out.println("Missing Ingredients: " + String.join(", ", missingIngredients));
            }
        }
    
        if (!anyCanMakeRecipes) {
            System.out.println("No recipes the user can make.");
        }
    
        // Display recipes the user cannot make
        System.out.println("------------------------------------------------");
        System.out.println("Recipes the user cannot make:");
        System.out.println("------------------------------------------------");
        boolean anyCannotMakeRecipes = false;
    
        for (Recipe recipe : allRecipes) {
            // Prepare lists for available and missing ingredients
            List<String> availableIngredients = new ArrayList<>();
            List<String> missingIngredients = new ArrayList<>();
            List<Integer> ingredientQuantities = new ArrayList<>();
    
            // Calculate the proportion matched and quality score
            double qualityScore = calculateRecipeQuality(recipe, userIngredients, availableIngredients, missingIngredients, ingredientQuantities);
            double proportionMatched = (double) availableIngredients.size() / recipe.getIngredients().length;
            double threshold = recipe.getThreshold();
    
            // Check if the user cannot make this recipe (proportion matched < threshold)
            if (proportionMatched < threshold) {
                anyCannotMakeRecipes = true;
                List<Integer> quantitiesList = Arrays.stream(recipe.getQuantities()).boxed().toList();
                String[] recipeQuantities = intListToStringArr(quantitiesList);
                String[] quantities = intListToStringArr(ingredientQuantities);
    
                // Display recipe details
                System.out.println("------------------------------------------------");
                System.out.println("Recipe: " + recipe.getName());
                System.out.println("Ingredients: " + String.join(", ", recipe.getIngredients()));
                System.out.println("Required quantities: " + String.join(", ", recipeQuantities));
                System.out.println("Your quantities: " + String.join(", ", quantities));
                System.out.println("Recipe instructions: " + recipe.getInstructions());
                System.out.println("Threshold: " + threshold);
                System.out.println("Proportion Matched: " + String.format("%.2f", proportionMatched));
                System.out.println("Quality Score: " + String.format("%.2f", qualityScore));
                System.out.println("Available Ingredients: " + String.join(", ", availableIngredients));
                System.out.println("Missing Ingredients: " + String.join(", ", missingIngredients));
            }
        }
    
        if (!anyCannotMakeRecipes) {
            System.out.println("No recipes the user cannot make.");
        }
    
        System.out.println("------------------------------------------------");
    }
    
    
    
    
}

class RankedRecipe {
    private Recipe recipe;
    private double qualityScore;
    private double proportionMatched;
    private List<String> availableIngredients;
    private List<String> missingIngredients;
    private List<Integer> userQuantities; // A list of the quantities the user has, since the recipe already stores the required quantities

    public RankedRecipe(Recipe recipe, double qualityScore, double proportionMatched, List<String> availableIngredients, 
                                                                List<String> missingIngredients, List<Integer> userQuantities) {
        this.recipe = recipe;
        this.qualityScore = qualityScore;
        this.proportionMatched = proportionMatched;
        this.availableIngredients = availableIngredients;
        this.missingIngredients = missingIngredients;
        this.userQuantities = userQuantities;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public double getProportionMatched() {
        return proportionMatched;
    }

    public List<String> getAvailableIngredients() {
        return availableIngredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    public List<Integer> getUserQuantities() {
        return userQuantities;
    }
}
