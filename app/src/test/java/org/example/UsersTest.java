package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class UsersTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "example";

    private int tenantId;

    @Test
    public void setup() {
        // Create "TestUser" before running tests
        UserCreation.createUser("TestUser");

        try {
            // to sleep 10 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // recommended because catching InterruptedException clears interrupt flag
            Thread.currentThread().interrupt();
            // you probably want to quit if the thread is interrupted
            return;
        }
    
        // Retrieve tenant_id for "TestUser"
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT tenant_id FROM tenants WHERE tenant_name = 'TestUser'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tenantId = rs.getInt("tenant_id");
            } else {
                fail("TestUser not found in the tenants table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error while setting up Test User.");
        }
    }

    @Test
    public void testUserExists() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT tenant_id FROM tenants WHERE tenant_name = 'TestUser'";
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
        assertTrue(tenantId > 0, "Test User should exist in the tenants table.");
    }

    @Test
public void testMandatoryItemsAssigned() {
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT tenant_id FROM tenants WHERE tenant_name = 'TestUser'";
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
    // Verify if the mandatory items were assigned to 'Test User' in tenants_fridge_items table
    int[] mandatoryItemIds = {1001, 1002, 1006, 1046, 1047, 1048, 1045, 1044};
    for (int itemId : mandatoryItemIds) {
        String checkItemSQL = "SELECT * FROM tenants_fridge_items WHERE tenant_id = ? AND fridge_item_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement itemStmt = conn.prepareStatement(checkItemSQL)) {
            itemStmt.setInt(1, tenantId);  // Use the tenantId from setup()
            itemStmt.setInt(2, itemId);
            ResultSet itemRs = itemStmt.executeQuery();

            // Assert that each mandatory item is assigned to 'Test User'
            assertTrue(itemRs.next(), "Item ID " + itemId + " should be assigned to user 'TestUser'.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("An error occurred while checking the fridge items.");
        }
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