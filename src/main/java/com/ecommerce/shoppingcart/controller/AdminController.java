package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.model.Order;
import com.ecommerce.shoppingcart.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminController(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getAllSales() {
        try {
            List<Order> orders = orderRepository.findAll();
            System.out.println("Órdenes encontradas: " + orders.size());
            
            List<Map<String, Object>> salesData = new java.util.ArrayList<>();
            
            for (Order order : orders) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("orderDate", order.getOrderDate());
                orderMap.put("totalAmount", order.getTotalAmount());
                orderMap.put("status", order.getStatus());
                
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("username", order.getUser().getUsername());
                userMap.put("displayName", order.getUser().getDisplayName());
                userMap.put("email", order.getUser().getEmail());
                orderMap.put("user", userMap);
                
                // Cargar items manualmente
                List<Map<String, Object>> itemsList = new java.util.ArrayList<>();
                try {
                    var items = orderItemRepository.findByOrderId(order.getId());
                    for (var item : items) {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("id", item.getId());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("unitPrice", item.getUnitPrice());
                        
                        Map<String, Object> productMap = new HashMap<>();
                        productMap.put("id", item.getProduct().getId());
                        productMap.put("name", item.getProduct().getName());
                        productMap.put("image", item.getProduct().getImage());
                        itemMap.put("product", productMap);
                        
                        itemsList.add(itemMap);
                    }
                } catch (Exception e) {
                    System.out.println("Error cargando items para orden " + order.getId() + ": " + e.getMessage());
                }
                orderMap.put("items", itemsList);
                
                salesData.add(orderMap);
            }
            
            return ResponseEntity.ok(salesData);
        } catch (Exception e) {
            System.out.println("Error cargando órdenes: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        Double totalRevenue = orderRepository.getTotalRevenue();
        Long totalOrders = orderRepository.getTotalCompletedOrders();
        Long totalCustomers = orderRepository.getTotalUniqueCustomers();
        
        analytics.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        analytics.put("totalOrders", totalOrders != null ? totalOrders : 0L);
        analytics.put("totalCustomers", totalCustomers != null ? totalCustomers : 0L);
        analytics.put("averageOrderValue", totalOrders != null && totalOrders > 0 ? 
            (totalRevenue != null ? totalRevenue / totalOrders : 0.0) : 0.0);
        
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
            
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderDate", order.getOrderDate());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("username", order.getUser().getUsername());
            userMap.put("displayName", order.getUser().getDisplayName());
            userMap.put("email", order.getUser().getEmail());
            orderMap.put("user", userMap);
            
            // Cargar items
            List<Map<String, Object>> itemsList = new java.util.ArrayList<>();
            var items = orderItemRepository.findByOrderId(order.getId());
            for (var item : items) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("unitPrice", item.getUnitPrice());
                
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", item.getProduct().getId());
                productMap.put("name", item.getProduct().getName());
                productMap.put("image", item.getProduct().getImage());
                productMap.put("price", item.getProduct().getPrice());
                itemMap.put("product", productMap);
                
                itemsList.add(itemMap);
            }
            orderMap.put("items", itemsList);
            
            return ResponseEntity.ok(orderMap);
        } catch (Exception e) {
            System.out.println("Error cargando detalle de orden: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}