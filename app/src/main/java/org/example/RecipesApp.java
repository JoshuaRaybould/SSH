package org.example;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command
public class RecipesApp implements Runnable {

    // I believe this section can certainly be improved but there may not be time so these are sufficient for us to try stuff out
    @Option(names = {"-c", "--create"}) // Set this to create a new user
    private Boolean create = false;

    @Option(names = {"-i", "-ingredients"}) // Set this to get ingredients of specific tenant
    private boolean ingredients = false;

    @Option(names = {"-r", "-recipes"}) // Set this to mean we want to view recipes
    private boolean recipes = false;

    @Option(names = {"-t", "--tenant"}) // Set the tenant we want
    private int tenantID = 1;

    @Option(names = {"-n", "--name"}) // Set this to name a new user
    private String name = "";

    @Override
    public void run() { // This for testing, but we can use it to view stuff ourselves

        if(create == true){
            UserCreation.createUser(name);
        }

        Tenant tenant = new Tenant(tenantID);
        if (!tenant.tenantExists()) { // Check if the tenant exists in the database
            System.out.println("Tenant with id = " + tenantID + " not found");
        }
        
        else if (ingredients) { // We want to display ingredients
            printTenantIngredients(new StdoutIngredients(), tenantID);
        } else if (recipes){ // We want to display recommended recipes
            RecipeRanking.displayRankedRecipes(tenantID);
        }
    }
    
    public static void printTenantIngredients(IIngredientsOutput output, int tenantID) {
        output.reportIngredients(tenantID);
    }
}


