package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    java.util.List<Product> findTop8ByActiveTrueOrderBySalesDescViewsDesc();
    java.util.List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
    java.util.List<Product> findByActiveTrueOrderByNameAsc();
}
