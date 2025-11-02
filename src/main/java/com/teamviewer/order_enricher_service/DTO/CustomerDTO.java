package com.teamviewer.order_enricher_service.DTO;

public record CustomerDTO(
        String id,
        String name,
        String street,
        String zip,
        String country
) {}
