package com.example.sgs_backend.application.product;


import com.example.sgs_backend.application.common.BaseRepository;
import com.example.sgs_backend.application.common.BaseService;
import com.example.sgs_backend.application.common.PageResponse;
import com.example.sgs_backend.application.product.dto.ProductRequest;
import com.example.sgs_backend.application.product.dto.ProductResponse;
import com.example.sgs_backend.application.product.dto.ProductSummaryResponse;
import com.example.sgs_backend.application.product.port.BarcodeGeneratorPort;
import com.example.sgs_backend.application.product.port.ProductCategoryRepository;
import com.example.sgs_backend.application.product.port.ProductRepository;
import com.example.sgs_backend.application.product.port.SupplierRepository;
import com.example.sgs_backend.domain.common.exception.DuplicateResourceException;
import com.example.sgs_backend.domain.common.exception.ResourceNotFoundException;
import com.example.sgs_backend.domain.common.valueobject.Money;
import com.example.sgs_backend.domain.common.valueobject.Quantity;
import com.example.sgs_backend.domain.product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * ✅ CORRIGÉ — Construit Money et Quantity à partir des DTOs
 */
@Service @Slf4j
public class ProductService extends BaseService<Product, UUID, ProductRequest, ProductResponse> {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final BarcodeGeneratorPort      barcodeGenerator;

    public ProductService(ProductRepository productRepository,
                          ProductCategoryRepository categoryRepository,
                          SupplierRepository supplierRepository,
                          BarcodeGeneratorPort barcodeGenerator) {
        this.productRepository  = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.barcodeGenerator   = barcodeGenerator;
    }

    @Override protected BaseRepository<Product, UUID> getRepository() { return productRepository; }
    @Override protected String getEntityName() { return "Product"; }

    @Override
    protected Product toEntity(ProductRequest req) {
        if (productRepository.existsByReference(req.reference()))
            throw new DuplicateResourceException("Product", "reference", req.reference());

        if (req.barcode() != null && !req.barcode().isBlank()
                && productRepository.existsByBarcode(req.barcode()))
            throw new DuplicateResourceException("Product", "barcode", req.barcode());

        ProductCategory category = categoryRepository
                .findByIdAndDeletedFalse(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", req.categoryId()));

        Supplier mainSupplier = null;
        if (req.mainSupplierId() != null) {
            mainSupplier = supplierRepository
                    .findByIdAndDeletedFalse(req.mainSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", req.mainSupplierId()));
        }

        String barcode = (req.barcode() != null && !req.barcode().isBlank())
                ? req.barcode()
                : barcodeGenerator.generateEan13(req.reference());

        String currency = req.currency() != null ? req.currency() : "XOF";

        // Construire Money VOs ✅
        Money purchasePriceMoney = req.purchasePrice() != null 
                ? Money.of(req.purchasePrice(), currency) 
                : null;
        Money sellingPriceMoney = Money.of(req.sellingPrice(), currency);

        // Construire Quantity VOs ✅
        Quantity minStock = Quantity.of(req.minimumStockValue(), req.minimumStockUnit());
        Quantity maxStock = (req.maximumStockValue() != null && req.maximumStockUnit() != null)
                ? Quantity.of(req.maximumStockValue(), req.maximumStockUnit())
                : null;
        Quantity reorder = (req.reorderPointValue() != null && req.reorderPointUnit() != null)
                ? Quantity.of(req.reorderPointValue(), req.reorderPointUnit())
                : null;

        // Stock initial = 0 dans la même unité que le minimum
        Quantity initialStock = Quantity.zero(req.minimumStockUnit());

        return Product.builder()
                .reference(req.reference())
                .name(req.name())
                .description(req.description())
                .barcode(barcode)
                .imageUrl(req.imageUrl())
                .category(category)
                .purchasePrice(purchasePriceMoney)
                .sellingPrice(sellingPriceMoney)
                .currentStock(initialStock)
                .minimumStock(minStock)
                .maximumStock(maxStock)
                .reorderPoint(reorder)
                .evaluationMethod(req.evaluationMethod() != null
                        ? req.evaluationMethod()
                        : StockEvaluationMethod.WEIGHTED_AVERAGE)
                .hasExpiryDate(req.hasExpiryDate())
                .expiryAlertDays(req.expiryAlertDays() != null ? req.expiryAlertDays() : 30)
                .mainSupplier(mainSupplier)
                .siteId(req.siteId())
                .status(ProductStatus.ACTIVE)
                .build();
    }

    @Override
    protected ProductResponse toResponse(Product p) {
        BigDecimal stockVal = p.getStockValue() != null ? p.getStockValue().getAmount() : BigDecimal.ZERO;
        return new ProductResponse(
                p.getId(), p.getReference(), p.getName(), p.getDescription(),
                p.getBarcode(), p.getImageUrl(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getCategory() != null ? p.getCategory().getFullPath() : null,
                p.getPurchasePrice() != null ? p.getPurchasePrice().getAmount() : null,
                p.getSellingPrice().getAmount(),
                p.getSellingPrice().getCurrency(),
                p.getMarginPercent(),
                p.getCurrentStock().getValue(),
                p.getCurrentStock().getUnit(),
                p.getMinimumStock().getValue(),
                p.getMinimumStock().getUnit(),
                p.isLowStock(), p.isOutOfStock(),
                stockVal, p.getEvaluationMethod(),
                p.getMainSupplier() != null ? p.getMainSupplier().getId() : null,
                p.getMainSupplier() != null ? p.getMainSupplier().getName() : null,
                p.getSiteId(), p.getStatus(), p.isHasExpiryDate(),
                p.getCreatedAt(), p.getCreatedBy(), p.getUpdatedAt()
        );
    }

    @Override
    protected void updateEntity(Product product, ProductRequest req) {
        if (!product.getReference().equals(req.reference())
                && productRepository.existsByReference(req.reference()))
            throw new DuplicateResourceException("Product", "reference", req.reference());

        ProductCategory category = categoryRepository
                .findByIdAndDeletedFalse(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", req.categoryId()));

        Supplier mainSupplier = null;
        if (req.mainSupplierId() != null)
            mainSupplier = supplierRepository.findByIdAndDeletedFalse(req.mainSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", req.mainSupplierId()));

        String currency = req.currency() != null ? req.currency() : product.getSellingPrice().getCurrency();

        product.setReference(req.reference());
        product.setName(req.name());
        product.setDescription(req.description());
        product.setImageUrl(req.imageUrl());
        product.setCategory(category);
        product.setPurchasePrice(req.purchasePrice() != null ? Money.of(req.purchasePrice(), currency) : null);
        product.setSellingPrice(Money.of(req.sellingPrice(), currency));
        product.setMinimumStock(Quantity.of(req.minimumStockValue(), req.minimumStockUnit()));
        product.setHasExpiryDate(req.hasExpiryDate());
        product.setMainSupplier(mainSupplier);
        product.setSiteId(req.siteId());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ProductResponse findByReference(String reference) {
        return productRepository.findByReference(reference)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product", reference));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ProductResponse findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product (barcode)", barcode));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public PageResponse<ProductSummaryResponse> findByCategory(UUID categoryId, Pageable pageable) {
        var page = productRepository
                .findByCategoryIdAndDeletedFalse(categoryId, pageable)
                .map(this::toSummary);
        return PageResponse.of(page);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('STOCK_READ')")
    public List<ProductSummaryResponse> findLowStock(UUID siteId) {
        return productRepository.findLowStockProducts(siteId)
                .stream().map(this::toSummary).toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('STOCK_READ')")
    public List<ProductSummaryResponse> findOutOfStock(UUID siteId) {
        return productRepository.findOutOfStockProducts(siteId)
                .stream().map(this::toSummary).toList();
    }

    @Transactional
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ProductResponse archiveProduct(UUID id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.archive();
        return toResponse(productRepository.save(product));
    }

    private ProductSummaryResponse toSummary(Product p) {
        return new ProductSummaryResponse(
                p.getId(), p.getReference(), p.getName(), p.getBarcode(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getSellingPrice().getAmount(), p.getSellingPrice().getCurrency(),
                p.getCurrentStock().getValue(), p.getCurrentStock().getUnit(),
                p.isLowStock(), p.getStatus()
        );
    }
}
