package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.OrderRepository;
import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Create a new order
    public Order createOrder(Order order) {
        orderRepository.createOrder(order);
        return order; // Returning the created order for confirmation
    }

    // Retrieve a specific order by customerId and orderTime
    public Optional<Order> getOrderById(String orderId) {
        Order order = orderRepository.getOrderById(orderId);
        return Optional.ofNullable(order);
    }

    // Retrieve all orders for a specific customer
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.getOrdersByCustomerId(customerId);
    }

    // Update order status (e.g., HOLD, EXECUTED, EXPIRED)
    public boolean updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.getOrderById(orderId);
        if (order != null) {
            orderRepository.updateOrderStatus(orderId, status);
            return true;
        }
        return false;
    }

    // Check if an order is expired
    public boolean isOrderExpired(String orderId) {
        return orderRepository.isOrderExpired(orderId);
    }

    // Add or update items in an existing order
    public boolean updateOrderItems(String orderId, List<Map<String, Object>> items) {
        Order order = orderRepository.getOrderById(orderId);
        if (order != null) {
            orderRepository.updateOrderItems(orderId, items);
            return true;
        }
        return false;
    }

    // Delete an order by customerId and orderTime
    public boolean deleteOrder(String orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (order != null) {
            orderRepository.deleteOrder(orderId);
            return true;
        }
        return false;
    }
}
