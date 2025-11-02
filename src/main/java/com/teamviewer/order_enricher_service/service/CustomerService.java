package com.teamviewer.order_enricher_service.service;

import com.teamviewer.order_enricher_service.DTO.CustomerDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerService {

    private Map<String, CustomerDTO> customers;

    @PostConstruct
    void seed() {
        customers = Map.of(
                "C001", new CustomerDTO("C001","Alice Example","12 Main St","90210","USA"),
                "C002", new CustomerDTO("C002","Bob Smith","44 Oak Ave","10001","USA"),
                "C003", new CustomerDTO("C003","Carla Canada","7 Maple Drive","70173","CAN")
        );
    }

    public CustomerDTO getCustomerById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Unknown customerId " + id);
        }
        CustomerDTO dto = customers.get(id);
        if (dto == null) throw new IllegalArgumentException("Unknown customerId " + id);
        return dto;
    }
}
