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

}