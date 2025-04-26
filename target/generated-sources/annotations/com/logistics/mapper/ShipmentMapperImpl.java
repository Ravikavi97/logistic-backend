package com.logistics.mapper;

import com.logistics.dto.ShipmentDTO;
import com.logistics.dto.request.CreateShipmentRequest;
import com.logistics.dto.request.UpdateShipmentRequest;
import com.logistics.dto.response.ShipmentResponse;
import com.logistics.entity.Shipment;
import com.logistics.entity.ShipmentItem;
import com.logistics.entity.ShipmentStatus;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-26T05:15:34+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class ShipmentMapperImpl implements ShipmentMapper {

    @Override
    public Shipment toEntity(CreateShipmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Shipment shipment = new Shipment();

        shipment.setItems( mapItemsToEntity( request.getItems() ) );
        shipment.setOrderId( request.getOrderId() );
        shipment.setTrackingNumber( request.getTrackingNumber() );
        shipment.setOriginAddress( request.getOriginAddress() );
        shipment.setDestinationAddress( request.getDestinationAddress() );
        shipment.setRecipientName( request.getRecipientName() );
        shipment.setExpectedDeliveryDate( request.getExpectedDeliveryDate() );

        shipment.setStatus( com.logistics.entity.ShipmentStatus.PENDING );

        linkShipmentItems( shipment );

        return shipment;
    }

    @Override
    public void updateEntity(UpdateShipmentRequest request, Shipment entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getOrderId() != null ) {
            entity.setOrderId( request.getOrderId() );
        }
        if ( request.getTrackingNumber() != null ) {
            entity.setTrackingNumber( request.getTrackingNumber() );
        }
        if ( request.getOriginAddress() != null ) {
            entity.setOriginAddress( request.getOriginAddress() );
        }
        if ( request.getDestinationAddress() != null ) {
            entity.setDestinationAddress( request.getDestinationAddress() );
        }
        if ( request.getExpectedDeliveryDate() != null ) {
            entity.setExpectedDeliveryDate( request.getExpectedDeliveryDate() );
        }
        if ( request.getActualDeliveryDate() != null ) {
            entity.setActualDeliveryDate( request.getActualDeliveryDate() );
        }
        if ( entity.getItems() != null ) {
            List<ShipmentItem> list = updateShipmentItems( request.getItems() );
            if ( list != null ) {
                entity.getItems().clear();
                entity.getItems().addAll( list );
            }
        }
        else {
            List<ShipmentItem> list = updateShipmentItems( request.getItems() );
            if ( list != null ) {
                entity.setItems( list );
            }
        }

        linkShipmentItems( entity );
    }

    @Override
    public ShipmentResponse toResponse(Shipment entity) {
        if ( entity == null ) {
            return null;
        }

        ShipmentResponse shipmentResponse = new ShipmentResponse();

        shipmentResponse.setItems( shipmentItemListToShipmentItemResponseList( entity.getItems() ) );
        shipmentResponse.setStatus( mapStatusToString( entity.getStatus() ) );
        shipmentResponse.setId( entity.getId() );
        shipmentResponse.setOrderId( entity.getOrderId() );
        shipmentResponse.setTrackingNumber( entity.getTrackingNumber() );
        shipmentResponse.setOriginAddress( entity.getOriginAddress() );
        shipmentResponse.setDestinationAddress( entity.getDestinationAddress() );
        shipmentResponse.setExpectedDeliveryDate( entity.getExpectedDeliveryDate() );
        shipmentResponse.setActualDeliveryDate( entity.getActualDeliveryDate() );
        shipmentResponse.setCreatedAt( entity.getCreatedAt() );
        shipmentResponse.setUpdatedAt( entity.getUpdatedAt() );

        shipmentResponse.setStatusHistory( mapStatusToHistory(entity) );

        return shipmentResponse;
    }

    @Override
    public ShipmentDTO toDTO(Shipment entity) {
        if ( entity == null ) {
            return null;
        }

        ShipmentDTO shipmentDTO = new ShipmentDTO();

        if ( entity.getExpectedDeliveryDate() != null ) {
            shipmentDTO.setEstimatedDeliveryDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getExpectedDeliveryDate() ) );
        }
        shipmentDTO.setRecipientName( entity.getRecipientName() );
        if ( entity.getStatus() != null ) {
            shipmentDTO.setStatus( Enum.valueOf( ShipmentStatus.class, mapStatusToString( entity.getStatus() ) ) );
        }
        shipmentDTO.setId( entity.getId() );
        shipmentDTO.setOrderId( entity.getOrderId() );
        shipmentDTO.setTrackingNumber( entity.getTrackingNumber() );
        shipmentDTO.setOriginAddress( entity.getOriginAddress() );
        shipmentDTO.setDestinationAddress( entity.getDestinationAddress() );
        if ( entity.getActualDeliveryDate() != null ) {
            shipmentDTO.setActualDeliveryDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getActualDeliveryDate() ) );
        }
        shipmentDTO.setCreatedAt( entity.getCreatedAt() );
        shipmentDTO.setUpdatedAt( entity.getUpdatedAt() );
        shipmentDTO.setVersion( entity.getVersion() );

        return shipmentDTO;
    }

    @Override
    public ShipmentItem toShipmentItem(CreateShipmentRequest.ShipmentItemRequest request) {
        if ( request == null ) {
            return null;
        }

        ShipmentItem shipmentItem = new ShipmentItem();

        shipmentItem.setItemId( request.getItemId() );
        shipmentItem.setItemName( request.getItemName() );
        if ( request.getQuantity() != null ) {
            shipmentItem.setQuantity( request.getQuantity() );
        }
        if ( request.getUnitPrice() != null ) {
            shipmentItem.setUnitPrice( request.getUnitPrice() );
        }

        return shipmentItem;
    }

    @Override
    public ShipmentItem toShipmentItem(UpdateShipmentRequest.ShipmentItemRequest request) {
        if ( request == null ) {
            return null;
        }

        ShipmentItem shipmentItem = new ShipmentItem();

        shipmentItem.setItemId( request.getItemId() );
        shipmentItem.setItemName( request.getItemName() );
        if ( request.getQuantity() != null ) {
            shipmentItem.setQuantity( request.getQuantity() );
        }
        if ( request.getUnitPrice() != null ) {
            shipmentItem.setUnitPrice( request.getUnitPrice() );
        }

        return shipmentItem;
    }

    @Override
    public ShipmentResponse.ShipmentItemResponse toShipmentItemResponse(ShipmentItem item) {
        if ( item == null ) {
            return null;
        }

        ShipmentResponse.ShipmentItemResponse shipmentItemResponse = new ShipmentResponse.ShipmentItemResponse();

        shipmentItemResponse.setItemId( item.getItemId() );
        shipmentItemResponse.setItemName( item.getItemName() );
        shipmentItemResponse.setQuantity( item.getQuantity() );
        shipmentItemResponse.setUnitPrice( item.getUnitPrice() );

        shipmentItemResponse.setTotalPrice( calculateTotalPrice(item) );

        return shipmentItemResponse;
    }

    protected List<ShipmentResponse.ShipmentItemResponse> shipmentItemListToShipmentItemResponseList(List<ShipmentItem> list) {
        if ( list == null ) {
            return null;
        }

        List<ShipmentResponse.ShipmentItemResponse> list1 = new ArrayList<ShipmentResponse.ShipmentItemResponse>( list.size() );
        for ( ShipmentItem shipmentItem : list ) {
            list1.add( toShipmentItemResponse( shipmentItem ) );
        }

        return list1;
    }
}
