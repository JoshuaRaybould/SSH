package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tenant {
    private final int tenantId;
    private String tenantName = "";

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String password = "example";

    public Tenant(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        if (tenantName.equals("")) {
    
            try (Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement()) {
    
                    String sql = "SELECT * FROM tenants WHERE tenant_id =" + tenantId;
                    ResultSet rs = stmt.executeQuery(sql);
                    if(!rs.next()) {
                        return "";
                    }
                    tenantName = rs.getString("tenant_name");
                }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return tenantName;
    }

    public boolean tenantExists() {
        getTenantName();
        return !tenantName.equals("");
    }
}
