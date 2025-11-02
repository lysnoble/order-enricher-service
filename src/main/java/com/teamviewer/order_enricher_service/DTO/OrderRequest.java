package com.teamviewer.order_enricher_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record OrderRequest(
        @NotBlank String orderId,
        @NotBlank String customerId,
        @NotEmpty List<String> productIds,
        @NotNull Instant timestamp
) {}
