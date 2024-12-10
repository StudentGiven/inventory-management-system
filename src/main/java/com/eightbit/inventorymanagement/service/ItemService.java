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
        validateNewItem(item);
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
        if (itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("Item ID must be provided for stock replenishment.");
        }
//        if (quantity <= 0) {
//            throw new IllegalArgumentException("Replenishment quantity must be greater than zero.");
//        }

        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item with ID " + itemId + " does not exist.");
        }

        itemRepository.replenishStock(itemId, quantity);
        return true;
    }

    // Set a threshold for an item
    public boolean setThreshold(String itemId, int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than zero.");
        }

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

    // Validate new item details before adding it to the inventory
    private void validateNewItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null.");
        }
        if (item.getItemId() == null || item.getItemId().isEmpty()) {
            throw new IllegalArgumentException("Item ID must be provided.");
        }
        if (item.getItemName() == null || item.getItemName().isEmpty()) {
            throw new IllegalArgumentException("Item name must be provided.");
        }
        if (item.getPrice() <= 0) {
            throw new IllegalArgumentException("Item price must be greater than zero.");
        }
        if (item.getStockLevel() < 0) {
            throw new IllegalArgumentException("Item stock level must not be negative.");
        }
        if (item.getThreshold() < 0) {
            throw new IllegalArgumentException("Item threshold must not be negative.");
        }
    }
}
