package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.ItemRepository;
import com.eightbit.inventorymanagement.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // Retrieve all available items
    public List<Item> getAvailableItems() {
        return itemRepository.getAvailableItems();
    }

    // Retrieve a specific item by itemId
    public Optional<Item> getItemById(String itemId) {
        Item item = itemRepository.getItemById(itemId);
        return Optional.ofNullable(item);
    }

    // Add a new item to the inventory
    public Item addItem(Item item) {
        itemRepository.addItem(item);
        return item; // Returning the added item for confirmation
    }

    // Remove an item from the inventory
    public boolean removeItem(String itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item != null) {
            itemRepository.removeItem(itemId);
            return true;
        }
        return false;
    }

    // Replenish stock for an item
    public boolean replenishStock(String itemId, int quantity) {
        Item item = itemRepository.getItemById(itemId);
        if (item != null && quantity > 0) {
            itemRepository.replenishStock(itemId, quantity);
            return true;
        }
        return false;
    }

    // Set a threshold for an item
    public boolean setThreshold(String itemId, int threshold) {
        Item item = itemRepository.getItemById(itemId);
        if (item != null) {
            itemRepository.setThreshold(itemId, threshold);
            return true;
        }
        return false;
    }

    // Check if an item is below the threshold and should notify admin
    public boolean isItemBelowThreshold(String itemId) {
        Item item = itemRepository.getItemById(itemId);
        return item != null && item.getStockLevel() < item.getThreshold();
    }
}
