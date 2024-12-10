package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.ItemRepository;
import com.eightbit.inventorymanagement.dao.OrderRepository;
import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderItem;
import com.eightbit.inventorymanagement.model.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    // Create a new order
    public Order createOrder(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            validateOrder(orderItem);
        }

        Instant currentTime = Instant.now();
        Instant orderExpireTime = currentTime.plus(Duration.ofMinutes(17)); // Give customer 2 min grace period
        System.out.println("Current Time: " + currentTime);
        System.out.println("17 Minutes Later: " + orderExpireTime);



        String currentTimeString = currentTime.toString();
        StringBuilder newOrderId = new StringBuilder(currentTimeString);

        // Check item is available
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
            itemRepository.replenishStock(itemId, -quantity);
        }

        Order newOrder = new Order();
        newOrder.setCustomerId("testUser4"); // Use test user for now
        newOrder.setOrderId(newOrderId.toString());
        newOrder.setOrderTime(currentTime.toString());
        newOrder.setHoldStartTime(currentTime.toString());
        newOrder.setHoldExpiryTime(orderExpireTime.toString());
        newOrder.setStatus(OrderStatus.HOLD);
        newOrder.setItems(orderItems);
        orderRepository.createOrder(newOrder);
        return newOrder; // Returning the created order for confirmation
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

        // Retrieve the existing order
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }

        List<OrderItem> oldItems = order.getItems();

//        log.info();
//        System.out.println(oldItems);
        System.out.println("Old Items: " + oldItems);
        System.out.println("Old Items size: " + oldItems.size());
        System.out.println("Old Items details: " + oldItems.get(0).getQuantity());


        // Create a map for quick lookup of old items by itemId
        Map<String, OrderItem> oldItemsMap = oldItems.stream()
                .collect(Collectors.toMap(OrderItem::getItemId, item -> item));

        // Process updated items
        for (OrderItem updatedItem : items) {
            String itemId = updatedItem.getItemId();
            int newQuantity = updatedItem.getQuantity();

            // Check if the updated item is in the old order
            if (oldItemsMap.containsKey(itemId)) {
                // Case 1: Updated item exists in the old order
                OrderItem oldItem = oldItemsMap.get(itemId);
                int oldQuantity = oldItem.getQuantity();
                int stockLevel = itemRepository.getItemById(itemId).getStockLevel();

                if (newQuantity > stockLevel + oldQuantity) {
                    throw new IllegalArgumentException("Insufficient stock for item " + itemId + ".");
                }

                // Update stock level
                itemRepository.replenishStock(itemId, oldQuantity - newQuantity);
            } else {
                // Case 2: Updated item is new to the order
                Item item = itemRepository.getItemById(itemId);
                if (item == null) {
                    throw new IllegalArgumentException("Item " + itemId + " does not exist.");
                }

                if (newQuantity > item.getStockLevel()) {
                    throw new IllegalArgumentException("Insufficient stock for item " + itemId + ".");
                }

                // Deduct stock for new item
                itemRepository.replenishStock(itemId, -newQuantity);
            }
        }

        // Case 3: Reset stock levels for items in the old order but not in the updated order
        Set<String> updatedItemIds = items.stream()
                .map(OrderItem::getItemId)
                .collect(Collectors.toSet());

        for (OrderItem oldItem : oldItems) {
            if (!updatedItemIds.contains(oldItem.getItemId())) {
                // Restore stock level for removed items
                itemRepository.replenishStock(oldItem.getItemId(), oldItem.getQuantity());
            }
        }

        // Update the order repository with the new items
        orderRepository.updateOrderItems(orderId, items);

        return true;
    }

    // Delete an order by orderId
    public boolean deleteOrder(String orderId) {
        validateOrderId(orderId);

        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " does not exist.");
        }

        // Check if the order status is "HOLD"
        if (!OrderStatus.HOLD.equals(order.getStatus())) {
            throw new IllegalStateException("Only orders with 'HOLD' status can be deleted.");
        }
        // restore item
        List<OrderItem> orderItems = order.getItems();
        for (OrderItem orderItem : orderItems) {
            String itemId = orderItem.getItemId();
            int quantity = orderItem.getQuantity();

            // Update the stock level
            itemRepository.replenishStock(itemId, quantity);
        }

        orderRepository.deleteOrder(orderId);
        return true;
    }

    // Helper method to validate order object before creation
    private void validateOrder(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("Order must not be null.");
        }
        if (orderItem.getItemId() == null || orderItem.getItemId().isEmpty()) {
            throw new IllegalArgumentException("Item ID must be provided for the order.");
        }
        if (orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    // Helper method to validate orderId before performing operations
    private void validateOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID must be provided.");
        }
    }

    public static long getOrderCount(List<OrderItem> orderItems, String targetItem) {
        return orderItems.stream()
                .filter(orderItem -> targetItem.equals(orderItem.getItemId()))
                .count();
    }
}
