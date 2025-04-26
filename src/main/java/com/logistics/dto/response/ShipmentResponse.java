package com.logistics.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShipmentResponse {
    private String id;
    private String orderId;
    private String trackingNumber;
    private String status;
    private String originAddress;
    private String destinationAddress;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ShipmentItemResponse> items;
    private List<ShipmentStatusHistoryResponse> statusHistory;

    @Data
    public static class ShipmentItemResponse {
        private String itemId;
        private String itemName;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
    }

    @Data
    public static class ShipmentStatusHistoryResponse {
        private String status;
        private String location;
        private LocalDateTime timestamp;
        private String notes;
    }
} 