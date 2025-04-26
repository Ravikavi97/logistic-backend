package com.logistics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryItemDTO {
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price cannot be negative")
    private BigDecimal unitPrice;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotBlank(message = "SKU is required")
    private String sku;
    
    @Min(value = 0, message = "Minimum quantity cannot be negative")
    private Integer minimumQuantity;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
} 