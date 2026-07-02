package com.modelos.usuarios.adapters.inbound.http.controllers;

import com.modelos.usuarios.adapters.inbound.http.contracts.ErrorResponse;
import com.modelos.usuarios.application.services.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final ObjectProvider<UserService> userServiceProvider;

    public HealthController(ObjectProvider<UserService> userServiceProvider) {
        this.userServiceProvider = userServiceProvider;
    }

    @GetMapping("/live")
    public Map<String, String> liveness() {
        return Map.of("status", "alive");
    }

    @GetMapping("/ready")
    public ResponseEntity<?> readiness() {
        if (userServiceProvider.getIfAvailable() == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("User service unavailable."));
        }

        return ResponseEntity.ok(Map.of(
                "status", "ready",
                "persistence", "in-memory"));
    }
}
