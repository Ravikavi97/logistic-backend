package com.logistics.controller;

import com.logistics.dto.ApiResponse;
import com.logistics.dto.OrderDTO;
import com.logistics.dto.PageResponse;
import com.logistics.entity.Order.OrderStatus;
import com.logistics.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getAllOrders(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            HttpServletRequest request) {
        log.debug("REST request to get all Orders with pagination: {}", pageable);
        Page<OrderDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.successPage(orders)
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(
            @PathVariable String id,
            HttpServletRequest request) {
        log.debug("REST request to get Order : {}", id);
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            HttpServletRequest request) {
        log.debug("REST request to create Order : {}", orderDTO);
        OrderDTO created = orderService.createOrder(orderDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(created, "Order created successfully")
                .withPath(request.getRequestURI())
                .withRequestId(UUID.randomUUID().toString()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderDTO orderDTO,
            HttpServletRequest request) {
        log.debug("REST request to update Order : {}", id);
        OrderDTO updated = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Order updated successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status,
            HttpServletRequest request) {
        log.debug("REST request to update Order status : {}, {}", id, status);
        OrderDTO updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Order status updated successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @PathVariable String id,
            HttpServletRequest request) {
        log.debug("REST request to delete Order : {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Order deleted successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getOrdersByCustomer(
            @PathVariable String customerId,
            @PageableDefault(size = 20) Pageable pageable,
            HttpServletRequest request) {
        log.debug("REST request to get Orders by customer: {} with pagination: {}", customerId, pageable);
        Page<OrderDTO> orders = orderService.getOrdersByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.successPage(orders)
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @PageableDefault(size = 20) Pageable pageable,
            HttpServletRequest request) {
        log.debug("REST request to get Orders by status: {} with pagination: {}", status, pageable);
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.successPage(orders)
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> searchOrders(
            @RequestParam String query,
            @PageableDefault(size = 20) Pageable pageable,
            HttpServletRequest request) {
        log.debug("REST request to search Orders: {} with pagination: {}", query, pageable);
        Page<OrderDTO> orders = orderService.searchOrders(query, pageable);
        return ResponseEntity.ok(ApiResponse.successPage(orders)
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/recent/{status}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getRecentOrdersByStatus(
            @PathVariable OrderStatus status,
            HttpServletRequest request) {
        log.debug("REST request to get recent Orders by status: {}", status);
        List<OrderDTO> orders = orderService.getRecentOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.successList(orders, "Recent orders retrieved successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }
} 