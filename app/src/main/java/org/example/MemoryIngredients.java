package org.example;

import java.util.List;

public class MemoryIngredients implements IIngredientsOutput{
    private String tenantName = "";
    private List<Ingredient> ingredients;

    @Override
    public void reportIngredients(int tenantId) {
        Tenant tenant = new Tenant(tenantId);
        tenantName = tenant.getTenantName();
        TenantIngredients tenantIngredients = new TenantIngredients(tenantId);
        ingredients = tenantIngredients.getIngredients();
    }

    public String getTenantName(){
        return tenantName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
}
