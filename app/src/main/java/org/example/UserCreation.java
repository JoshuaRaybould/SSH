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
            
            // Inserts a new user into the Database
            String insertUserSQL = "INSERT INTO tenants (tenant_name) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userName);
                pstmt.executeUpdate();
                
                // Get the generated tenant_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int tenantId = generatedKeys.getInt(1);  // Get the generated tenant_id

                    // Always assign specific items to the user
                    int[] mandatoryItemIds = {1001, 1002, 1006, 1046, 1047, 1048, 1045, 1044};
                    for (int itemId : mandatoryItemIds) {
                        assignItemToUser(conn, tenantId, itemId, generateQuantity("Unit"), 0);
                    }

                    // Get fridge items from the database
                    String selectItemsSQL = "SELECT fridge_item_id, fridge_item_name, type FROM fridge_items";
                    try (Statement stmt = conn.createStatement()) {
                        ResultSet rs = stmt.executeQuery(selectItemsSQL);

                        // Assign random quantities for each fridge item based on its type
                        Random rand = new Random();

                        while (rs.next()) {
                            int fridgeItemId = rs.getInt("fridge_item_id");
                            String type = rs.getString("type");
                            int quantity = 0;

                            // Assign random quantities based on the type
                            if (type.equals("Liquid")) {
                                quantity = rand.nextInt(1001);  // 0 to 1000 ml
                            } else if (type.equals("Solid")) {
                                quantity = rand.nextInt(1001);  // 0 to 1000 g
                            } else if (type.equals("Unit")) {
                                quantity = rand.nextInt(13);   // 0 to 12 units
                            }

                            // Insert the fridge item for the new tenant into tenants_fridge_items table
                            String insertFridgeItemSQL = "INSERT INTO tenants_fridge_items (tenant_id, fridge_item_id, quantity, date_time, quality) " +
                                                         "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
                            try (PreparedStatement insertFridgeItemPstmt = conn.prepareStatement(insertFridgeItemSQL)) {
                                insertFridgeItemPstmt.setInt(1, tenantId);
                                insertFridgeItemPstmt.setInt(2, fridgeItemId);
                                insertFridgeItemPstmt.setInt(3, quantity);
                                insertFridgeItemPstmt.setDouble(4, 0); // Set quality to 0
                                insertFridgeItemPstmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate random quantities based on item type
    private static int generateQuantity(String type) {
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
        System.out.print("Enter the name of the user to create: ");
        String userName = scanner.nextLine();
        createUser(userName);
        System.out.println("User created successfully!");
        scanner.close();
    }
}