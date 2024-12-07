package org.example;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class UserCreation {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String username = "postgres";
    static String password = "example";
        
    public static void createUser(String userName) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
    
            // Insert the new user and retrieve the generated tenant_id
            String insertUserSQL = "INSERT INTO tenants (tenant_name) VALUES (?) RETURNING tenant_id";
            int tenantId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
                pstmt.setString(1, userName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        tenantId = rs.getInt("tenant_id");
                    } else {
                        throw new SQLException("Failed to retrieve tenant_id.");
                    }
                }
            }
    
            // Assign mandatory items to the user
            int[] mandatoryItemIds = {1001, 1002, 1006, 1046, 1047, 1048, 1045, 1044};
            for (int itemId : mandatoryItemIds) {
                assignItemToUser(conn, tenantId, itemId, generateQuantity("Unit"), 0);
            }
    
            // Assign random fridge items to the user
            String selectItemsSQL = "SELECT fridge_item_id, type FROM fridge_items";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectItemsSQL)) {
    
                Random rand = new Random();
                while (rs.next()) {
                    int fridgeItemId = rs.getInt("fridge_item_id");
                    String type = rs.getString("food_type");
    
                    int quantity = switch (type) {
                        case "Liquid" -> rand.nextInt(1001); // 0 to 1000 ml
                        case "Solid" -> rand.nextInt(1001);  // 0 to 1000 g
                        case "Unit" -> rand.nextInt(13);     // 0 to 12 units
                        default -> 0;
                    };
    
                    assignItemToUser(conn, tenantId, fridgeItemId, quantity, 0);
                }
            }
    
            System.out.println("User created successfully with tenant ID: " + tenantId);
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper method to generate random quantities based on item type
    public static int generateQuantity(String type) {
        Random rand = new Random();
        if (type.equals("Liquid")) {
            return rand.nextInt(1001);  // 0 to 1000 ml
        } else if (type.equals("Solid")) {
            return rand.nextInt(1001);  // 0 to 1000 g
        } else if (type.equals("Unit")) {
            return rand.nextInt(13);   // 0 to 12 units
        }
        return 0;
    }

    // Helper method to assign an item to a user
    private static void assignItemToUser(Connection conn, int tenantId, int fridgeItemId, int quantity, double quality) throws SQLException {
        String insertFridgeItemSQL = "INSERT INTO tenants_fridge_items (tenant_id, fridge_item_id, quantity, date_time, quality) " +
                                     "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertFridgeItemSQL)) {
            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, fridgeItemId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, quality);
            pstmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Automatically create a 'Test User'
        createUser("Test User");
        System.out.println("Test User created successfully!");

        // Prompt for a new user
        System.out.print("Enter the name of the user to create: ");
        String userName = scanner.nextLine();
        createUser(userName);
        System.out.println("User created successfully!");
        
        scanner.close();
    }
}