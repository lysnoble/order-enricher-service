package com.teamviewer.order_enricher_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.order_enricher_service.DTO.CustomerDTO;
import com.teamviewer.order_enricher_service.DTO.OrderRequest;
import com.teamviewer.order_enricher_service.DTO.OrderResponse;
import com.teamviewer.order_enricher_service.DTO.ProductDTO;
import com.teamviewer.order_enricher_service.model.EnrichedOrderEntity;
import com.teamviewer.order_enricher_service.repository.EnrichedOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderEnrichmentServiceTest {

    @Mock private CustomerService customerService;
    @Mock private ProductService productService;
    @Mock private EnrichedOrderRepository repository;

    // Real mapper to avoid chained-mock NPEs
    @Spy private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private OrderEnrichmentService orderEnrichmentService;

    // Shared test data
    private static final String ORDER_ID = "order1";
    private static final String CUSTOMER_ID = "customer1";
    private static final List<String> PRODUCT_IDS = List.of("product1", "product2");
    private static final Instant TS = Instant.ofEpochSecond(123456L);

    private static final CustomerDTO CUSTOMER = new CustomerDTO(
            "customer1", "John Doe", "123 Main St", "63101", "USA"
    );

    private static final List<ProductDTO> PRODUCTS = List.of(
            new ProductDTO("product1", "Product A", new BigDecimal("19.99"), "Electronics", List.of("gadget", "tech")),
            new ProductDTO("product2", "Product B", new BigDecimal("49.99"), "Accessories", List.of("premium", "sale"))
    );

    private static final String CUSTOMER_JSON =
            "{\"id\":\"customer1\",\"name\":\"John Doe\",\"street\":\"123 Main St\",\"zip\":\"63101\",\"country\":\"USA\"}";

    private static final String PRODUCTS_JSON =
            "[" +
                    "{\"id\":\"product1\",\"name\":\"Product A\",\"price\":19.99,\"category\":\"Electronics\",\"tags\":[\"gadget\",\"tech\"]}," +
                    "{\"id\":\"product2\",\"name\":\"Product B\",\"price\":49.99,\"category\":\"Accessories\",\"tags\":[\"premium\",\"sale\"]}" +
                    "]";

    @Test
    void createOrder_savesEnrichedOrderAndReturnsResponse() throws Exception {
        OrderRequest request = new OrderRequest(ORDER_ID, CUSTOMER_ID, PRODUCT_IDS, TS);

        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(CUSTOMER);
        when(productService.getProductsByIds(PRODUCT_IDS)).thenReturn(PRODUCTS);

        OrderResponse response = orderEnrichmentService.createOrder(request);

        // capture the entity saved to the repository
        ArgumentCaptor<EnrichedOrderEntity> cap = ArgumentCaptor.forClass(EnrichedOrderEntity.class);
        verify(repository).save(cap.capture());
        EnrichedOrderEntity saved = cap.getValue();

        assertEquals(ORDER_ID, response.orderId());
        assertEquals(CUSTOMER, response.customer());
        assertEquals(PRODUCTS, response.products());
        assertEquals(TS, response.timestamp());

        // sanity checks on the persisted JSON
        assertEquals(CUSTOMER_ID, objectMapper.readValue(saved.getCustomerJson(), CustomerDTO.class).id());
        List<ProductDTO> parsed = objectMapper.readerForListOf(ProductDTO.class).readValue(saved.getProductsJson());
        assertEquals(PRODUCTS.size(), parsed.size());
    }

    @Test
    void getById_returnsOrderResponseWhenOrderExists() {
        EnrichedOrderEntity entity = EnrichedOrderEntity.builder()
                .orderId(ORDER_ID)
                .customerId(CUSTOMER_ID)
                .productIds(PRODUCT_IDS)
                .timestamp(TS)
                .customerJson(CUSTOMER_JSON)
                .productsJson(PRODUCTS_JSON)
                .build();

        when(repository.findById(ORDER_ID)).thenReturn(Optional.of(entity));

        OrderResponse response = orderEnrichmentService.getById(ORDER_ID);

        assertEquals(ORDER_ID, response.orderId());
        assertEquals(CUSTOMER, response.customer());
        assertEquals(PRODUCTS, response.products());
        assertEquals(TS, response.timestamp());
    }

    @Test
    void getById_throwsExceptionWhenOrderDoesNotExist() {
        when(repository.findById("invalidOrderId")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> orderEnrichmentService.getById("invalidOrderId"));
    }

    @Test
    void findByCustomerId_returnsListOfOrderResponses() {
        EnrichedOrderEntity entity = EnrichedOrderEntity.builder()
                .orderId(ORDER_ID)
                .customerId(CUSTOMER_ID)
                .productIds(PRODUCT_IDS)
                .timestamp(TS)
                .customerJson(CUSTOMER_JSON)
                .productsJson(PRODUCTS_JSON)
                .build();

        when(repository.findByCustomerId(CUSTOMER_ID)).thenReturn(List.of(entity));

        List<OrderResponse> responses = orderEnrichmentService.findByCustomerId(CUSTOMER_ID);

        assertEquals(1, responses.size());
        OrderResponse r = responses.get(0);
        assertEquals(ORDER_ID, r.orderId());
        assertEquals(CUSTOMER, r.customer());
        assertEquals(PRODUCTS, r.products());
        assertEquals(TS, r.timestamp());
    }
}
