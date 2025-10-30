package com.ecommerce.shoppingcart.mapper;

import com.ecommerce.shoppingcart.dto.OrderItemResponse;
import com.ecommerce.shoppingcart.dto.OrderResponse;
import com.ecommerce.shoppingcart.model.Order;
import com.ecommerce.shoppingcart.model.OrderItem;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems() == null ? List.of() : order.getItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(items)
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getUnitPrice() * item.getQuantity())
                .build();
    }
}
