package com.ecommerce.shoppingcart.dto;

import com.ecommerce.shoppingcart.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {

    private final Long id;
    private final LocalDateTime orderDate;
    private final Double totalAmount;
    private final OrderStatus status;
    private final List<OrderItemResponse> items;
}
