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
        // Ensure 'Test User' is created before the tests run
        UserCreation.createUser("Test User");

        // Retrieve the tenant_id for 'Test User' from the database
        tenantId = -1;  // Default invalid tenant ID
        String getTenantIdSQL = "SELECT tenant_id FROM tenants WHERE tenant_name = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement getTenantStmt = conn.prepareStatement(getTenantIdSQL)) {
            getTenantStmt.setString(1, "Test User");  // 'Test User' is the name of the user created
            ResultSet tenantRs = getTenantStmt.executeQuery();
            
            // Retrieve tenantId for 'Test User'
            if (tenantRs.next()) {
                tenantId = tenantRs.getInt("tenant_id");
            } else {
                fail("Test user not found in the tenants table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("An error occurred while retrieving tenant_id.");
        }
    }

    @Test
    public void testUserExists() {
        // Assert that the tenant ID was found and is greater than 0
        assertTrue(tenantId > 0, "Test User should exist in the tenants table.");
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

                // Ass
