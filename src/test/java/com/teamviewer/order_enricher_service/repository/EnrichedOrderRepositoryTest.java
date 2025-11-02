package com.teamviewer.order_enricher_service.repository;

import com.teamviewer.order_enricher_service.model.EnrichedOrderEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrichedOrderRepositoryTest {

    @Autowired
    private EnrichedOrderRepository repository;

    @Nested
    @DisplayName("findByCustomerId")
    class FindByCustomerId {

        @Test
        @DisplayName("Should return orders for a valid customerId")
        void returnOrdersForValidCustomerId() {
            List<EnrichedOrderEntity> orders = repository.findByCustomerId("customer1");
            assertThat(orders).isNotEmpty();
        }

        @Test
        @DisplayName("Should return empty list for non-existent customerId")
        void returnEmptyListForNonExistentCustomerId() {
            List<EnrichedOrderEntity> orders = repository.findByCustomerId("nonExistentCustomer");
            assertThat(orders).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByProductId")
    class FindByProductId {

        @Test
        @DisplayName("Should return orders for a valid productId")
        void returnOrdersForValidProductId() {
            List<EnrichedOrderEntity> orders = repository.findByProductId("product1");
            assertThat(orders).isNotEmpty();
        }

        @Test
        @DisplayName("Should return empty list for non-existent productId")
        void returnEmptyListForNonExistentProductId() {
            List<EnrichedOrderEntity> orders = repository.findByProductId("nonExistentProduct");
            assertThat(orders).isEmpty();
        }
    }
}