package com.teamviewer.order_enricher_service.service;

import com.teamviewer.order_enricher_service.DTO.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
        customerService.seed();
    }

    @Test
    @DisplayName("Retrieve customer by valid ID")
    void retrieveCustomerByValidId() {
        CustomerDTO customer = customerService.getCustomerById("C001");
        assertNotNull(customer);
        assertEquals("Alice Example", customer.name());
    }

    @Test
    @DisplayName("Throw exception for unknown customer ID")
    void throwExceptionForUnknownCustomerId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById("C999"));
        assertEquals("Unknown customerId C999", exception.getMessage());
    }

    @Test
    @DisplayName("Throw exception for null customer ID")
    void throwExceptionForNullCustomerId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById(null));
        assertEquals("Unknown customerId " + null, exception.getMessage());
    }

    @Test
    @DisplayName("Throw exception for empty customer ID")
    void throwExceptionForEmptyCustomerId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById(""));
        assertEquals("Unknown customerId ", exception.getMessage());
    }
}