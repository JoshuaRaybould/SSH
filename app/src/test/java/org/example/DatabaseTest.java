package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String username = "postgres";
    String password = "example";

    
    @Test
    public void testBasicDatabase() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement()) {
                assertTrue(conn.isValid(5));
                String sql = "SELECT * FROM fridge_items LIMIT 1";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                assertEquals(rs.getInt("fridge_item_id"), 1001);
                assertEquals(rs.getString("fridge_item_name"), "Milk");
            }
        catch (SQLException e) {
            e.printStackTrace();
            fail("Connection failed!");
        }
    }

    @Test
    public void testBasicRecipeTable() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement()) {
                assertTrue(conn.isValid(5));
                String sql = "SELECT * FROM recipes LIMIT 1";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                assertEquals(rs.getInt("recipe_id"), 1001);
                assertEquals(rs.getString("recipe_name"), "Lemon Chicken");
                assertEquals(rs.getString("recipe_instructions"), "Marinate chicken with lemon juice, minced garlic, olive oil and pepper. Bake or pan-fry until it's golden and cooked through.");
            }
        catch (SQLException e) {
            e.printStackTrace();
            fail("Connection failed!");
        }
    }
}