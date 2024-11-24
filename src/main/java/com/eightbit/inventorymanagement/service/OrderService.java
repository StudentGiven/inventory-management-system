package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.ItemRepository;
import com.eightbit.inventorymanagement.dao.OrderRepository;
import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderItem;
import com.eightbit.inventorymanagement.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    // Create a new order
    public Order createOrder(Order order) {
        validateOrder(order);

        Instant currentTime = Instant.now();
        Instant orderExpireTime = currentTime.plus(Duration.ofMinutes(17)); // Give customer 2 min grace period
        System.out.println("Current Time: " + currentTime);
        System.out.println("15 Minutes Later: " + orderExpireTime);



        String currentTimeString = currentTime.toString();
        StringBuilder newOrderId = new StringBuilder(currentTimeString);

        // Check item is available
        List<OrderItem> orderItems = order.getItems();
        for (OrderItem orderItem : orderItems) {
            String itemId = orderItem.getItemId();
            int quantity = orderItem.getQuantity();

            // Validate item existence and availability
            Item item = itemRepository.getItemById(itemId);
            if (item == null) {
                throw new IllegalArgumentException("Item " + itemId + " does not exist.");
            }
            String itemName = item.getItemName();
            newOrderId.append(itemName);

            if (quantity > item.getStockLevel()) {
                throw new IllegalArgumentException("Insufficient stock for item " + itemName + ".");
            }

            // Update the stock level
            itemRepository.replenishStock(itemId, quantity - item.getStockLevel());
        }

        order.setCustomerId("testUser"); // Use test user for now
        order.setOrderId(newOrderId.toString());
        order.setOrderTime(currentTime);
        order.setHoldStartTime(currentTime);
        order.setHoldExpiryTime(orderExpireTime);
        order.setStatus(OrderStatus.HOLD);
        orderRepository.createOrder(order);
        return order; // Returning the created order for confirmation
    }

    // Retrieve a specific order by customerId and orderTime
    public Optional<Order> getOrderById(String orderId) {
        validateOrderId(orderId);
        Order order = orderRepository.getOrderById(orderId);
        return Optional.ofNullable(order);
    }

    // Retrieve all orders for a specific customer
    public List<Order> getOrdersByCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID must be provided to retrieve orders.");
        }
        return orderRepository.getOrdersByCustomerId(customerId);
    }

    // Update order status (e.g., HOLD, EXECUTED, EXPIRED)
    public boolean updateOrderStatus(String orderId, OrderStatus status) {
        validateOrderId(orderId);
        if (status == null) {
            throw new IllegalArgumentException("Order status must be provided for update.");
        }

        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }

        orderRepository.updateOrderStatus(orderId, status);
        return true;
    }

    // Check if an order is expired
    public boolean isOrderExpired(String orderId) {
        validateOrderId(orderId);
        return orderRepository.isOrderExpired(orderId);
    }

    // Add or update items in an existing order
    public boolean updateOrderItems(String orderId, List<OrderItem> items) {
        validateOrderId(orderId);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order items must be provided for update.");
        }

        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }

        orderRepository.updateOrderItems(orderId, items);
        return true;
    }

    // Delete an order by customerId and orderTime
    public boolean deleteOrder(String orderId) {
        validateOrderId(orderId);

        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }

        orderRepository.deleteOrder(orderId);
        return true;
    }

    // Helper method to validate order object before creation
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null.");
        }
        if (order.getCustomerId() == null || order.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("Customer ID must be provided for the order.");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items must be provided.");
        }
    }

    // Helper method to validate orderId before performing operations
    private void validateOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID must be provided.");
        }
    }
}
