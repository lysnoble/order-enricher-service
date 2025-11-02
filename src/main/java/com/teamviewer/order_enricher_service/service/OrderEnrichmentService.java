package com.teamviewer.order_enricher_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.order_enricher_service.DTO.*;
import com.teamviewer.order_enricher_service.model.EnrichedOrderEntity;
import com.teamviewer.order_enricher_service.repository.EnrichedOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEnrichmentService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final EnrichedOrderRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderResponse createOrder(OrderRequest req) {
        CustomerDTO customer = customerService.getCustomerById(req.customerId());
        List<ProductDTO> products = productService.getProductsByIds(req.productIds());

        String customerJson = toJson(customer);
        String productsJson = toJson(products);

        EnrichedOrderEntity entity = EnrichedOrderEntity.builder()
                .orderId(req.orderId())
                .customerId(req.customerId())
                .productIds(req.productIds())
                .timestamp(req.timestamp())
                .customerJson(customerJson)
                .productsJson(productsJson)
                .build();

        repository.save(entity);
        return new OrderResponse(req.orderId(), customer, products, req.timestamp());
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(String orderId) {
        EnrichedOrderEntity e = repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found " + orderId));
        return toResponse(e);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByProductId(String productId) {
        return repository.findByProductId(productId).stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(EnrichedOrderEntity e) {
        CustomerDTO customer = fromJson(e.getCustomerJson(), CustomerDTO.class);
        List<ProductDTO> products = fromJsonList(e.getProductsJson());
        return new OrderResponse(e.getOrderId(), customer, products, e.getTimestamp());
    }

    private String toJson(Object o) {
        try { return objectMapper.writeValueAsString(o); }
        catch (JsonProcessingException ex) { throw new RuntimeException(ex); }
    }

    private <T> T fromJson(String json, Class<T> cls) {
        try { return objectMapper.readValue(json, cls); }
        catch (JsonProcessingException ex) { throw new RuntimeException(ex); }
    }

    private List<ProductDTO> fromJsonList(String json) {
        try {
            return objectMapper.readerForListOf(ProductDTO.class).readValue(json);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
