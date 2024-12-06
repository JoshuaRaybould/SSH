package org.example.backend_SSH.ingredient_logic;

public class StdoutIngredients implements IIngredientsOutput {

    @Override
    public void reportIngredients(int tenantId) {
        TenantIngredients tenant1 = new TenantIngredients(tenantId);
        if (tenant1.getTenantName().equals("")) {
            System.out.println("Tenant doesn't exist");
        } else {
            System.out.println("Tenant with id " + tenantId + ", " + tenant1.getTenantName() + ", has the following ingedients");
            for (Ingredient ing: tenant1.getIngredients()) {
                System.out.println("Name:" + ing.getName() + ", Quantity:" + ing.getQuantity() + ", Quality:" + ing.getQuality());
            }
        }
    }
    
}
