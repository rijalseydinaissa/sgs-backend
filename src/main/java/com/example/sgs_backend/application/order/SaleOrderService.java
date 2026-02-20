package com.example.sgs_backend.application.order;

import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.order.port.SaleOrderRepository;
import com.example.sgs_backend.domain.order.SaleOrder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SaleOrderService extends BaseService<SaleOrder, UUID, Object, Object> {

    private final SaleOrderRepository repository;

    public SaleOrderService(SaleOrderRepository repository) {
        this.repository = repository;
    }

    @Override protected BaseRepository<SaleOrder, UUID> getRepository() { return repository; }
    @Override protected String getEntityName() { return "SaleOrder"; }
    @Override protected SaleOrder toEntity(Object req) { return null; }
    @Override protected Object toResponse(SaleOrder entity) { return null; }
    @Override protected void updateEntity(SaleOrder entity, Object req) {}
}
