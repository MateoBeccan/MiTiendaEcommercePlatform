package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.model.Order;
import com.ecommerce.shoppingcart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) request.get("items");
            
            Order order = orderService.createOrder(username, cartItems);
            return ResponseEntity.ok(Map.of("orderId", order.getId(), "message", "Orden creada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }
}