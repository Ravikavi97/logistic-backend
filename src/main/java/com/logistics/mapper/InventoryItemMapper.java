package com.logistics.mapper;

import com.logistics.dto.InventoryItemDTO;
import com.logistics.dto.request.CreateInventoryItemRequest;
import com.logistics.dto.request.UpdateInventoryItemRequest;
import com.logistics.dto.response.InventoryItemResponse;
import com.logistics.entity.InventoryItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    InventoryItem toEntity(CreateInventoryItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(UpdateInventoryItemRequest request, @MappingTarget InventoryItem entity);

    InventoryItemResponse toResponse(InventoryItem entity);

    InventoryItemDTO toDTO(InventoryItem entity);
}