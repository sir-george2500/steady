package com.steady.steady_app.controller.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController extends BaseController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("app", "UP");

        // Check DB connection
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(2)) {  // timeout 2 seconds
                status.put("database", "UP");
            } else {
                status.put("database", "DOWN");
            }
        } catch (SQLException e) {
            status.put("database", "DOWN");
            status.put("error", e.getMessage());
        }

        return status;
    }
}
