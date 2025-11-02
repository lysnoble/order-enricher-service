package com.teamviewer.order_enricher_service.repository;

import com.teamviewer.order_enricher_service.model.EnrichedOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrichedOrderRepository extends JpaRepository<EnrichedOrderEntity, String> {
    List<EnrichedOrderEntity> findByCustomerId(String customerId);

    @Query("select e from EnrichedOrderEntity e join e.productIds p where p = :productId")
    List<EnrichedOrderEntity> findByProductId(String productId);
}
