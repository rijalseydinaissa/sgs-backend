package com.example.sgs_backend.api.order;

import com.example.sgs_backend.api.common.BaseController;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.order.SaleOrderService;
import com.example.sgs_backend.domain.order.SaleOrder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sale-orders")
@RequiredArgsConstructor
@Tag(name = "Sale Orders", description = "Commandes clients")
public class SaleOrderController extends BaseController<SaleOrder, UUID, Object, Object> {

    private final SaleOrderService service;

    @Override
    protected BaseService<SaleOrder, UUID, Object, Object> getService() {
        return service;
    }
}
