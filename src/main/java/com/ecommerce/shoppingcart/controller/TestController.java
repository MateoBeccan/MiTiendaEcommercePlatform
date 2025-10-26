package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public TestController(OrderRepository orderRepository, UserRepository userRepository, 
                         ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            status.put("users", userRepository.count());
            status.put("products", productRepository.count());
            status.put("orders", orderRepository.count());
            status.put("status", "OK");
        } catch (Exception e) {
            status.put("status", "ERROR");
            status.put("error", e.getMessage());
        }
        
        return status;
    }
    
    @PostMapping("/verify-password")
    public Map<String, Object> verifyPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            var user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                boolean matches = passwordEncoder.matches(password, user.getPassword());
                result.put("matches", matches);
                result.put("username", username);

                result.put("hashedPassword", user.getPassword().substring(0, 20) + "...");
            } else {
                result.put("error", "Usuario no encontrado");
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            var users = userRepository.findAll();
            result.put("totalUsers", users.size());
            
            result.put("activeUsers", users.size());
            
            // Mostrar algunos usuarios para debug
            var userList = new java.util.ArrayList<Map<String, Object>>();
            for (var user : users) {
                var userInfo = new HashMap<String, Object>();
                userInfo.put("username", user.getUsername());
                userInfo.put("email", user.getEmail());

                userInfo.put("role", user.getRole());
                userList.add(userInfo);
            }
            result.put("users", userList);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
}