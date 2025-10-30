package com.ecommerce.shoppingcart.mapper;

import com.ecommerce.shoppingcart.dto.ProductResponse;
import com.ecommerce.shoppingcart.model.Category;
import com.ecommerce.shoppingcart.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .image(product.getImage())
                .categoryId(category != null ? category.getId() : null)
                .categoryName(category != null ? category.getName() : null)
                .active(product.getActive())
                .views(product.getViews())
                .sales(product.getSales())
                .build();
    }
}
