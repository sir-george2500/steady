package com.steady.steady_app.controller.v1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class HealthController extends BaseController{

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "UP");
    }
}
