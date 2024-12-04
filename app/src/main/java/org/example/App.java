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

// To get a user's ingredients type, tenantid i or tenantid ingredients
public class App {
    public static void main(String[] args) {

        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter command:");

               try {
                String command = reader.readLine(); // Try to read the command given in

                String[] commandParts = command.split(" ");
                if (commandParts.length != 2) {
                    System.out.println("Usage:tenantID i");
                    System.out.println("example:1 i");
                } else {
                    try {
                        int tenantID = Integer.parseInt(commandParts[0]);
                        if (commandParts[1].equals("ingredients") || commandParts[1].equals("i")) {
                            TenantIngredients tenant1 = new TenantIngredients(tenantID);
                            if (tenant1.getTenantName().equals("")) {
                                System.out.println("Tenant doesn't exist");
                            } else {
                                System.out.println("Tenant with id " + commandParts[0] + ", " + tenant1.getTenantName() + ", has the following ingedients");
                                for (Ingredient ing: tenant1.getIngredients()) {
                                    System.out.println("Name:" + ing.getName() + ", Quantity:" + ing.getQuantity() + ", Quality:" + ing.getQuality());
                                }
                            }
                        } else {
                            System.out.println("Usage:tenantID i");
                            System.out.println("example:1 i");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please type a valid user id.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
