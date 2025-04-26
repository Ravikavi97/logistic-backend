package com.logistics.repository;

import com.logistics.entity.Order;
import com.logistics.entity.Order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    
    Page<Order> findByCustomerId(String customerId, Pageable pageable);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE " +
           "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "o.customerId = :query OR " +
           "o.id = :query")
    Page<Order> search(@Param("query") String query, Pageable pageable);
    
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
} 