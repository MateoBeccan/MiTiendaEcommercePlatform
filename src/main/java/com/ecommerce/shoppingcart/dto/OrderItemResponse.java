package com.ecommerce.shoppingcart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private final Long productId;
    private final String productName;
    private final Integer quantity;
    private final Double unitPrice;
    private final Double lineTotal;
}
