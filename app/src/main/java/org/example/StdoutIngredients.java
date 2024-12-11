package org.example;

public class StdoutIngredients implements IIngredientsOutput {

    @Override
    public void reportIngredients(int tenantId) { // This is to print the ingredients a user has to terminal
        Tenant tenant = new Tenant(tenantId);
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        if (tenant.getTenantName().equals("")) {
            System.out.println("Tenant doesn't exist");
        } else {
            System.out.println("Tenant with id " + tenantId + ", " + tenant.getTenantName() + ", has the following ingedients");
            for (Ingredient ingredient: tenantIngredients.getIngredients()) {
                String ingredientString = String.format("%-15s", (ingredient.getName()));
                String ingredientQuantity = String.format("%-6s", ingredient.getQuantity());
                System.out.println("Name:" + ingredientString + "Quantity:" + ingredientQuantity + "Quality:" + ingredient.getQuality());
            }
        }
    }
    
}
