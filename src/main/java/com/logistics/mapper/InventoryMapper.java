package com.logistics.mapper;

import com.logistics.dto.InventoryItemDTO;
import com.logistics.entity.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    
    public InventoryItemDTO toDTO(InventoryItem entity) {
        if (entity == null) {
            return null;
        }
        
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setCategory(entity.getCategory());
        dto.setLocation(entity.getLocation());
        dto.setSku(entity.getSku());
        dto.setMinimumQuantity(entity.getMinimumQuantity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setVersion(entity.getVersion());
        return dto;
    }
    
    public InventoryItem toEntity(InventoryItemDTO dto) {
        if (dto == null) {
            return null;
        }
        
        InventoryItem entity = new InventoryItem();
        updateEntity(entity, dto);
        return entity;
    }
    
    public void updateEntity(InventoryItem entity, InventoryItemDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setCategory(dto.getCategory());
        entity.setLocation(dto.getLocation());
        entity.setSku(dto.getSku());
        entity.setMinimumQuantity(dto.getMinimumQuantity());
        // Don't update id, createdAt, updatedAt as they are managed by JPA
    }
} 