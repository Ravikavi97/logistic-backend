package com.logistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateShipmentStatusRequest {
    @NotBlank
    private String status;
    
    @NotBlank
    private String location;
    
    @NotNull
    private LocalDateTime timestamp;
    
    private String notes;
} 