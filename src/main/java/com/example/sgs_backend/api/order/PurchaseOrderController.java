package com.example.sgs_backend.api.order;

import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.order.PurchaseOrderService;
import com.example.sgs_backend.application.order.dto.PurchaseOrderRequest;
import com.example.sgs_backend.application.order.dto.PurchaseOrderResponse;
import com.example.sgs_backend.domain.order.PurchaseOrder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Orders", description = "Commandes fournisseurs")
public class PurchaseOrderController extends BaseController<PurchaseOrder, UUID, PurchaseOrderRequest, PurchaseOrderResponse> {

    private final PurchaseOrderService service;

    @Override
    protected BaseService<PurchaseOrder, UUID, PurchaseOrderRequest, PurchaseOrderResponse> getService() {
        return service;
    }
}
