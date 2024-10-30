package com.eightbit.inventorymanagement.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    // Create a new order
    public void createOrder(Order order) {
        dynamoDBMapper.save(order);
    }

    // Retrieve an order by orderId (unique identifier)
    public Order getOrderById(String orderId) {
        return dynamoDBMapper.load(Order.class, orderId);
    }

    // Retrieve all orders for a specific customer
    public List<Order> getOrdersByCustomerId(String customerId) {
        Order orderKey = new Order();
        orderKey.setCustomerId(customerId);

        DynamoDBQueryExpression<Order> queryExpression = new DynamoDBQueryExpression<Order>()
                .withHashKeyValues(orderKey);

        return dynamoDBMapper.query(Order.class, queryExpression);
    }

    // Update order status (e.g., HOLD, EXECUTED, EXPIRED)
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            dynamoDBMapper.save(order);
        }
    }

    // Check if an order is expired based on holdExpiryTime
    public boolean isOrderExpired(String orderId) {
        Order order = getOrderById(orderId);
        if (order != null && order.getHoldExpiryTime() != null) {
            Instant expiryTime = Instant.parse(order.getHoldExpiryTime());
            return Instant.now().isAfter(expiryTime);
        }
        return false;
    }

    // Add or update items in an existing order
    public void updateOrderItems(String orderId, List<Map<String, Object>> items) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setItems(items);
            dynamoDBMapper.save(order);
        }
    }

    // Delete an order by customerId and orderTime
    public void deleteOrder(String orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            dynamoDBMapper.delete(order);
        }
    }
}
