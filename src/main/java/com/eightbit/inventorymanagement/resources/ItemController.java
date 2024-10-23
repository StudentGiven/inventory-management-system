package com.eightbit.inventorymanagement.resources;

import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAvailableItems() {
        List<Item> items = itemService.getAllAvailableItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        Item item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item newItem = itemService.addItem(item);
        return ResponseEntity.status(201).body(newItem);
    }


}
