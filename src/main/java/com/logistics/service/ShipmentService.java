package com.logistics.service;

import com.logistics.dto.ShipmentDTO;
import com.logistics.dto.request.CreateShipmentRequest;
import com.logistics.dto.request.UpdateShipmentRequest;
import com.logistics.entity.Shipment;
import com.logistics.entity.ShipmentStatus;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.mapper.ShipmentMapper;
import com.logistics.repository.ShipmentRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    @Cacheable(value = "shipments", key = "#pageable")
    public Page<ShipmentDTO> getAllShipments(Pageable pageable, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch all shipments with pagination: {}", requestId, pageable);
        
        try {
            Page<Shipment> shipments = shipmentRepository.findAll(pageable);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return shipments.map(shipmentMapper::toDTO);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "shipment", key = "#id")
    public ShipmentDTO getShipmentById(String id, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipment with ID: {}", requestId, id);
        
        try {
            Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return shipmentMapper.toDTO(shipment);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"shipments", "shipment"}, allEntries = true)
    public ShipmentDTO createShipment(CreateShipmentRequest request, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to create shipment with data: {}", requestId, request);
        
        try {
            Shipment shipment = shipmentMapper.toEntity(request);
            shipment = shipmentRepository.save(shipment);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully created shipment with ID: {} in {} ms", 
                    requestId, shipment.getId(), duration.toMillis());
            
            return shipmentMapper.toDTO(shipment);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to create shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"shipments", "shipment"}, allEntries = true)
    public ShipmentDTO updateShipment(String id, UpdateShipmentRequest request, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update shipment with ID: {} and data: {}", 
                requestId, id, request);
        
        try {
            Shipment existingShipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
            
            shipmentMapper.updateEntity(request, existingShipment);
            Shipment updatedShipment = shipmentRepository.save(existingShipment);
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return shipmentMapper.toDTO(updatedShipment);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"shipments", "shipment"}, allEntries = true)
    public ShipmentDTO updateShipmentStatus(String id, String status, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to update shipment status with ID: {} to status: {}", 
                requestId, id, status);
        
        try {
            Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
            
            shipment.setStatus(ShipmentStatus.valueOf(status.toUpperCase()));
            shipment = shipmentRepository.save(shipment);
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully updated shipment status in {} ms", 
                    requestId, duration.toMillis());
            
            return shipmentMapper.toDTO(shipment);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to update shipment status after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @CacheEvict(value = {"shipments", "shipment"}, allEntries = true)
    public void deleteShipment(String id, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to delete shipment with ID: {}", requestId, id);
        
        try {
            Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
            
            shipmentRepository.delete(shipment);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully deleted shipment in {} ms", 
                    requestId, duration.toMillis());
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to delete shipment after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "shipmentsByOrder", key = "#orderId")
    public List<ShipmentDTO> getShipmentsByOrder(String orderId, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipments for order ID: {}", requestId, orderId);
        
        try {
            List<Shipment> shipments = shipmentRepository.findByOrderId(orderId, Pageable.unpaged()).getContent();
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.size(), duration.toMillis());
            
            return shipments.stream()
                .map(shipmentMapper::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments for order after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "shipmentsByStatus", key = "#status + '-' + #pageable")
    public Page<ShipmentDTO> getShipmentsByStatus(String status, Pageable pageable, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipments with status: {} and pagination: {}", 
                requestId, status, pageable);
        
        try {
            Page<Shipment> shipments = shipmentRepository.findByStatus(ShipmentStatus.valueOf(status.toUpperCase()), pageable);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return shipments.map(shipmentMapper::toDTO);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipments by status after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "shipmentByTracking", key = "#trackingNumber")
    public ShipmentDTO getShipmentByTrackingNumber(String trackingNumber, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch shipment with tracking number: {}", requestId, trackingNumber);
        
        try {
            Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber, Pageable.unpaged())
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with tracking number: " + trackingNumber));
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return shipmentMapper.toDTO(shipment);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch shipment by tracking number after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "shipmentSearch", key = "#query + '-' + #pageable")
    public Page<ShipmentDTO> searchShipments(String query, Pageable pageable, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to search shipments with query: {} and pagination: {}", 
                requestId, query, pageable);
        
        try {
            Page<Shipment> shipments = shipmentRepository.search(query, pageable);
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched {} shipments in {} ms", 
                    requestId, shipments.getTotalElements(), duration.toMillis());
            
            return shipments.map(shipmentMapper::toDTO);
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to search shipments after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

    @Cacheable(value = "recentShipments", key = "#status + '-' + #limit")
    public List<ShipmentDTO> getRecentShipmentsByStatus(String status, int limit, String requestId) {
        Instant start = Instant.now();
        log.info("[RequestId: {}] Starting to fetch recent shipments with status: {} and limit: {}", 
                requestId, status, limit);
        
        try {
            Pageable pageable = Pageable.ofSize(limit).withPage(0);
            Page<Shipment> shipmentPage = shipmentRepository.findTopByStatusOrderByCreatedAtDesc(
                    ShipmentStatus.valueOf(status.toUpperCase()), pageable);
            Shipment shipment = shipmentPage.getContent().stream().findFirst().orElse(null);
            
            Duration duration = Duration.between(start, Instant.now());
            log.info("[RequestId: {}] Successfully fetched recent shipment in {} ms", 
                    requestId, duration.toMillis());
            
            return shipment != null ? List.of(shipmentMapper.toDTO(shipment)) : List.of();
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            log.error("[RequestId: {}] Failed to fetch recent shipments after {} ms. Error: {}", 
                    requestId, duration.toMillis(), e.getMessage(), e);
            throw e;
        }
    }

} 