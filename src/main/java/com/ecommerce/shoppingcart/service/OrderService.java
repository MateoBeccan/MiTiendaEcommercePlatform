package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.dto.OrderItemRequest;
import com.ecommerce.shoppingcart.dto.OrderResponse;
import com.ecommerce.shoppingcart.exception.BusinessException;
import com.ecommerce.shoppingcart.exception.ResourceNotFoundException;
import com.ecommerce.shoppingcart.mapper.OrderMapper;
import com.ecommerce.shoppingcart.model.Order;
import com.ecommerce.shoppingcart.model.OrderItem;
import com.ecommerce.shoppingcart.model.OrderStatus;
import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.repository.OrderItemRepository;
import com.ecommerce.shoppingcart.repository.OrderRepository;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.repository.UserRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                       ProductRepository productRepository, UserRepository userRepository,
                       OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(String username, List<OrderItemRequest> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new BusinessException("El carrito no puede estar vacío");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Map<Product, Integer> itemsWithQuantity = new LinkedHashMap<>();
        double totalAmount = 0.0;

        for (OrderItemRequest item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new BusinessException("El producto " + product.getName() + " no está disponible");
            }

            if (product.getStock() < item.getQuantity()) {
                throw new BusinessException("Stock insuficiente para: " + product.getName());
            }

            itemsWithQuantity.merge(product, item.getQuantity(), Integer::sum);
            totalAmount += product.getPrice() * item.getQuantity();
        }

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);

        List<OrderItem> savedItems = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : itemsWithQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            product.setStock(product.getStock() - quantity);
            product.setSales(product.getSales() + quantity);
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            savedItems.add(orderItemRepository.save(orderItem));
        }

        order.setItems(savedItems);
        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return orderRepository.findByUserOrderByOrderDateDesc(user).stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
