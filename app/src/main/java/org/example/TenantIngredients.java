package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TenantIngredients {
    private final int tenantId;
    private String tenantName = "";
    private ArrayList<Ingredient> ingredients;

    public TenantIngredients(int tenantId) {
        this.tenantId = tenantId;
    }

    public ArrayList<Ingredient> getIngredients() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "example";

        ingredients = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement()) {

                String sql = "SELECT * FROM tenants_fridge_items INNER JOIN fridge_items ON tenants_fridge_items.fridge_item_id=fridge_items.fridge_item_id WHERE tenants_fridge_items.tenant_id = " + tenantId;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Ingredient currIngredient = new Ingredient();
                    currIngredient.setName(rs.getString("fridge_item_name"));
                    currIngredient.setQuantity(rs.getInt("quantity"));
                    currIngredient.setQuality(rs.getDouble("quality"));
                    ingredients.add(currIngredient);
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    public String getTenantName() {
        if (tenantName.equals("")) {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String password = "example";
    
            try (Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement()) {
    
                    String sql = "SELECT * FROM tenants WHERE tenant_id =" + tenantId;
                    ResultSet rs = stmt.executeQuery(sql);
                    if(!rs.next()) {
                        return "";
                    }
                    tenantName = rs.getString("tenant_name");
                }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return tenantName;
    }


}
