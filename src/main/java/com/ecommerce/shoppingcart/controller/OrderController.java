package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.OrderRequest;
import com.ecommerce.shoppingcart.dto.OrderResponse;
import com.ecommerce.shoppingcart.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderResponse response = orderService.createOrder(username, request.getItems());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderResponse> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }
}
