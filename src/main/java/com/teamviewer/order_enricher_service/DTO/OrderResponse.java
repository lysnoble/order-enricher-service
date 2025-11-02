package com.teamviewer.order_enricher_service.DTO;


import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String orderId,
        CustomerDTO customer,
        List<ProductDTO> products,
        Instant timestamp
) {}