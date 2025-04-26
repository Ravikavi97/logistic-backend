package com.logistics.controller;

import com.logistics.dto.ShipmentDTO;
import com.logistics.dto.request.CreateShipmentRequest;
import com.logistics.dto.request.UpdateShipmentRequest;
import com.logistics.dto.response.ApiResponse;
import com.logistics.dto.response.PageResponse;
import com.logistics.service.ShipmentService;
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
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ShipmentDTO>>> getAllShipments(
            Pageable pageable,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch all shipments with pagination: {}", requestId, pageable);
        
        try {
            Page<ShipmentDTO> shipments = shipmentService.getAllShipments(pageable, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.successPage(
                shipments.getContent(),
                shipments.getTotalElements(),
                shipments.getNumber(),
                shipments.getSize(),
                shipments.getTotalPages(),
                "Successfully retrieved shipments",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentDTO>> getShipment(
            @PathVariable String id,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipment with ID: {}", requestId, id);
        
        try {
            ShipmentDTO shipment = shipmentService.getShipmentById(id, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipment,
                "Successfully retrieved shipment",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShipmentDTO>> createShipment(
            @Valid @RequestBody CreateShipmentRequest request,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to create shipment with data: {}", requestId, request);
        
        try {
            ShipmentDTO shipment = shipmentService.createShipment(request, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully created shipment with ID: {} in {} ms", 
                    requestId, shipment.getId(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipment,
                "Successfully created shipment",
                httpRequest.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to create shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentDTO>> updateShipment(
            @PathVariable String id,
            @Valid @RequestBody UpdateShipmentRequest request,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update shipment with ID: {} and data: {}", 
                requestId, id, request);
        
        try {
            ShipmentDTO shipment = shipmentService.updateShipment(id, request, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipment,
                "Successfully updated shipment",
                httpRequest.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ShipmentDTO>> updateShipmentStatus(
            @PathVariable String id,
            @RequestParam String status,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update shipment status with ID: {} to status: {}", 
                requestId, id, status);
        
        try {
            ShipmentDTO shipment = shipmentService.updateShipmentStatus(id, status, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated shipment status in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipment,
                "Successfully updated shipment status",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update shipment status after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShipment(
            @PathVariable String id,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to delete shipment with ID: {}", requestId, id);
        
        try {
            shipmentService.deleteShipment(id, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully deleted shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                null,
                "Successfully deleted shipment",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to delete shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<ShipmentDTO>>> getShipmentsByOrder(
            @PathVariable String orderId,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipments for order ID: {}", requestId, orderId);
        
        try {
            List<ShipmentDTO> shipments = shipmentService.getShipmentsByOrder(orderId, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.size(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipments,
                "Successfully retrieved shipments for order",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments for order after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentDTO>>> getShipmentsByStatus(
            @PathVariable String status,
            Pageable pageable,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipments with status: {} and pagination: {}", 
                requestId, status, pageable);
        
        try {
            Page<ShipmentDTO> shipments = shipmentService.getShipmentsByStatus(status, pageable, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.successPage(
                shipments.getContent(),
                shipments.getTotalElements(),
                shipments.getNumber(),
                shipments.getSize(),
                shipments.getTotalPages(),
                "Successfully retrieved shipments by status",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments by status after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ApiResponse<ShipmentDTO>> getShipmentByTrackingNumber(
            @PathVariable String trackingNumber,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipment with tracking number: {}", requestId, trackingNumber);
        
        try {
            ShipmentDTO shipment = shipmentService.getShipmentByTrackingNumber(trackingNumber, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipment,
                "Successfully retrieved shipment by tracking number",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipment by tracking number after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentDTO>>> searchShipments(
            @RequestParam String query,
            Pageable pageable,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to search shipments with query: {} and pagination: {}", 
                requestId, query, pageable);
        
        try {
            Page<ShipmentDTO> shipments = shipmentService.searchShipments(query, pageable, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.successPage(
                shipments.getContent(),
                shipments.getTotalElements(),
                shipments.getNumber(),
                shipments.getSize(),
                shipments.getTotalPages(),
                "Successfully retrieved shipments by search",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to search shipments after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/recent/{status}")
    public ResponseEntity<ApiResponse<List<ShipmentDTO>>> getRecentShipmentsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "5") int limit,
            HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch recent {} shipments with status: {}", 
                requestId, limit, status);
        
        try {
            List<ShipmentDTO> shipments = shipmentService.getRecentShipmentsByStatus(status, limit, requestId);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.size(), duration.toMillis());
            
            return ResponseEntity.ok(ApiResponse.success(
                shipments,
                "Successfully retrieved recent shipments by status",
                request.getRequestURI(),
                requestId
            ));
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch recent shipments by status after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }
} 