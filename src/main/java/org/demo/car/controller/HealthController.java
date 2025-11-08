package org.demo.car.controller;

import org.demo.car.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    /**
     * 首页
     */
    @GetMapping("/")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("service", "Car Insurance Platform Backend");
        data.put("version", "1.0.0");
        data.put("status", "running");
        data.put("timestamp", LocalDateTime.now());
        return Result.success("Welcome to Car Insurance Platform API", data);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", LocalDateTime.now());
        
        // 检查数据库连接
        try (Connection conn = dataSource.getConnection()) {
            data.put("status", "UP");
            data.put("database", "MySQL");
            data.put("databaseStatus", "Connected");
            data.put("catalog", conn.getCatalog());
        } catch (Exception e) {
            data.put("status", "DOWN");
            data.put("databaseStatus", "Disconnected");
            data.put("error", e.getMessage());
            return Result.error(500, "System health check failed");
        }
        
        return Result.success(data);
    }
}
