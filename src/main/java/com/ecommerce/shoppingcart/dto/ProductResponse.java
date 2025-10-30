package com.ecommerce.shoppingcart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final Double price;
    private final Integer stock;
    private final String image;
    private final Long categoryId;
    private final String categoryName;
    private final Boolean active;
    private final Integer views;
    private final Integer sales;
}
