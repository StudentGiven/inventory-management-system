package com.eightbit.inventorymanagement.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderItem;
import com.eightbit.inventorymanagement.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public OrderRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    // Create a new order
    public void createOrder(Order order) {
        dynamoDBMapper.save(order);
    }

    // Retrieve an order by orderId (unique identifier)
    public Order getOrderById(String orderId) {
//        return dynamoDBMapper.load(Order.class, orderId);

        // Create a query expression for the GSI
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":orderId", new AttributeValue().withS(orderId));

        DynamoDBQueryExpression<Order> queryExpression = new DynamoDBQueryExpression<Order>()
                .withIndexName("OrderIdIndex") // GSI name
                .withKeyConditionExpression("orderId = :orderId") // Query condition
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false); // GSIs do not support strongly consistent reads

        // Execute the query
        List<Order> orders = dynamoDBMapper.query(Order.class, queryExpression);

        // Return the first result or null if no matches
        return orders.get(0);

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
            if (status.equals(OrderStatus.EXECUTED)) {
                order.setExecutionTime(Instant.now().toString());
            }
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
    public void updateOrderItems(String orderId, List<OrderItem> items) {
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
