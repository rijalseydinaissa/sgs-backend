package com.example.sgs_backend.api.stock;


import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.stock.StockService;
import com.example.sgs_backend.application.stock.dto.StockMovementRequest;
import com.example.sgs_backend.application.stock.dto.StockMovementResponse;
import com.example.sgs_backend.domain.stock.StockMovement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/** ✅ extends BaseController<StockMovement, UUID, StockMovementRequest, StockMovementResponse> */
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Stock", description = "Mouvements, inventaires, alertes")
public class StockController extends BaseController<StockMovement, UUID, StockMovementRequest, StockMovementResponse> {

    private final StockService stockService;

    @Override protected BaseService<StockMovement, UUID, StockMovementRequest, StockMovementResponse> getService() {
        return stockService;
    }

    @PostMapping("/entry")
    @PreAuthorize("hasAuthority('STOCK_ENTRY')")
    @Operation(summary = "Enregistrer une entrée de stock (réception marchandise)")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordEntry(
            @Valid @RequestBody StockMovementRequest request,
            @RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.created(stockService.recordEntry(request, userId)));
    }

    @PostMapping("/exit")
    @PreAuthorize("hasAuthority('STOCK_EXIT')")
    @Operation(summary = "Enregistrer une sortie de stock (vente, perte)")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordExit(
            @Valid @RequestBody StockMovementRequest request,
            @RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.created(stockService.recordExit(request, userId)));
    }
}
