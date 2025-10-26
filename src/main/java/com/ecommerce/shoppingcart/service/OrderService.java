package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.model.*;
import com.ecommerce.shoppingcart.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, 
                       ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(String username, List<Map<String, Object>> cartItems) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        double totalAmount = 0.0;
        
        // Validar stock primero
        for (Map<String, Object> item : cartItems) {
            Long productId = Long.valueOf(item.get("id").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());
            
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (product.getStock() < quantity) {
                throw new RuntimeException("Stock insuficiente para: " + product.getName());
            }
            
            totalAmount += product.getPrice() * quantity;
        }
        
        // Crear y guardar la orden
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.DELIVERED)
                .build();
        
        order = orderRepository.save(order);
        
        // Crear y guardar los items
        for (Map<String, Object> item : cartItems) {
            Long productId = Long.valueOf(item.get("id").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());
            
            Product product = productRepository.findById(productId).get();
            
            // Actualizar stock y estadísticas
            product.setStock(product.getStock() - quantity);
            product.setSales(product.getSales() + quantity);
            productRepository.save(product);
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            
            orderItemRepository.save(orderItem);
        }
        
        return order;
    }

    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
}