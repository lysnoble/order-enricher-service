package com.teamviewer.order_enricher_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enriched_orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EnrichedOrderEntity {

    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private String orderId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @ElementCollection
    @CollectionTable(name = "order_product_ids",
            joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "product_id")
    @Builder.Default
    private List<String> productIds = new ArrayList<>();

    @Column(name = "ts", nullable = false)
    private Instant timestamp;

    // store enriched snapshots as JSON text to keep schema simple
    @Lob
    @Column(name = "customer_json", columnDefinition = "TEXT")
    private String customerJson;

    @Lob
    @Column(name = "products_json", columnDefinition = "TEXT")
    private String productsJson;
}
