package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.ItemRepository;
import com.eightbit.inventorymanagement.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItemWithValidData() {
        Item item = new Item();
        item.setItemId("item123");
        item.setItemName("Test Item");
        item.setPrice(100.0);
        item.setStockLevel(10);
        item.setThreshold(5);

        Item result = itemService.addItem(item);

        assertEquals(item, result, "The added item should be returned");
        verify(itemRepository, times(1)).addItem(item);
    }

    @Test
    public void testAddItemWithNullItem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addItem(null);
        });

        assertEquals("Item must not be null.", exception.getMessage());
        verify(itemRepository, never()).addItem(any(Item.class));
    }

    @Test
    public void testAddItemWithEmptyItemId() {
        Item item = new Item();
        item.setItemName("Test Item");
        item.setPrice(100.0);
        item.setStockLevel(10);
        item.setThreshold(5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addItem(item);
        });

        assertEquals("Item ID must be provided.", exception.getMessage());
        verify(itemRepository, never()).addItem(any(Item.class));
    }

    @Test
    public void testAddItemWithNegativePrice() {
        Item item = new Item();
        item.setItemId("item123");
        item.setItemName("Test Item");
        item.setPrice(-10.0);
        item.setStockLevel(10);
        item.setThreshold(5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addItem(item);
        });

        assertEquals("Item price must be greater than zero.", exception.getMessage());
        verify(itemRepository, never()).addItem(any(Item.class));
    }

    @Test
    public void testAddItemWithNegativeStockLevel() {
        Item item = new Item();
        item.setItemId("item123");
        item.setItemName("Test Item");
        item.setPrice(100.0);
        item.setStockLevel(-1);
        item.setThreshold(5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addItem(item);
        });

        assertEquals("Item stock level must not be negative.", exception.getMessage());
        verify(itemRepository, never()).addItem(any(Item.class));
    }

    @Test
    public void testAddItemWithNegativeThreshold() {
        Item item = new Item();
        item.setItemId("item123");
        item.setItemName("Test Item");
        item.setPrice(100.0);
        item.setStockLevel(10);
        item.setThreshold(-1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addItem(item);
        });

        assertEquals("Item threshold must not be negative.", exception.getMessage());
        verify(itemRepository, never()).addItem(any(Item.class));
    }

    @Test
    public void testReplenishStockWithValidData() {
        Item item = new Item();
        item.setItemId("item123");
        item.setStockLevel(10);

        when(itemRepository.getItemById("item123")).thenReturn(item);

        boolean result = itemService.replenishStock("item123", 5);

        assertTrue(result, "Replenish stock should return true for valid data");
        verify(itemRepository, times(1)).replenishStock("item123", 5);
    }

    @Test
    public void testReplenishStockWithInvalidItemId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.replenishStock(null, 5);
        });

        assertEquals("Item ID must be provided for stock replenishment.", exception.getMessage());
        verify(itemRepository, never()).replenishStock(anyString(), anyInt());
    }

    @Test
    public void testReplenishStockWithNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.replenishStock("item123", -5);
        });

        assertEquals("Replenishment quantity must be greater than zero.", exception.getMessage());
        verify(itemRepository, never()).replenishStock(anyString(), anyInt());
    }

    @Test
    public void testReplenishStockWithNonExistentItem() {
        when(itemRepository.getItemById("nonExistentId")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.replenishStock("nonExistentId", 5);
        });

        assertEquals("Item with ID nonExistentId does not exist.", exception.getMessage());
        verify(itemRepository, never()).replenishStock(anyString(), anyInt());
    }

}
