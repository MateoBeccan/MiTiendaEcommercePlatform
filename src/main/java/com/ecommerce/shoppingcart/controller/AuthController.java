package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.model.Role;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.repository.*;
import com.ecommerce.shoppingcart.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, OrderRepository orderRepository, 
                         PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Validaciones
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(400).body("El nombre de usuario es requerido");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(400).body("El email es requerido");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return ResponseEntity.status(400).body("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(400).body("El nombre de usuario ya existe");
        }
        
        // Verificar si el email ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("El email ya está registrado");
        }
        
        // Asignar rol por defecto si no se especifica
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        try {
            userRepository.save(user);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String loginField = user.getUsername(); // Puede ser username o email
        
        // Buscar por username o email
        var found = userRepository.findByUsername(loginField)
                .or(() -> userRepository.findByEmail(loginField));
        
        if (found.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
        
        User foundUser = found.get();

        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generateToken(foundUser.getUsername());
        return ResponseEntity.ok(token);
    }
    
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUserRole() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var authorities = authentication.getAuthorities();
            boolean isAdmin = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            String username = authentication.getName();
            var user = userRepository.findByUsername(username).orElse(null);
            String displayName = user != null && user.getDisplayName() != null ? user.getDisplayName() : username;
            String email = user != null ? user.getEmail() : "";
            return ResponseEntity.ok("{\"role\":\"" + (isAdmin ? "ADMIN" : "USER") + "\",\"username\":\"" + username + "\",\"displayName\":\"" + displayName + "\",\"email\":\"" + email + "\"}");
        }
        return ResponseEntity.status(401).body("No autenticado");
    }
    
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody java.util.Map<String, String> request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            if (request.containsKey("displayName")) {
                user.setDisplayName(request.get("displayName"));
            }
            
            if (request.containsKey("newPassword")) {
                String currentPassword = request.get("currentPassword");
                if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
                    return ResponseEntity.status(400).body("Contraseña actual incorrecta");
                }
                user.setPassword(passwordEncoder.encode(request.get("newPassword")));
            }
            
            userRepository.save(user);
            return ResponseEntity.ok("Perfil actualizado exitosamente");
        }
        return ResponseEntity.status(401).body("No autenticado");
    }
    
    @DeleteMapping("/account")
    public ResponseEntity<String> deleteAccount(@RequestBody java.util.Map<String, String> request) {
        try {
            var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                var user = userRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
                String password = request.get("password");
                System.out.println("Eliminando cuenta para usuario: " + user.getUsername());
                System.out.println("Contraseña recibida: " + (password != null ? "[PRESENTE]" : "[NULA]"));
                
                if (password == null || password.trim().isEmpty()) {
                    return ResponseEntity.status(400).body("La contraseña es requerida");
                }
                
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    System.out.println("Contraseña no coincide para usuario: " + user.getUsername());
                    return ResponseEntity.status(400).body("Contraseña incorrecta");
                }
                
                System.out.println("Eliminando usuario: " + user.getUsername());
                
                try {
                    userRepository.delete(user);
                    System.out.println("Usuario eliminado exitosamente: " + user.getUsername());
                    return ResponseEntity.ok("Cuenta eliminada exitosamente");
                } catch (Exception e) {
                    System.out.println("Error eliminando usuario: " + e.getMessage());
                    e.printStackTrace();
                    return ResponseEntity.status(400).body("Error al eliminar la cuenta: " + e.getMessage());
                }
            }
            return ResponseEntity.status(401).body("No autenticado");
        } catch (Exception e) {
            System.out.println("Error eliminando cuenta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }
}
