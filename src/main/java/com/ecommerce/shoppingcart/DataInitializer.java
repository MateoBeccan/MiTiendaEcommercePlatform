package com.ecommerce.shoppingcart;

import com.ecommerce.shoppingcart.model.*;
import com.ecommerce.shoppingcart.repository.*;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductRepository productRepository, UserRepository userRepository, 
                          CategoryRepository categoryRepository, OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario administrador
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@mitienda.com")
                    .password(passwordEncoder.encode("admin123"))
                    .displayName("Administrador")
                    .role(Role.ADMIN)
                    .build());
        }
        
        // Crear usuario regular
        if (userRepository.findByUsername("user").isEmpty()) {
            userRepository.save(User.builder()
                    .username("user")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("user123"))
                    .displayName("Usuario Demo")
                    .role(Role.USER)
                    .build());
        }

        // Crear categorías
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder().name("Electrónicos").description("Dispositivos electrónicos").build());
            categoryRepository.save(Category.builder().name("Computación").description("Productos de computación").build());
            categoryRepository.save(Category.builder().name("Gaming").description("Productos para gaming").build());
        }
        
        if (productRepository.count() == 0) {
            Category gaming = categoryRepository.findAll().get(2);
            Category computacion = categoryRepository.findAll().get(1);
            
            productRepository.save(Product.builder()
                    .name("Laptop Gaming")
                    .description("Laptop para gaming con RTX 4060")
                    .image("https://via.placeholder.com/300x200?text=Laptop")
                    .price(1299.99)
                    .stock(5)
                    .category(gaming)
                    .views(150)
                    .sales(12)
                    .build());

            productRepository.save(Product.builder()
                    .name("Mouse Inalámbrico")
                    .description("Mouse ergonómico inalámbrico")
                    .image("https://via.placeholder.com/300x200?text=Mouse")
                    .price(29.99)
                    .stock(20)
                    .category(computacion)
                    .views(89)
                    .sales(25)
                    .build());

            productRepository.save(Product.builder()
                    .name("Teclado Mecánico")
                    .description("Teclado mecánico RGB")
                    .image("https://via.placeholder.com/300x200?text=Teclado")
                    .price(89.99)
                    .stock(15)
                    .category(gaming)
                    .views(67)
                    .sales(18)
                    .build());
        }
        
        // Crear datos de prueba de órdenes
        createTestOrders();
    }
    
    private void createTestOrders() {
        if (orderRepository.count() > 0) {
            return;
        }
        
        try {
            var user = userRepository.findByUsername("user").orElse(null);
            var products = productRepository.findAll();
            
            if (user != null && !products.isEmpty()) {
                Order order = Order.builder()
                        .user(user)
                        .totalAmount(159.98)
                        .status(OrderStatus.DELIVERED)
                        .build();
                
                order = orderRepository.save(order);
                
                OrderItem item1 = OrderItem.builder()
                        .order(order)
                        .product(products.get(0))
                        .quantity(1)
                        .unitPrice(products.get(0).getPrice())
                        .build();
                
                orderItemRepository.save(item1);
                
                if (products.size() > 1) {
                    OrderItem item2 = OrderItem.builder()
                            .order(order)
                            .product(products.get(1))
                            .quantity(2)
                            .unitPrice(products.get(1).getPrice())
                            .build();
                    
                    orderItemRepository.save(item2);
                }
            }
        } catch (Exception e) {
            System.out.println("Error creando órdenes de prueba: " + e.getMessage());
        }
    }
}