package com.eightbit.inventorymanagement;

import com.eightbit.inventorymanagement.model.Category;
import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.service.DynamoDBService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
//        SpringApplication.run(InventoryManagementApplication.class, args);

        System.out.println("This is a test");
        System.out.println("AWS Region: " + System.getenv("AWS_REGION"));
        System.out.println("AWS Access Key ID: " + System.getenv("AWS_ACCESS_KEY_ID"));
        System.out.println("AWS SECRET ACCESS KEY: " + System.getenv("AWS_SECRET_ACCESS_KEY"));

        DynamoDBService dynamoDBService = new DynamoDBService();
        dynamoDBService.createTableIfNotExists();
        System.out.println("This is a test2");
        System.out.println("AWS Region: " + System.getenv("AWS_REGION"));
        System.out.println("AWS Access Key ID: " + System.getenv("AWS_ACCESS_KEY_ID"));

        // Create a new item
        Arraylist<Item> items = readFromJson("items.json")

        Item item = new Item();
        item.setItemId("100002"); // Unique ID
        item.setItemName("clothes");
        item.setDescription("A sample item description.");
        item.setPrice(99.99);
        item.setStockLevel(100);
        item.setCategory(Category.APPAREL);

        // Save the item to the DynamoDB table
        dynamoDBService.saveItem(items);
    }

}
