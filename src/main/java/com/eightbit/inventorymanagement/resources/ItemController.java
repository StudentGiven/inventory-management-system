package com.eightbit.inventorymanagement.resources;

import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /*
    Manually wire dependency
     */
//    private ItemService itemService;
//
//    public void setItemService(ItemService itemService) {
//        this.itemService = itemService;
//    }

    @GetMapping("/")
    public String rootHandler() {
        return "Welcome to the Inventory Management System!";
    }

    // Retrieve all available items
    @GetMapping
    public ResponseEntity<List<Item>> getAvailableItems() {
        List<Item> items = itemService.getAvailableItems();
        return ResponseEntity.ok(items);
    }

    // Retrieve a specific item by itemId
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        Optional<Item> item = itemService.getItemById(itemId);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Add a new item to the inventory
    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        System.out.println("Received item: " + item);
        Item newItem = itemService.addItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    // Remove an item from the inventory
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String itemId) {
        boolean isRemoved = itemService.removeItem(itemId);
        return isRemoved ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Replenish stock for an item
    @PutMapping("/{itemId}/replenish")
    public ResponseEntity<Void> replenishStock(@PathVariable String itemId, @RequestParam int quantity) {
        boolean isReplenished = itemService.replenishStock(itemId, quantity);
        return isReplenished ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Set a threshold for an item
    @PutMapping("/{itemId}/threshold")
    public ResponseEntity<Void> setThreshold(@PathVariable String itemId, @RequestParam int threshold) {
        boolean isThresholdSet = itemService.setThreshold(itemId, threshold);
        return isThresholdSet ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
