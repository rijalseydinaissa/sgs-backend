package com.example.sgs_backend.application.stock.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record InventoryRequest(
    @NotBlank String reference,
    @NotNull UUID siteId,
    @NotNull LocalDate inventoryDate,
    String notes
) {}
