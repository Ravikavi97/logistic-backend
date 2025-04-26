package com.logistics.repository;

import com.logistics.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.minimumQuantity")
    Page<InventoryItem> findLowStockItems(Pageable pageable);
    
    List<InventoryItem> findByQuantityLessThanEqual(int quantity);
    
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<InventoryItem> search(String searchTerm, Pageable pageable);
} 