package org.example;

import java.sql.*;
import java.util.Random;

public class UserCreation {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String username = "postgres";
    static String password = "example";
        
        public static void createUser(String userName) {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
            
            //inserts a new user into the Database
            String insertUserSQL = "INSERT INTO tenants (tenant_name) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userName);
                pstmt.executeUpdate();
                
                // Get the generated tenant_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int tenantId = generatedKeys.getInt(1);  // Get the generated tenant_id

                    //Get fridge items from the database
                    String selectItemsSQL = "SELECT fridge_item_id, fridge_item_name, type FROM fridge_items";
                    try (Statement stmt = conn.createStatement()) {
                        ResultSet rs = stmt.executeQuery(selectItemsSQL);

                        //Assigns random quantities for each fridge item based on its type
                        Random rand = new Random();

                        while (rs.next()) {
                            int fridgeItemId = rs.getInt("fridge_item_id");
                            String type = rs.getString("type");
                            int quantity = 0;

                            // Assign random quantities based on the type
                            if (type.equals("Liquid")) {
                                quantity = rand.nextInt(1001);  // Random number between 0 and 1000 ml
                            } else if (type.equals("Solid")) {
                                quantity = rand.nextInt(1001);  // Random number between 0 and 1000 g
                            } else if (type.equals("Unit")) {
                                quantity = rand.nextInt(13);   // Random number between 0 and 12
                            }

                            // Step 4: Insert the fridge item for the new tenant into tenants_fridge_items table
                            String insertFridgeItemSQL = "INSERT INTO tenants_fridge_items (tenant_id, fridge_item_id, quantity, date_time, quality) " +
                                                         "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
                            try (PreparedStatement insertFridgeItemPstmt = conn.prepareStatement(insertFridgeItemSQL)) {
                                // Random quality value for simplicity (you can adjust it as per your requirement)

                                insertFridgeItemPstmt.setInt(1, tenantId);
                                insertFridgeItemPstmt.setInt(2, fridgeItemId);
                                insertFridgeItemPstmt.setInt(3, quantity);

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

    public static void main(String[] args) {
        createUser("John Doe");
    }
}