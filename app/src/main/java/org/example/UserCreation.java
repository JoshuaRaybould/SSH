package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

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

            //Assign mandatory items to the user
            int[] mandatoryItemIds = {1001, 1002, 1006, 1046, 1047, 1048, 1045, 1044};
            for (int itemId : mandatoryItemIds) {
                int shelfLife = getShelfLife(conn, itemId); // Query shelf life for each mandatory item
                assignMandatoryItemToUser(conn, tenantId, itemId, shelfLife, new Random());
            }

            // Assign random fridge items to the user
            String selectItemsSQL = "SELECT fridge_item_id, food_type, estimated_shelf_life FROM fridge_items";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectItemsSQL)) {

                Random rand = new Random();
                while (rs.next()) {
                    String foodType = rs.getString("food_type");
                    int fridgeItemId = rs.getInt("fridge_item_id");
                    int shelfLife = rs.getInt("estimated_shelf_life");
                    Boolean isMandatory = false;

                    for(int mandatoryItemId : mandatoryItemIds) {
                        if(fridgeItemId == mandatoryItemId){
                            isMandatory = true; 
                        }
                    }

                    if(rand.nextInt(100) <= 70 && !isMandatory){

                        // Calculate random captured date between current date and (current date - shelf life - 1)
                        LocalDate capturedDate = generateCapturedDate(shelfLife, rand);

                        // Use the Ingredient class to calculate quality
                        Ingredient ingredient = new Ingredient();
                        ingredient.setCapturedDate(capturedDate);
                        ingredient.setEstimatedShelfLife(shelfLife);
                        double quality = ingredient.calculateQuality();

                        // Adjusted to handle case insensitivity for food types
                        int quantity = switch (foodType.toLowerCase()) {  // Convert to lowercase
                            case "liquid" -> rand.nextInt(1001);  // 0 to 1000 ml
                            case "solid" -> rand.nextInt(1001);   // 0 to 1000 g
                            case "unit" -> rand.nextInt(13);      // 0 to 12 units
                            default -> 0;
                        };

                        // System.out.println("This is an error lol");
                        assignItemToUser(conn, tenantId, fridgeItemId, quantity, quality, Date.valueOf(capturedDate));
                    }
                }
            }

            System.out.println("Items added successfully to tenant ID: " + tenantId);
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int getShelfLife(Connection conn, int itemId) throws SQLException {
        String selectShelfLifeSQL = "SELECT estimated_shelf_life FROM fridge_items WHERE fridge_item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectShelfLifeSQL)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("estimated_shelf_life");
                } else {
                    throw new SQLException("Shelf life not found for item ID: " + itemId);
                }
            }
        }
    }

    // Helper method to assign a mandatory item to a user, based on its food_type
    private static void assignMandatoryItemToUser(Connection conn, int tenantId, int itemId, int shelfLife, Random rand) throws SQLException {
        String selectItemSQL = "SELECT food_type FROM fridge_items WHERE fridge_item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectItemSQL)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String foodType = rs.getString("food_type");
                    int quantity = generateQuantity(foodType);

                    // Calculate random captured date between current date and (current date - shelf life - 1)
                    LocalDate capturedDate = generateCapturedDate(shelfLife, rand);

                    Ingredient ingredient = new Ingredient();
                    ingredient.setCapturedDate(capturedDate);
                    ingredient.setEstimatedShelfLife(shelfLife);
                    double quality = ingredient.calculateQuality();

                    assignItemToUser(conn, tenantId, itemId, quantity, quality, Date.valueOf(capturedDate));
                } else {
                    System.err.println("Item ID " + itemId + " not found in fridge_items.");
                }
            }
        }
    }

    // Calculate random captured date between current date and (current date - shelf life - 1)
    public static LocalDate generateCapturedDate(int shelfLife, Random rand) {
        int maxDeviation = shelfLife + 1;
        int deviation = rand.nextInt(maxDeviation);
        LocalDate capturedDate = LocalDate.now().minusDays(deviation);
        return capturedDate;
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
    private static void assignItemToUser(Connection conn, int tenantId, int fridgeItemId, int quantity, double quality, Date capturedDate) throws SQLException {
        String insertFridgeItemSQL = "INSERT INTO tenants_fridge_items (tenant_id, fridge_item_id, quantity, date_time, quality) " +
                                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertFridgeItemSQL)) {
            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, fridgeItemId);
            pstmt.setInt(3, quantity);
            pstmt.setDate(4, capturedDate);
            pstmt.setDouble(5, quality);
            pstmt.executeUpdate();
        }
    }

    public static void Create() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Prompt for a new user
        System.out.print("Enter the name of the user to create: ");
        String userName;
        try {
            userName = reader.readLine();
            createUser(userName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Try to read the command given in        
        System.out.println("User created successfully!");

    }
}
