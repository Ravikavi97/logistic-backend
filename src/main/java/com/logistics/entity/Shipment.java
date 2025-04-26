package com.logistics.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @Column(name = "origin_address", nullable = false)
    private String originAddress;

    @Column(name = "destination_address", nullable = false)
    private String destinationAddress;

    @Column(name = "recipient_name") // Add this field
    private String recipientName;

    @Column(name = "expected_delivery_date", nullable = false)
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void addItem(ShipmentItem item) {
        items.add(item);
        item.setShipment(this);
    }

    public void removeItem(ShipmentItem item) {
        items.remove(item);
        item.setShipment(null);
    }
} 