package com.enview.product.service;

import com.enview.product.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    List<Product> data=new ArrayList<>();

    public ProductService() {
        data.addAll(Arrays.asList(
                new Product(1, "Laptop", 1200.00, "High-performance laptop"),
                new Product(2, "Mouse", 25.00, "Wireless ergonomic mouse"),
                new Product(3, "Keyboard", 75.00, "Mechanical gaming keyboard"),
                new Product(4, "Monitor", 300.00, "27-inch 4K monitor")
        ));
    }

    public List<Product> getAllProducts() {
        // In a real application, this would fetch from a database
        return data;
    }
    public Product getProduct(int id){
        return data.stream().filter( product -> product.getId()==id).findFirst().get();
    }

    public int addProduct(Product product){
        product.setId(data.stream().sorted(Comparator.comparing(Product::getId).reversed()).findFirst().get().getId()+1);
        data.add(product);
        return product.getId();
    }
}
