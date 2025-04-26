package com.logistics.repository;

import com.logistics.entity.Shipment;
import com.logistics.entity.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    
    Page<Shipment> findByOrderId(String orderId, Pageable pageable);
    
    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);
    
    Page<Shipment> findByTrackingNumber(String trackingNumber, Pageable pageable);

    @Query("SELECT s FROM Shipment s WHERE " +
            "LOWER(s.trackingNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.orderId) = LOWER(:query) OR " +
            "LOWER(s.destinationAddress) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Shipment> search(@Param("query") String query, Pageable pageable);
    List<Shipment> findByStatusOrderByCreatedAtDesc(ShipmentStatus status);

    Page<Shipment> findTopByStatusOrderByCreatedAtDesc(ShipmentStatus status, Pageable pageable);
    
    boolean existsByOrderId(String orderId);

    // Change the return type to a single Shipment and remove Pageable
//    Shipment findTop1ByStatusOrderByCreatedAtDesc(ShipmentStatus status);

} 