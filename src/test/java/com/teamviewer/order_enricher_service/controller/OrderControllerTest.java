package com.teamviewer.order_enricher_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.order_enricher_service.DTO.OrderRequest;
import com.teamviewer.order_enricher_service.DTO.OrderResponse;
import com.teamviewer.order_enricher_service.DTO.CustomerDTO;
import com.teamviewer.order_enricher_service.DTO.ProductDTO;
import com.teamviewer.order_enricher_service.service.OrderEnrichmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderEnrichmentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final CustomerDTO CUSTOMER = new CustomerDTO(
            "C100", "Jane Doe", "42 Galaxy Rd", "63101", "USA"
    );

    private static final List<ProductDTO> PRODUCTS = List.of(
            new ProductDTO("P100", "Laptop", new BigDecimal("1299.99"), "Electronics", List.of("mobile", "work")),
            new ProductDTO("P200", "Mouse", new BigDecimal("29.99"), "Accessories", List.of("usb", "input"))
    );

    private static final Instant TS = Instant.parse("2024-10-12T10:15:30Z");

    private static final OrderResponse RESPONSE =
            new OrderResponse("O123", CUSTOMER, PRODUCTS, TS);

    @Test
    @DisplayName("POST /orders creates order and returns 201")
    void createOrder_returnsCreated() throws Exception {
        OrderRequest req = new OrderRequest("O123", "C100", List.of("P100", "P200"), TS);
        when(service.createOrder(any(OrderRequest.class))).thenReturn(RESPONSE);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/O123"))
                .andExpect(jsonPath("$.orderId").value("O123"))
                .andExpect(jsonPath("$.customer.id").value("C100"))
                .andExpect(jsonPath("$.products[0].id").value("P100"));
    }

    @Test
    @DisplayName("GET /orders/{orderId} returns 200 and body")
    void getOrderById_returnsOk() throws Exception {
        when(service.getById("O123")).thenReturn(RESPONSE);

        mockMvc.perform(get("/orders/O123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("O123"))
                .andExpect(jsonPath("$.customer.name").value("Jane Doe"))
                .andExpect(jsonPath("$.products.length()").value(2));
    }

    @Test
    @DisplayName("GET /orders with customerId returns list")
    void queryByCustomerId_returnsList() throws Exception {
        when(service.findByCustomerId("C100")).thenReturn(List.of(RESPONSE));

        mockMvc.perform(get("/orders").param("customerId", "C100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value("O123"));
    }

    @Test
    @DisplayName("GET /orders with productId returns list")
    void queryByProductId_returnsList() throws Exception {
        when(service.findByProductId("P100")).thenReturn(List.of(RESPONSE));

        mockMvc.perform(get("/orders").param("productId", "P100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.id").value("C100"));
    }

    @Test
    @DisplayName("GET /orders without params returns 400 Bad Request")
    void queryWithoutParams_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handles IllegalArgumentException with 404 response")
    void handleIllegalArgumentException_returns404() throws Exception {
        when(service.getById("badId"))
                .thenThrow(new IllegalArgumentException("Order not found badId"));

        mockMvc.perform(get("/orders/badId"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found badId"));
    }
}
