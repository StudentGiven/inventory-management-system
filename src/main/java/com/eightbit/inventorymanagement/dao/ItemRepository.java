package com.eightbit.inventorymanagement.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.eightbit.inventorymanagement.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    // Retrieve all available items
    public List<Item> getAvailableItems() {

        Map<String, AttributeValue> isAvailableFilter = new HashMap<>();
        isAvailableFilter.put(":isAvailable", new AttributeValue().withBOOL(true));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("isAvailable = :isAvailable")
                .withExpressionAttributeValues(isAvailableFilter);

        return dynamoDBMapper.scan(Item.class, scanExpression);
    }

    // Retrieve a specific item by itemId
    public Item getItemById(String itemId) {
        return dynamoDBMapper.load(Item.class, itemId);
    }

    // Add a new item to the inventory
    public void addItem(Item item) {
        dynamoDBMapper.save(item);
    }

    // Remove an item from the inventory
    public void removeItem(String itemId) {
        Item item = getItemById(itemId);
        if (item != null) {
            dynamoDBMapper.delete(item);
        }
    }

    // Replenish stock for an item
    public void replenishStock(String itemId, int quantity) {
        Item item = getItemById(itemId);
        if (item != null) {
            item.setStockLevel(item.getStockLevel() + quantity);
            dynamoDBMapper.save(item);
        }
    }

    // Set a low stock threshold for an item and update availability
    public void setThreshold(String itemId, int threshold) {
        Item item = getItemById(itemId);
        if (item != null) {
            item.setThreshold(threshold);
            dynamoDBMapper.save(item);
        }
    }
}
