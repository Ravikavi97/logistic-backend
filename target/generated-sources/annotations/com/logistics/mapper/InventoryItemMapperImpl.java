package com.logistics.mapper;

import com.logistics.dto.InventoryItemDTO;
import com.logistics.dto.request.CreateInventoryItemRequest;
import com.logistics.dto.request.UpdateInventoryItemRequest;
import com.logistics.dto.response.InventoryItemResponse;
import com.logistics.entity.InventoryItem;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-26T14:08:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class InventoryItemMapperImpl implements InventoryItemMapper {

    @Override
    public InventoryItem toEntity(CreateInventoryItemRequest request) {
        if ( request == null ) {
            return null;
        }

        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setName( request.getName() );
        inventoryItem.setDescription( request.getDescription() );
        inventoryItem.setQuantity( request.getQuantity() );
        if ( request.getUnitPrice() != null ) {
            inventoryItem.setUnitPrice( BigDecimal.valueOf( request.getUnitPrice() ) );
        }
        inventoryItem.setCategory( request.getCategory() );
        inventoryItem.setLocation( request.getLocation() );
        inventoryItem.setSku( request.getSku() );
        inventoryItem.setMinimumQuantity( request.getMinimumQuantity() );

        return inventoryItem;
    }

    @Override
    public void updateEntity(UpdateInventoryItemRequest request, InventoryItem entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            entity.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            entity.setDescription( request.getDescription() );
        }
        if ( request.getUnitPrice() != null ) {
            entity.setUnitPrice( BigDecimal.valueOf( request.getUnitPrice() ) );
        }
        if ( request.getCategory() != null ) {
            entity.setCategory( request.getCategory() );
        }
        if ( request.getLocation() != null ) {
            entity.setLocation( request.getLocation() );
        }
        if ( request.getSku() != null ) {
            entity.setSku( request.getSku() );
        }
        if ( request.getMinimumQuantity() != null ) {
            entity.setMinimumQuantity( request.getMinimumQuantity() );
        }
    }

    @Override
    public InventoryItemResponse toResponse(InventoryItem entity) {
        if ( entity == null ) {
            return null;
        }

        InventoryItemResponse inventoryItemResponse = new InventoryItemResponse();

        inventoryItemResponse.setId( entity.getId() );
        inventoryItemResponse.setName( entity.getName() );
        inventoryItemResponse.setDescription( entity.getDescription() );
        inventoryItemResponse.setQuantity( entity.getQuantity() );
        if ( entity.getUnitPrice() != null ) {
            inventoryItemResponse.setUnitPrice( entity.getUnitPrice().doubleValue() );
        }
        inventoryItemResponse.setCategory( entity.getCategory() );
        inventoryItemResponse.setLocation( entity.getLocation() );
        inventoryItemResponse.setSku( entity.getSku() );
        inventoryItemResponse.setMinimumQuantity( entity.getMinimumQuantity() );
        inventoryItemResponse.setCreatedAt( entity.getCreatedAt() );
        inventoryItemResponse.setUpdatedAt( entity.getUpdatedAt() );
        inventoryItemResponse.setVersion( entity.getVersion() );

        return inventoryItemResponse;
    }

    @Override
    public InventoryItemDTO toDTO(InventoryItem entity) {
        if ( entity == null ) {
            return null;
        }

        InventoryItemDTO inventoryItemDTO = new InventoryItemDTO();

        inventoryItemDTO.setId( entity.getId() );
        inventoryItemDTO.setName( entity.getName() );
        inventoryItemDTO.setDescription( entity.getDescription() );
        inventoryItemDTO.setQuantity( entity.getQuantity() );
        inventoryItemDTO.setUnitPrice( entity.getUnitPrice() );
        inventoryItemDTO.setCategory( entity.getCategory() );
        inventoryItemDTO.setLocation( entity.getLocation() );
        inventoryItemDTO.setSku( entity.getSku() );
        inventoryItemDTO.setMinimumQuantity( entity.getMinimumQuantity() );
        inventoryItemDTO.setCreatedAt( entity.getCreatedAt() );
        inventoryItemDTO.setUpdatedAt( entity.getUpdatedAt() );
        inventoryItemDTO.setVersion( entity.getVersion() );

        return inventoryItemDTO;
    }
}
