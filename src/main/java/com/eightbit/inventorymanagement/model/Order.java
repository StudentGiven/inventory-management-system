package com.eightbit.inventorymanagement.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "Orders")
public class Order {

    private String customerId;
    private String orderTime;          // ISO 8601 String for DynamoDB
    private String orderId;
    private List<OrderItem> items;
    private String holdStartTime;
    private String holdExpiryTime;
    private String executionTime;
    private OrderStatus status;

    @DynamoDBHashKey(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBRangeKey(attributeName = "orderTime")
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

//    public Instant getOrderTimeAsInstant() {
//        return Instant.parse(this.orderTime);
//    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "OrderIdIndex", attributeName = "orderId")
//    @DynamoDBAttribute(attributeName = "orderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "items")
    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @DynamoDBAttribute(attributeName = "holdStartTime")
    public String getHoldStartTime() {
        return holdStartTime;
    }

    public void setHoldStartTime(String holdStartTime) {
        this.holdStartTime = holdStartTime;
    }

    @DynamoDBAttribute(attributeName = "holdExpiryTime")
    public String getHoldExpiryTime() {
        return holdExpiryTime;
    }

    public void setHoldExpiryTime(String holdExpiryTime) {
        this.holdExpiryTime = holdExpiryTime;
    }

    @DynamoDBAttribute(attributeName = "executionTime")
    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "status")
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
