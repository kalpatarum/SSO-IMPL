package com.enview.product.controller;

import com.enview.product.model.Product;
import com.enview.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    // @PreAuthorize: This annotation secures the method.
    // hasAuthority('SCOPE_products:read'): Ensures that the incoming JWT
    // has a 'scope' claim containing 'products:read'.
    // The 'SCOPE_' prefix is automatically added by Spring Security.
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable int id){
        return productService.getProduct(id);
    }

    @PostMapping("/add")
    public int addProduct(@RequestBody Product product){
        return this.productService.addProduct(product);
    }
}
