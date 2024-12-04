package org.example;

import java.util.ArrayList;
import java.util.List;

public class MemoryIngredients implements IIngredientsOutput{
    private String tenantString;
    private List<String> ingedientStrings;

    @Override
    public void reportIngredients(int tenantId) {
        TenantIngredients tenant1 = new TenantIngredients(tenantId);
        if (!tenant1.getTenantName().equals("")) {
            tenantString = "Tenant with id " + tenantId + ", " + tenant1.getTenantName() + ", has the following ingedients";
            for (Ingredient ing: tenant1.getIngredients()) {
                ingedientStrings.add("Name:" + ing.getName() + ", Quantity:" + ing.getQuantity() + ", Quality:" + ing.getQuality());
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
    
}
