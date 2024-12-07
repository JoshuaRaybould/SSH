package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class UsersTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "example";

    private int tenantId;

    public void setup() {
        // Create "Test User" before running tests
        UserCreation.createUser("Test User");
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT tenant_id FROM tenants WHERE tenant_name = 'Test User'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tenantId = rs.getInt("tenant_id");
            } else {
                fail("Test User not found in the tenants table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error while setting up Test User.");
        }
    }


    @Test
    public void testGenerateQuantity_Liquid() {
        int quantity = UserCreation.generateQuantity("Liquid");
        assertTrue(quantity >= 0 && quantity <= 1000, "Quantity should be between 0 and 1000 for Liquid");
    }

    @Test
    public void testGenerateQuantity_Solid() {
        int quantity = UserCreation.generateQuantity("Solid");
        assertTrue(quantity >= 0 && quantity <= 1000, "Quantity should be between 0 and 1000 for Solid");
    }

    @Test
    public void testGenerateQuantity_Unit() {
        int quantity = UserCreation.generateQuantity("Unit");
        assertTrue(quantity >= 0 && quantity <= 12, "Quantity should be between 0 and 12 for Unit");
    }
}