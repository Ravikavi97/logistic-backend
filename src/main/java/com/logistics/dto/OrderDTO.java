package com.logistics.dto;

import com.logistics.entity.Order.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotNull(message = "Status is required")
    private OrderStatus status;
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> items;
    
    private String shippingAddress;
    private String billingAddress;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    
    @Data
    public static class OrderItemDTO {
        @NotBlank(message = "Product ID is required")
        private String productId;
        
        @NotBlank(message = "Product name is required")
        private String productName;
        
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;
        
        @NotNull(message = "Unit price is required")
        @Positive(message = "Unit price must be positive")
        private BigDecimal unitPrice;
        
        @NotNull(message = "Total price is required")
        @Positive(message = "Total price must be positive")
        private BigDecimal totalPrice;
    }
} 