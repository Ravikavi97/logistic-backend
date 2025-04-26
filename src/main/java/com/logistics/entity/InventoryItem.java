package com.logistics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_items", indexes = {
    @Index(name = "idx_inventory_sku", columnList = "sku", unique = true),
    @Index(name = "idx_inventory_category", columnList = "category"),
    @Index(name = "idx_inventory_name", columnList = "name"),
    @Index(name = "idx_inventory_quantity", columnList = "quantity")
})
public class InventoryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String sku;

    @Min(value = 0, message = "Minimum quantity cannot be negative")
    private Integer minimumQuantity;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Optimistic locking to handle concurrent updates
    @Version
    private Long version;
} 