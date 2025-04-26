package com.logistics.dto;

import com.logistics.entity.ShipmentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShipmentDTO {
    private String id;
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;
    
    @NotNull(message = "Status is required")
    private ShipmentStatus status;
    
    @NotBlank(message = "Carrier is required")
    private String carrier;
    
    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;
    
    @NotBlank(message = "Origin address is required")
    private String originAddress;
    
    @NotBlank(message = "Destination address is required")
    private String destinationAddress;
    
    @NotBlank(message = "Recipient name is required")
    private String recipientName;
    
    @NotBlank(message = "Recipient phone is required")
    private String recipientPhone;
    
    private String estimatedDeliveryDate;
    private String actualDeliveryDate;
    private String notes;
    
    @Valid
    private List<TrackingEventDTO> trackingEvents;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    
    @Data
    public static class TrackingEventDTO {
        @NotNull(message = "Timestamp is required")
        private LocalDateTime timestamp;
        
        @NotNull(message = "Status is required")
        private ShipmentStatus status;
        
        @NotBlank(message = "Description is required")
        private String description;
        
        private String location;
        private String notes;
    }

    @Data
    public static class ShipmentItemDTO {
        private String id;
        private String itemId;
        private String itemName;
        private int quantity;
        private double unitPrice;
    }
} 