package com.teamviewer.order_enricher_service.controller;

import com.teamviewer.order_enricher_service.DTO.OrderRequest;
import com.teamviewer.order_enricher_service.DTO.OrderResponse;
import com.teamviewer.order_enricher_service.service.OrderEnrichmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderEnrichmentService service;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        OrderResponse created = service.createOrder(request);
        return ResponseEntity.created(URI.create("/orders/" + created.orderId())).body(created);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable String orderId) {
        return ResponseEntity.ok(service.getById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> query(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String productId) {

        if (customerId == null && productId == null) {
            return ResponseEntity.badRequest().build();
        }
        if (customerId != null) {
            return ResponseEntity.ok(service.findByCustomerId(customerId));
        }
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
