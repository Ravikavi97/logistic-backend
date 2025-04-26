package com.logistics.mapper;

import com.logistics.dto.ShipmentDTO;
import com.logistics.dto.request.CreateShipmentRequest;
import com.logistics.dto.request.UpdateShipmentRequest;
import com.logistics.dto.response.ShipmentResponse;
import com.logistics.entity.Shipment;
import com.logistics.entity.ShipmentItem;
import com.logistics.entity.ShipmentStatus;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShipmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.logistics.entity.ShipmentStatus.PENDING)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actualDeliveryDate", ignore = true)
    @Mapping(target = "items", source = "items", qualifiedByName = "mapItemsToEntity")
    Shipment toEntity(CreateShipmentRequest request);

    @AfterMapping
    default void linkShipmentItems(@MappingTarget Shipment shipment) {
        if (shipment.getItems() != null) {
            List<ShipmentItem> items = new ArrayList<>(shipment.getItems());
            shipment.getItems().clear(); // Clear to rebuild with bidirectional relationship
            for (ShipmentItem item : items) {
                item.setShipment(shipment);
                shipment.addItem(item); // Maintain bidirectional relationship
            }
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(UpdateShipmentRequest request, @MappingTarget Shipment entity);

    @Mapping(target = "items", source = "items")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusToString")
    @Mapping(target = "statusHistory", expression = "java(mapStatusToHistory(entity))")
    ShipmentResponse toResponse(Shipment entity);

    @Mapping(target = "estimatedDeliveryDate", source = "expectedDeliveryDate")
    @Mapping(target = "trackingEvents", ignore = true)
    @Mapping(target = "carrier", ignore = true)
    @Mapping(target = "shippingMethod", ignore = true)
    @Mapping(target = "recipientName", source = "recipientName")
    @Mapping(target = "recipientPhone", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatusToString")
    ShipmentDTO toDTO(Shipment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ShipmentItem toShipmentItem(CreateShipmentRequest.ShipmentItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    ShipmentItem toShipmentItem(UpdateShipmentRequest.ShipmentItemRequest request);

    @Mapping(target = "itemId", source = "itemId")
    @Mapping(target = "itemName", source = "itemName")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(item))")
    ShipmentResponse.ShipmentItemResponse toShipmentItemResponse(ShipmentItem item);

    @Named("mapItemsToEntity")
    default List<ShipmentItem> mapItemsToEntity(List<CreateShipmentRequest.ShipmentItemRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(this::toShipmentItem)
                .collect(Collectors.toList());
    }

    default List<ShipmentItem> updateShipmentItems(List<UpdateShipmentRequest.ShipmentItemRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(this::toShipmentItem)
                .collect(Collectors.toList());
    }

    default List<ShipmentResponse.ShipmentStatusHistoryResponse> mapStatusToHistory(Shipment entity) {
        List<ShipmentResponse.ShipmentStatusHistoryResponse> history = new ArrayList<>();
        ShipmentResponse.ShipmentStatusHistoryResponse statusHistory = new ShipmentResponse.ShipmentStatusHistoryResponse();
        statusHistory.setStatus(mapStatusToString(entity.getStatus()));
        statusHistory.setTimestamp(LocalDateTime.now());
        history.add(statusHistory);
        return history;
    }

    default Double calculateTotalPrice(ShipmentItem item) {
        return item.getQuantity() * item.getUnitPrice();
    }

    @Named("mapStatusToString")
    default String mapStatusToString(ShipmentStatus status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }
}