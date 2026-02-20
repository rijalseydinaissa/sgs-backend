package com.example.sgs_backend.api.notification;

import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.application.notification.NotificationService;
import com.example.sgs_backend.application.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;
    
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> send(@RequestBody NotificationRequest request) {
        service.send(request);
        return ResponseEntity.ok(ApiResponse.success("Notification envoyée"));
    }
}
