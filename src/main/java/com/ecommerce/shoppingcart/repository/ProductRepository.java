package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop8ByActiveTrueOrderBySalesDescViewsDesc();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByActiveTrueOrderByNameAsc();

    @Query("SELECT p FROM Product p WHERE p.active = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) "
            + "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<Product> searchActiveProducts(@Param("term") String term);
}
