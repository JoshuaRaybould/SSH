package org.example;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class UserCreation {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String username = "postgres";
    static String password = "example";
    public static Object createUser;

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
                assignMandatoryItemToUser(conn, tenantId, itemId);
            }

            // Assign random fridge items to the user
            String selectItemsSQL = "SELECT fridge_item_id, food_type FROM fridge_items";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectItemsSQL)) {

                Random rand = new Random();
                while (rs.next()) {
                    int fridgeItemId = rs.getInt("fridge_item_id");
                    String type = rs.getString("food_type");

                    // Adjusted to handle case insensitivity for food types
                    int quantity = switch (type.toLowerCase()) {  // Convert to lowercase
                        case "liquid" -> rand.nextInt(1001);  // 0 to 1000 ml
                        case "solid" -> rand.nextInt(1001);   // 0 to 1000 g
                        case "unit" -> rand.nextInt(13);      // 0 to 12 units
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

    // Helper method to assign a mandatory item to a user, based on its food_type
    private static void assignMandatoryItemToUser(Connection conn, int tenantId, int itemId) throws SQLException {
        String selectItemSQL = "SELECT food_type FROM fridge_items WHERE fridge_item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectItemSQL)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String foodType = rs.getString("food_type");
                    int quantity = generateQuantity(foodType);
                    assignItemToUser(conn, tenantId, itemId, quantity, 0);
                } else {
                    System.err.println("Item ID " + itemId + " not found in fridge_items.");
                }
            }
        }
    }

    // Helper method to generate random quantities based on item type
    public static int generateQuantity(String type) {
        Random rand = new Random();
        if (type.equalsIgnoreCase("Liquid")) {
            return rand.nextInt(1001);  // 0 to 1000 ml
        } else if (type.equalsIgnoreCase("Solid")) {
            return rand.nextInt(1001);  // 0 to 1000 g
        } else if (type.equalsIgnoreCase("Unit")) {
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

        createUser("Test User");

        // Prompt for a new user
        System.out.print("Enter the name of the user to create: ");
        String userName = scanner.nextLine();
        createUser(userName);
        System.out.println("User created successfully!");

        scanner.close();
    }
}