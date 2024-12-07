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

    public void setOrderTime(Instant orderTime) {
        this.orderTime = orderTime.toString();
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

    public void setHoldStartTime(Instant holdStartTime) {
        this.holdStartTime = holdStartTime.toString();
    }

    @DynamoDBAttribute(attributeName = "holdExpiryTime")
    public String getHoldExpiryTime() {
        return holdExpiryTime;
    }

    public void setHoldExpiryTime(Instant holdExpiryTime) {
        this.holdExpiryTime = holdExpiryTime.toString();
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
