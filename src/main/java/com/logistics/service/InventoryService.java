package com.logistics.service;

import com.logistics.dto.InventoryItemDTO;
import com.logistics.dto.request.CreateInventoryItemRequest;
import com.logistics.dto.request.UpdateInventoryItemRequest;
import com.logistics.entity.InventoryItem;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.mapper.InventoryItemMapper;
import com.logistics.repository.InventoryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@Transactional
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemMapper inventoryItemMapper;

    @Autowired
    public InventoryService(InventoryItemRepository inventoryItemRepository, InventoryItemMapper inventoryItemMapper) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
    }

    @Cacheable(value = "inventoryItems", key = "#pageable")
    public Page<InventoryItemDTO> getAllItems(Pageable pageable, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch all inventory items with pagination: {}", requestId, pageable);
        
        try {
            Page<InventoryItem> items = inventoryItemRepository.findAll(pageable);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} inventory items in {} ms", 
                    requestId, items.getTotalElements(), duration.toMillis());
            
            return items.map(inventoryItemMapper::toDTO);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch inventory items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "inventoryItem", key = "#id")
    public InventoryItemDTO getItemById(String id, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch inventory item with ID: {}", requestId, id);
        
        try {
            InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched inventory item in {} ms", 
                    requestId, duration.toMillis());
            
            return inventoryItemMapper.toDTO(item);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"inventoryItems", "inventoryItem"}, allEntries = true)
    public InventoryItemDTO createItem(CreateInventoryItemRequest request, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to create inventory item with data: {}", requestId, request);
        
        try {
            InventoryItem item = inventoryItemMapper.toEntity(request);
            item = inventoryItemRepository.save(item);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully created inventory item with ID: {} in {} ms", 
                    requestId, item.getId(), duration.toMillis());
            
            return inventoryItemMapper.toDTO(item);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to create inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"inventoryItems", "inventoryItem"}, allEntries = true)
    public InventoryItemDTO updateItem(String id, UpdateInventoryItemRequest request, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update inventory item with ID: {} and data: {}", 
                requestId, id, request);
        
        try {
            InventoryItem existingItem = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
            
            inventoryItemMapper.updateEntity(request, existingItem);
            InventoryItem updatedItem = inventoryItemRepository.save(existingItem);
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated inventory item in {} ms", 
                    requestId, duration.toMillis());
            
            return inventoryItemMapper.toDTO(updatedItem);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"inventoryItems", "inventoryItem"}, allEntries = true)
    public InventoryItemDTO updateQuantity(String id, int quantity, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update quantity for inventory item with ID: {} to quantity: {}", 
                requestId, id, quantity);
        
        try {
            InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
            
            item.setQuantity(quantity);
            item = inventoryItemRepository.save(item);
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated inventory item quantity in {} ms", 
                    requestId, duration.toMillis());
            
            return inventoryItemMapper.toDTO(item);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update inventory item quantity after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"inventoryItems", "inventoryItem"}, allEntries = true)
    public void deleteItem(String id, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to delete inventory item with ID: {}", requestId, id);
        
        try {
            InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
            
            inventoryItemRepository.delete(item);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully deleted inventory item in {} ms", 
                    requestId, duration.toMillis());
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to delete inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "lowStockItems", key = "#threshold")
    public List<InventoryItemDTO> getLowStockItems(int threshold, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch low stock items with threshold: {}", requestId, threshold);
        
        try {
            List<InventoryItem> items = inventoryItemRepository.findByQuantityLessThanEqual(threshold);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} low stock items in {} ms", 
                    requestId, items.size(), duration.toMillis());
            
            return items.stream()
                .map(inventoryItemMapper::toDTO)
                .toList();
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch low stock items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "inventorySearch", key = "#query + '-' + #pageable")
    public Page<InventoryItemDTO> searchItems(String query, Pageable pageable, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to search inventory items with query: {} and pagination: {}", 
                requestId, query, pageable);
        
        try {
            Page<InventoryItem> items = inventoryItemRepository.search(query, pageable);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} inventory items in {} ms", 
                    requestId, items.getTotalElements(), duration.toMillis());
            
            return items.map(inventoryItemMapper::toDTO);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to search inventory items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    public Page<InventoryItemDTO> getLowStockItems(Pageable pageable) {
        log.info("Fetching low stock items with pageable: {}", pageable);
        try {
            Page<InventoryItem> items = inventoryItemRepository.findLowStockItems(pageable);
            return items.map(inventoryItemMapper::toDTO);
        } catch (Exception e) {
            log.error("Error fetching low stock items", e);
            throw new RuntimeException("Failed to fetch low stock items", e);
        }
    }
} 