/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
*/
package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import picocli.CommandLine;

// To get a user's ingredients type, tenantid i or tenantid ingredients
public class App {
    public static void main(String[] args) {
        if (args.length >= 1) {
            new CommandLine(new RecipesApp()).execute(args);
        }
        
        while (args.length < 1) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter command:");

               try {
                String command = reader.readLine(); // Try to read the command given in

                if (command.equals("e")) { // exit the program
                    System.exit(0);
                }
                if (command.equals("c")) {
                    UserCreation.Create();
                }

                String[] commandParts = command.split(" ");
                if (commandParts.length != 2) {
                    printUsage();
                } else {
                    try {
                        int tenantID = Integer.parseInt(commandParts[0]);
                        carryOutCommands(tenantID, commandParts[1]);

                    } catch (NumberFormatException e) {
                        System.out.println("Please type a valid user id.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void carryOutCommands(int tenantID, String command) {
        // First check the tenant actually exists

        Tenant tenant = new Tenant(tenantID);
        if (!tenant.tenantExists()) { // Check if the tenant exists in the database
            System.out.println("Tenant with id = " + tenantID + " not found");
        }
        else if (command.equals("ingredients") || command.equals("i")) { // We want to display ingredients
            printTenantIngredients(new StdoutIngredients(), tenantID);
        } else if (command.equals("rankrecipes") || command.equals("r")){ // We want to display recommended recipes
            RecipeRanking.displayRankedRecipes(tenantID);
        }else{
            printUsage();
        }
    }

    public static void printTenantIngredients(IIngredientsOutput output, int tenantID) {
        output.reportIngredients(tenantID);
    }

    public static void printUsage() {
        System.out.println("Usage:tenantID <command>");
        System.out.println("Commands: i   gives the ingredients the user has");
        System.out.println("Commands: r   gives the recommended recipes for the user");
    }
}
