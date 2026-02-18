package com.project.gold.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        log.info("Root endpoint accessed");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Digital Gold Backend API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "getPrice", "GET /api/v1/gold/price",
            "buyGold", "POST /api/v1/gold/buy",
            "getHoldings", "GET /api/v1/gold/holdings/{userId}",
            "getTransactions", "GET /api/v1/gold/transactions/{userId}"
        ));
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}
