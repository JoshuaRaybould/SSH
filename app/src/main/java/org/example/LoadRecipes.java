package org.example;

import java.util.ArrayList;

public class LoadRecipes {

    private ArrayList<Ingredient> userIngredients;
    private ArrayList<Recipe> matchedRecipes;
    


    Recipe recipe1 = new Recipe("Lemon Chicken", 
                                "Marinate chicken with lemon juice, minced garlic, olive oil and pepper. Bake or pan-fry until it's golden and cooked through.",
                                new String[] {"chicken","lemon juice","garlic","oil","pepper"},
                                new int[] {1,5,3,10,10});

    Recipe recipe2 = new Recipe("Tomato and Basil Pasta", 
                                "Cook pasta until al dente. Fry off garlic in olive oil and then add tomato puree and pepper. Mix the pasta with the sauce and stir.",
                                new String[] {"pasta","garlic","oil","tomato puree","pepper"},
                                new int[] {500,3,10,7,7});

    Recipe recipe3 = new Recipe("Veg Stir Fry", 
                                "Heat oil in a pan or a wok. Add and stir fry the chopped vegetables until nice and tender. Add soy sauce and any other condiments that you wish to add and serve.",
                                new String[] {"stir fry","broccoli","oil","carrots","pepper", "soy sauce"},
                                new int[] {500,3,10,7,7,15});
        

    public Recipe[] ReturnRecipes(){
        return new Recipe[] {recipe1, recipe2,recipe3};
    }

    public ArrayList<Recipe> LoadMatchedRecipes(int tenant_id){

        TenantIngredients tenant = new TenantIngredients(tenant_id); //creates a tenant based on tenant_id
        userIngredients = new ArrayList<Ingredient>();
        matchedRecipes = new ArrayList<Recipe>();
        userIngredients = tenant.getIngredients();// retrieves tenants ingredients into an array list

        System.out.println("size: " + userIngredients.size());//test case

        Recipe [] currentRecipes = ReturnRecipes(); //returns an array of recipes

        for (Recipe recipes : currentRecipes){ //goes through all the recipes in the current recipes

            boolean Matched = false;
            int count = 0; //used to check if match and then resets when back to 0
            String [] recipe_ingredients = recipes.getIngredients();//stores ingredients of current recipe into an array
            
            for (Ingredient currentIngredient : userIngredients){ //goes through all the users ingredients 

                //System.out.println(currentIngredient);

                for (String each_recipe_ingredient : recipe_ingredients){//goes through each ingredient in recipe

                    if (each_recipe_ingredient.equalsIgnoreCase(currentIngredient.getName())){ //checks if the recipe ingredient matches the user ingredient
                        Matched = true;
                        break;
                    }
                }
                if (Matched){
                    matchedRecipes.add(recipes);
                    break;
                }
            }
        } 
       return matchedRecipes;
    }

    public LoadRecipes() {

    }

}
