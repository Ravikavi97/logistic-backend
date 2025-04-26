package com.logistics.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateShipmentRequest {
    @NotBlank
    private String orderId;
    
    @NotBlank
    private String trackingNumber;
    
    @NotBlank
    private String originAddress;
    
    @NotBlank
    private String destinationAddress;
    
    @NotNull
    private LocalDateTime expectedDeliveryDate;
    
    private LocalDateTime actualDeliveryDate;
    
    @NotEmpty
    @Valid
    private List<ShipmentItemRequest> items;

    @Data
    public static class ShipmentItemRequest {
        @NotBlank
        private String itemId;
        
        @NotBlank
        private String itemName;
        
        @NotNull
        private Integer quantity;
        
        @NotNull
        private Double unitPrice;
    }
} 