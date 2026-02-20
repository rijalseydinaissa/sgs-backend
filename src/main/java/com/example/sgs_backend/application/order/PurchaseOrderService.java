package com.example.sgs_backend.application.order;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.order.dto.PurchaseOrderRequest;
import com.example.sgs_backend.application.order.dto.PurchaseOrderResponse;
import com.example.sgs_backend.application.order.port.PurchaseOrderRepository;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.order.PurchaseOrder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PurchaseOrderService extends BaseService<PurchaseOrder, UUID, PurchaseOrderRequest, PurchaseOrderResponse> {

    private final PurchaseOrderRepository repository;

    public PurchaseOrderService(PurchaseOrderRepository repository) {
        this.repository = repository;
    }

    @Override protected BaseRepository<PurchaseOrder, UUID> getRepository() { return repository; }
    @Override protected String getEntityName() { return "PurchaseOrder"; }

    @Override
    protected PurchaseOrder toEntity(PurchaseOrderRequest req) {
        return PurchaseOrder.builder()
                .reference(req.reference())
                .supplierId(req.supplierId())
                .orderDate(req.orderDate())
                .totalAmount(Money.zero("XOF"))
                .build();
    }

    @Override
    protected PurchaseOrderResponse toResponse(PurchaseOrder p) {
        return new PurchaseOrderResponse(
                p.getId(), p.getReference(), p.getSupplierId(), null,
                p.getOrderDate(), p.getStatus(),
                p.getTotalAmount() != null ? p.getTotalAmount().getAmount() : null,
                p.getTotalAmount() != null ? p.getTotalAmount().getCurrency() : null
        );
    }

    @Override
    protected void updateEntity(PurchaseOrder entity, PurchaseOrderRequest req) {
        // Commandes ne se modifient pas après création
    }
}
