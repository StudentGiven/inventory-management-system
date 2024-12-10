package com.eightbit.inventorymanagement;

import com.eightbit.inventorymanagement.model.*;
import com.eightbit.inventorymanagement.service.DynamoDBService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);

        System.out.println("This is a test");
        System.out.println("AWS Region: " + System.getenv("AWS_REGION"));
        System.out.println("AWS Access Key ID: " + System.getenv("AWS_ACCESS_KEY_ID"));
        System.out.println("AWS SECRET ACCESS KEY: " + System.getenv("AWS_SECRET_ACCESS_KEY"));

        DynamoDBService dynamoDBService = new DynamoDBService();
        dynamoDBService.createTableIfNotExists();
        dynamoDBService.createOrderTableIfNotExists();
        System.out.println("This is a test2");
        System.out.println("AWS Region: " + System.getenv("AWS_REGION"));
        System.out.println("AWS Access Key ID: " + System.getenv("AWS_ACCESS_KEY_ID"));

        Item item = new Item();
        item.setItemId("100004"); // Unique ID
        item.setItemName("clothes2");
        item.setDescription("A sample item description.");
        item.setPrice(99.99);
        item.setStockLevel(10);
        item.setCategory(Category.APPAREL);

        // Save the item to the DynamoDB table
        dynamoDBService.saveItem(item);

        System.out.println("This is a test3");

        Instant now = Instant.now();
        Instant expireTime = now.plus(Duration.ofMinutes(17));
        Order order = new Order();
        order.setCustomerId("testUser1");
        order.setOrderTime(now.toString());
        order.setOrderId("123648596");
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId("100004");
        orderItem.setQuantity(1);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        order.setItems(orderItems);
        order.setOrderId(now.toString() + orderItems.get(0).getItemId());
        order.setHoldStartTime(now.toString());
        order.setHoldExpiryTime(expireTime.toString());
        order.setStatus(OrderStatus.HOLD);

//        System.out.println("This is a test4");

        // Save the item to the DynamoDB table
//        dynamoDBService.saveOrder(order);



    }

}
