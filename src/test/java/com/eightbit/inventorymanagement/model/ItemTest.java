package com.eightbit.inventorymanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ItemTest {
    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item();
    }

    @Test
    public void testSetAndGetItemId() {
        item.setItemId("item123");
        assertEquals("item123", item.getItemId());
    }

    @Test
    public void testSetAndGetItemName() {
        item.setItemName("Test Item");
        assertEquals("Test Item", item.getItemName());
    }

    @Test
    public void testSetAndGetDescription() {
        item.setDescription("This is a test item.");
        assertEquals("This is a test item.", item.getDescription());
    }

    @Test
    public void testSetAndGetPrice() {
        item.setPrice(19.99);
        assertEquals(19.99, item.getPrice());
    }

    @Test
    public void testSetAndGetStockLevel() {
        item.setStockLevel(10);
        assertEquals(10, item.getStockLevel());
    }

    @Test
    public void testSetAndGetCategory() {
        Category testCategory = Category.ELECTRONICS;
        item.setCategory(testCategory);
        assertEquals(testCategory, item.getCategory());
    }

    @Test
    public void testSetAndGetThreshold() {
        item.setThreshold(5);
        assertEquals(5, item.getThreshold());
    }

    @Test
    public void testSetAndGetIsAvailable() {
        item.setAvailable(true);
        assertTrue(item.isAvailable());

        item.setAvailable(false);
        assertFalse(item.isAvailable());
    }

    @Test
    public void testIsAvailableAutoUpdateBasedOnStockLevel() {
        item.setStockLevel(5);
        assertTrue(item.isAvailable());

        item.setStockLevel(0);
        assertFalse(item.isAvailable());

        item.setStockLevel(-1);
        assertFalse(item.isAvailable());
    }
}
