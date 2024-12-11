package org.example;

import java.util.List;
// This is just to get ingredients into a list for testing
// It also creates the user if they don't already exist
public class MemoryIngredients implements IIngredientsOutput{ 
    private String tenantName = "";
    private List<Ingredient> ingredients;

    @Override
    public void reportIngredients(int tenantId) {
        Tenant tenant = new Tenant(tenantId);
        tenantName = tenant.getTenantName();
        // If the tenant doesn't already exist make it 
        if (tenantName.equals("")) { // Since this is only used for testing it will only ever create users 1 to 4
            UserCreation.createUser("User:" + tenantId);
            tenantName = tenant.getTenantName();
        }
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        ingredients = tenantIngredients.getIngredients();
    }

    public Boolean hasSucceeded(){
        return (!tenantName.equals(""));
    }

    public String getTenantName(){
        return tenantName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
}
