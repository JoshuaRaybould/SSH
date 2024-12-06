package org.example;

public class StdoutIngredients implements IIngredientsOutput {

    @Override
    public void reportIngredients(int tenantId) {
        Tenant tenant = new Tenant(tenantId);
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        if (tenant.getTenantName().equals("")) {
            System.out.println("Tenant doesn't exist");
        } else {
            System.out.println("Tenant with id " + tenantId + ", " + tenant.getTenantName() + ", has the following ingedients");
            for (Ingredient ing: tenantIngredients.getIngredients()) {
                System.out.println("Name:" + ing.getName() + ", Quantity:" + ing.getQuantity() + ", Quality:" + ing.getQuality());
            }
        }
    }
    
}
