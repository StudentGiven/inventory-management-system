package com.eightbit.inventorymanagement.dao;

import com.eightbit.inventorymanagement.model.Item;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {

    public final DynamoDbClient dynamoDbClient;

    public ItemRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public List<Item> getAvailableItem() {
        // getAvailableItems
        return new ArrayList<Item>();
    }
}
