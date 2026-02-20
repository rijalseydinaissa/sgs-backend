package com.example.sgs_backend.api.dashboard;


import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.application.dashboard.DashboardService;
import com.example.sgs_backend.application.dashboard.dto.DashboardKPIs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard", description = "KPIs temps réel")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(summary = "Obtenir les KPIs dashboard (résultats cachés 1h)")
    public ResponseEntity<ApiResponse<DashboardKPIs>> getKPIs() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getKPIs()));
    }
}
