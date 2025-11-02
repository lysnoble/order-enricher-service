package com.teamviewer.order_enricher_service.DTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        String id,
        String name,
        BigDecimal price,
        String category,
        List<String> tags
) {}
