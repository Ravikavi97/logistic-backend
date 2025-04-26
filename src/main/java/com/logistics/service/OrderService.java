package com.logistics.service;

import com.logistics.dto.OrderDTO;
import com.logistics.entity.Order;
import com.logistics.entity.Order.OrderStatus;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.mapper.OrderMapper;
import com.logistics.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "#id")
    public OrderDTO getOrderById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    orderMapper.updateEntity(existingOrder, orderDTO);
                    Order updatedOrder = orderRepository.save(existingOrder);
                    return orderMapper.toDTO(updatedOrder);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public OrderDTO updateOrderStatus(String id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = orderRepository.save(order);
                    return orderMapper.toDTO(updatedOrder);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'customer:' + #customerId + ':page:' + #pageable.pageNumber")
    public Page<OrderDTO> getOrdersByCustomer(String customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable)
                .map(orderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'status:' + #status + ':page:' + #pageable.pageNumber")
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable)
                .map(orderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'search:' + #query + ':page:' + #pageable.pageNumber")
    public Page<OrderDTO> searchOrders(String query, Pageable pageable) {
        return orderRepository.search(query, pageable)
                .map(orderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'status:' + #status + ':recent'")
    public List<OrderDTO> getRecentOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }
} 