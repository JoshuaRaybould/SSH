package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class LoadRecipes {

    
    public ArrayList<Recipe> getRecipesfromJSON(){

        ArrayList<Recipe> recipeList = new ArrayList<>();

        try {

            //reads the entire json recipe file and puts it into string format
            Path jsonFile = Paths.get("database/recipes.json");  
            byte [] file = Files.readAllBytes(jsonFile);
            String jsonRecipeData = new String(file);
            
            //gets each recipe as an object and add it to the json recipe array which is a json array
            JSONObject object = new JSONObject(jsonRecipeData);
            JSONArray jsonRecipeArray = object.getJSONArray("recipes");

            //goes through each recipe and retrieves the data needed
            for (int i = 0; i < jsonRecipeArray.length(); i++){

                JSONObject jsonRecipe = jsonRecipeArray.getJSONObject(i); //gets each recipe
                String recipe_name = jsonRecipe.getString("name"); //retrieves the actual name of recipe which is next to the 'name' keyword
                String recipe_instructions = jsonRecipe.getString("instructions");// retrieves actual recipe instructions which is next to the 'instructions' keyword
                JSONArray recipe_ingredients = jsonRecipe.getJSONArray("ingredients");// retrieves the actual ingredients array stored next to the 'ingredients' keyword and stores in json array
                JSONArray ingredient_quantities = jsonRecipe.getJSONArray("quantity");//retrieves the actual quantity array stored next to keyword 'quantity' into a json array

                String [] ingredients = new String[recipe_ingredients.length()];//going to be used to store entries from json array into string array
                int [] quantity = new int[ingredient_quantities.length()];//going to be used to store entries from json array into int array

                for (int x = 0; x < recipe_ingredients.length(); x++){ //because ingredients and quantitys array will have to be same length 

                    ingredients[x] = recipe_ingredients.getString(x);//adds the ingredients from json array to string array
                    quantity[x] = ingredient_quantities.getInt(x);//adds quantity from json array to int array

                }
                recipeList.add(new Recipe(recipe_name, recipe_instructions, ingredients, quantity));//creates and adds a new recipe object into recipe list
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    public ArrayList<Recipe> LoadMatchedRecipes(ArrayList<Ingredient> userIngredients){

        // takes in array list of users ingredients
        ArrayList<Recipe> matchedRecipes = new ArrayList<Recipe>();

        //System.out.println("size: " + userIngredients.size());//test case

        ArrayList<Recipe> currentRecipes = getRecipesfromJSON(); //returns an array of recipes

        for (Recipe recipes : currentRecipes){ //goes through all the recipes in the current recipes

            boolean Matched = false;
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
                    matchedRecipes.add(recipes); //adds recipe to matched recipes list if there is a 
                    break;
                }
            }
        } 
       return matchedRecipes;
    }

    public LoadRecipes(){
        
    }
}

