package com.logistics.mapper;

import com.logistics.dto.OrderDTO;
import com.logistics.entity.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    public OrderDTO toDTO(Order entity) {
        if (entity == null) {
            return null;
        }
        
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setStatus(entity.getStatus());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setItems(entity.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList()));
        dto.setShippingAddress(entity.getShippingAddress());
        dto.setBillingAddress(entity.getBillingAddress());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setVersion(entity.getVersion());
        return dto;
    }
    
    public Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Order entity = new Order();
        updateEntity(entity, dto);
        return entity;
    }
    
    public void updateEntity(Order entity, OrderDTO dto) {
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setStatus(dto.getStatus());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setItems(dto.getItems().stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList()));
        entity.setShippingAddress(dto.getShippingAddress());
        entity.setBillingAddress(dto.getBillingAddress());
        entity.setNotes(dto.getNotes());
    }
    
    private OrderDTO.OrderItemDTO toItemDTO(Order.OrderItem entity) {
        OrderDTO.OrderItemDTO dto = new OrderDTO.OrderItemDTO();
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        return dto;
    }
    
    private Order.OrderItem toItemEntity(OrderDTO.OrderItemDTO dto) {
        Order.OrderItem entity = new Order.OrderItem();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setTotalPrice(dto.getTotalPrice());
        return entity;
    }
} 