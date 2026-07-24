package dispesas.com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {

        Map<String, Object> response = new HashMap<>();

        response.put("status", "UP");
        response.put("service", "Sistema de Gestão de Despesas");
        response.put("timestamp", Instant.now());

        return ResponseEntity.ok(response);
    }
}
