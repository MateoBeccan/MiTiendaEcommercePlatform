package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.ProductRequest;
import com.ecommerce.shoppingcart.dto.ProductResponse;
import com.ecommerce.shoppingcart.mapper.ProductMapper;
import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam(required = false) String search,
                                                @RequestParam(required = false) Long category) {
        List<Product> products;
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
        } else if (category != null) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/popular")
    public List<ProductResponse> getPopularProducts() {
        return productService.getPopularProducts().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ProductResponse> incrementViews(@PathVariable Long id) {
        Product product = productService.incrementViews(id);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest request) {
        Product product = productService.updateProduct(id, request);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
