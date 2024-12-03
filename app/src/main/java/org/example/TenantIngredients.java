package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TenantIngredients {
    private final int tenantId;
    private ArrayList<Ingredient> ingredients;

    public TenantIngredients(int tenantId) {
        this.tenantId = tenantId;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }


}
