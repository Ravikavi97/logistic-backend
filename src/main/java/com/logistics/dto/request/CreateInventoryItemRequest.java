package com.logistics.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateInventoryItemRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price must be greater than or equal to 0")
    private Double unitPrice;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotBlank(message = "SKU is required")
    private String sku;
    
    @NotNull(message = "Minimum quantity is required")
    @Min(value = 0, message = "Minimum quantity must be greater than or equal to 0")
    private Integer minimumQuantity;
} 