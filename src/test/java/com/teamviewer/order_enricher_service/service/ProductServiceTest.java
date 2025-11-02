package com.teamviewer.order_enricher_service.service;

import com.teamviewer.order_enricher_service.DTO.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        productService.seed();
    }

    @Test
    @DisplayName("Returns valid products when all IDs exist")
    void getProductsByIds_validIds_returnsProductList() {
        List<ProductDTO> products = productService.getProductsByIds(List.of("P100", "P200"));

        assertEquals(2, products.size());
        assertEquals("Laptop", products.get(0).name());
        assertEquals("Mouse", products.get(1).name());
        assertEquals(new BigDecimal("1299.99"), products.get(0).price());
        assertEquals("Accessories", products.get(1).category());
        assertTrue(products.get(0).tags().contains("mobile"));
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when any ID is invalid")
    void getProductsByIds_invalidId_throwsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.getProductsByIds(List.of("P100", "P999"))
        );
        assertTrue(ex.getMessage().contains("unknown"));
    }

    @Test
    @DisplayName("Returns an empty list when given an empty input")
    void getProductsByIds_emptyList_returnsEmpty() {
        List<ProductDTO> products = productService.getProductsByIds(List.of());
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Throws NullPointerException when input list is null")
    void getProductsByIds_nullList_throwsNPE() {
        assertThrows(NullPointerException.class, () -> productService.getProductsByIds(null));
    }

    @Test
    @DisplayName("ProductService initializes with correct seed data")
    void seed_createsAllProducts() {
        List<ProductDTO> all = productService.getProductsByIds(List.of("P100", "P200", "P300", "P400", "P500"));
        assertEquals(5, all.size());
        assertEquals("Keyboard", all.get(2).name());
        assertEquals("Electronics", all.get(0).category());
        assertEquals("Audio", all.get(4).category());
        assertEquals(new BigDecimal("59.90"), all.get(4).price());
    }
}
