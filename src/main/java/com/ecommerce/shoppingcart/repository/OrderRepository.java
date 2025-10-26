package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.model.Order;
import com.ecommerce.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    Double getTotalRevenue();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DELIVERED'")
    Long getTotalCompletedOrders();
    
    @Query("SELECT COUNT(DISTINCT o.user) FROM Order o WHERE o.status = 'DELIVERED'")
    Long getTotalUniqueCustomers();
    
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllByOrderByOrderDateDesc();
    
    List<Order> findByUser(User user);
}