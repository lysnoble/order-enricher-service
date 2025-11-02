package com.teamviewer.order_enricher_service.service;

import com.teamviewer.order_enricher_service.DTO.ProductDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private Map<String, ProductDTO> products;

    @PostConstruct
    void seed() {
        products = Map.of(
                "P100", new ProductDTO("P100","Laptop", new BigDecimal("1299.99"),"Electronics", List.of("mobile","work")),
                "P200", new ProductDTO("P200","Mouse", new BigDecimal("29.99"),"Accessories", List.of("usb","input")),
                "P300", new ProductDTO("P300","Keyboard", new BigDecimal("79.99"),"Accessories", List.of("mechanical")),
                "P400", new ProductDTO("P400","Monitor", new BigDecimal("249.00"),"Electronics", List.of("1080p")),
                "P500", new ProductDTO("P500","Headset", new BigDecimal("59.90"),"Audio", List.of("mic"))
        );
    }

    public List<ProductDTO> getProductsByIds(List<String> ids) {
        List<ProductDTO> result = ids.stream()
                .map(products::get)
                .collect(Collectors.toCollection(ArrayList::new));
        if (result.contains(null)) {
            throw new IllegalArgumentException("One or more productIds are unknown " + ids);
        }
        return result;
    }
}
