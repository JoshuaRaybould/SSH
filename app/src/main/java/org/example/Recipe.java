package org.example;

public class Recipe {

   
    private String name;
    private String instructions;
    private String[] ingredients;
    private int[] quantities;

    public Recipe(String name, String instructions, String[] ingredients, int[] quantities){
        
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.quantities = quantities;
    }
    
    
    public String getName(){
        return name;
    }

    public String getInstructions(){
        return instructions;
    }

    public String[] getIngredients(){
        return ingredients;
    }

    public int[] getQuantities(){
        return quantities;
    }



    
}

