package org.example;

import java.util.ArrayList;
import java.util.List;

public class MemoryIngredients implements IIngredientsOutput{
    private String tenantString = "";
    private List<String> ingedientStrings; // This is for use with test user
    private List<Ingredient> ingredients;

    @Override
    public void reportIngredients(int tenantId) {
        Tenant tenant = new Tenant(tenantId);
        String tenantName = tenant.getTenantName();
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        ingredients = tenantIngredients.getIngredients();

        if (tenantId == 1) { // If we are the test user
            if (!tenantName.equals("")) {
                tenantString = "Tenant with id " + tenantId + ", " + tenantName + ", has the following ingedients";
                for (Ingredient ingredient: ingredients) {
                    ingedientStrings.add("Name:" + ingredient.getName() + ", Quantity:" + ingredient.getQuantity() + ", Quality:" + ingredient.getQuality());
                }
            }
        } 
    }

    public MemoryIngredients() {
        ingedientStrings = new ArrayList<>();
    }

    public String getTenantString(){
        return tenantString;
    }

    public List<String> getIngredientStrings() {
        return ingedientStrings;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
}
