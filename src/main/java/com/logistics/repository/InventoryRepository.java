package com.logistics.repository;

import com.logistics.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, String> {
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT i FROM InventoryItem i WHERE i.id = :id")
    Optional<InventoryItem> findByIdWithLock(@Param("id") String id);
    
    Page<InventoryItem> findByCategory(String category, Pageable pageable);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.minimumQuantity")
    List<InventoryItem> findLowStockItems();
    
    boolean existsBySku(String sku);
    
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.sku) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<InventoryItem> search(@Param("search") String search, Pageable pageable);
} 