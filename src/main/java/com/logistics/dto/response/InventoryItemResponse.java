package com.logistics.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryItemResponse {
    private String id;
    private String name;
    private String description;
    private Integer quantity;
    private Double unitPrice;
    private String category;
    private String location;
    private String sku;
    private Integer minimumQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
} 