package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.ProductRequest;
import com.ecommerce.shoppingcart.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.model.Category;
import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.repository.CategoryRepository;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Category category = getCategory(request.getCategoryId());
        Product product = Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .image(request.getImage())
                .category(category)
                .active(request.getActive() == null ? Boolean.TRUE : request.getActive())
                .build();
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product existing = getProductById(id);
        Category category = getCategory(request.getCategoryId());
        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setImage(request.getImage());
        existing.setCategory(category);
        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchActiveProducts(searchTerm.trim());
    }

    @Transactional(readOnly = true)
    public List<Product> getPopularProducts() {
        return productRepository.findTop8ByActiveTrueOrderBySalesDescViewsDesc();
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    @Transactional
    public Product incrementViews(Long productId) {
        Product product = getProductById(productId);
        product.setViews(product.getViews() + 1);
        return productRepository.save(product);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
    }
}
