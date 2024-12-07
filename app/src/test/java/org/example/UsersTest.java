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

    @BeforeEach
    public void setUp() {
        // Fetch the tenant ID for 'Test User' to be used in subsequent tests
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String checkUserSQL = "SELECT tenant_id FROM tenants WHERE tenant_name = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(checkUserSQL)) {
                pstmt.setString(1, "Test User");
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    tenantId = rs.getInt("tenant_id");
                } else {
                    fail("Test User does not exist in the tenants table.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("An error occurred while fetching the tenant ID.");
        }
    }

    @Test
    public void testUserTestUserExists() {
        // Verify if 'Test User' exists in the tenants table
        assertTrue(tenantId > 0, "User 'Test User' should exist in the tenants table.");
    }

    @Test
    public void testMandatoryItemsAssigned() {
        // Verify if the mandatory items were assigned to 'Test User' in tenants_fridge_items table
        int[] mandatoryItemIds = {1001, 1002, 1006, 1046, 1047, 1048, 1045, 1044};
        for (int itemId : mandatoryItemIds) {
            String checkItemSQL = "SELECT * FROM tenants_fridge_items WHERE tenant_id = ? AND fridge_item_id = ?";
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement itemStmt = conn.prepareStatement(checkItemSQL)) {
                itemStmt.setInt(1, tenantId);
                itemStmt.setInt(2, itemId);
                ResultSet itemRs = itemStmt.executeQuery();

                // Assert that each mandatory item is assigned to 'Test User'
                assertTrue(itemRs.next(), "Item ID " + itemId + " should be assigned to user 'Test User'.");
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
