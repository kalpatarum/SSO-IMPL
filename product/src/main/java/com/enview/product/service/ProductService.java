package com.enview.product.service;

import com.enview.product.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    public List<Product> getAllProducts() {
        // In a real application, this would fetch from a database
        return Arrays.asList(
                new Product("P001", "Laptop", 1200.00, "High-performance laptop"),
                new Product("P002", "Mouse", 25.00, "Wireless ergonomic mouse"),
                new Product("P003", "Keyboard", 75.00, "Mechanical gaming keyboard"),
                new Product("P004", "Monitor", 300.00, "27-inch 4K monitor")
        );
    }
}
