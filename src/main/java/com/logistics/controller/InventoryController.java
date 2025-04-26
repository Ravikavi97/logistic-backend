package com.logistics.controller;

import com.logistics.dto.InventoryItemDTO;
import com.logistics.dto.request.CreateInventoryItemRequest;
import com.logistics.dto.request.UpdateInventoryItemRequest;
import com.logistics.dto.request.UpdateQuantityRequest;
import com.logistics.dto.response.ApiResponse;
import com.logistics.dto.response.PageResponse;
import com.logistics.service.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InventoryItemDTO>>> getAllItems(
            Pageable pageable,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch all inventory items with pagination: {}", requestId, pageable);
        
        try {
            Page<InventoryItemDTO> items = inventoryService.getAllItems(pageable, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} inventory items in {} ms", 
                    requestId, items.getTotalElements(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.successPage(
                items.getContent(),
                items.getTotalElements(),
                items.getNumber(),
                items.getSize(),
                items.getTotalPages(),
                "Successfully retrieved inventory items",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch inventory items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> getItem(
            @PathVariable String id,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch inventory item with ID: {}", requestId, id);
        
        try {
            InventoryItemDTO item = inventoryService.getItemById(id, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched inventory item in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                item,
                "Successfully retrieved inventory item",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemDTO>> createItem(
            @Valid @RequestBody CreateInventoryItemRequest request,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to create inventory item with data: {}", requestId, request);
        
        try {
            InventoryItemDTO item = inventoryService.createItem(request, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully created inventory item with ID: {} in {} ms", 
                    requestId, item.getId(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                item,
                "Successfully created inventory item",
                httpRequest.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to create inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> updateItem(
            @PathVariable String id,
            @Valid @RequestBody UpdateInventoryItemRequest request,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update inventory item with ID: {} and data: {}", 
                requestId, id, request);
        
        try {
            InventoryItemDTO item = inventoryService.updateItem(id, request, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated inventory item in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                item,
                "Successfully updated inventory item",
                httpRequest.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<ApiResponse<InventoryItemDTO>> updateQuantity(
            @PathVariable String id,
            @Valid @RequestBody UpdateQuantityRequest request,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update quantity for inventory item with ID: {} to quantity: {}", 
                requestId, id, request.getQuantity());
        
        try {
            InventoryItemDTO item = inventoryService.updateQuantity(id, request.getQuantity(), requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated inventory item quantity in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                item,
                "Successfully updated inventory item quantity",
                httpRequest.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update inventory item quantity after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable String id,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to delete inventory item with ID: {}", requestId, id);
        
        try {
            inventoryService.deleteItem(id, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully deleted inventory item in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                null,
                "Successfully deleted inventory item",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to delete inventory item after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDTO>>> getLowStockItems(
            @RequestParam(defaultValue = "10") int threshold,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch low stock items with threshold: {}", requestId, threshold);
        
        try {
            List<InventoryItemDTO> items = inventoryService.getLowStockItems(threshold, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} low stock items in {} ms", 
                    requestId, items.size(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                items,
                "Successfully retrieved low stock items",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch low stock items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<InventoryItemDTO>>> searchItems(
            @RequestParam String query,
            Pageable pageable,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to search inventory items with query: {} and pagination: {}", 
                requestId, query, pageable);
        
        try {
            Page<InventoryItemDTO> items = inventoryService.searchItems(query, pageable, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} inventory items from search in {} ms", 
                    requestId, items.getTotalElements(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.successPage(
                items.getContent(),
                items.getTotalElements(),
                items.getNumber(),
                items.getSize(),
                items.getTotalPages(),
                "Successfully retrieved inventory items",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to search inventory items after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }
} 