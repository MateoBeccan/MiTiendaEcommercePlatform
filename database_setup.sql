-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- Tabla de categorías
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
);

-- Tabla de productos
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    image VARCHAR(500) NOT NULL,
    category_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    views INT NOT NULL DEFAULT 0,
    sales INT NOT NULL DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Tabla de órdenes
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DOUBLE NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla de items de orden
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Insertar datos de prueba
INSERT IGNORE INTO categories (name, description) VALUES 
('Electrónicos', 'Dispositivos electrónicos'),
('Computación', 'Productos de computación'),
('Gaming', 'Productos para gaming');

INSERT IGNORE INTO users (username, email, password, display_name, role) VALUES 
('admin', 'admin@mitienda.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador', 'ADMIN'),
('user', 'user@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario Demo', 'USER');

INSERT IGNORE INTO products (name, description, price, stock, image, category_id, views, sales) VALUES 
('Laptop Gaming', 'Laptop para gaming con RTX 4060', 1299.99, 5, 'https://via.placeholder.com/300x200?text=Laptop', 3, 150, 12),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico', 29.99, 20, 'https://via.placeholder.com/300x200?text=Mouse', 2, 89, 25),
('Teclado Mecánico', 'Teclado mecánico RGB', 89.99, 15, 'https://via.placeholder.com/300x200?text=Teclado', 3, 67, 18);