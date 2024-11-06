package com.eightbit.inventorymanagement.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.eightbit.inventorymanagement.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testGetAvailableItems2() {
//        Item item1 = new Item();
//        item1.setItemId("item1");
//        item1.setAvailable(true);
//
//        Item item2 = new Item();
//        item2.setItemId("item2");
//        item2.setAvailable(true);
//
//        // Add items to paginated scan list mock
//        PaginatedScanList<Item> paginatedScanList = null;
//        paginatedScanList.add(item1);
//        paginatedScanList.add(item2);
//
//        Map<String, AttributeValue> isAvailableFilter = new HashMap<>();
//        isAvailableFilter.put(":isAvailable", new AttributeValue().withBOOL(true));
//
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//                .withFilterExpression("isAvailable = :isAvailable")
//                .withExpressionAttributeValues(isAvailableFilter);
//
//        when(itemRepository.getAvailableItems()).thenReturn(paginatedScanList);
//
//        PaginatedScanList<Item> availableItems = itemRepository.getAvailableItems();
//        assertEquals(2, availableItems.size(), "The number of available items should be 2");
//        assertEquals(item1, availableItems.get(0), "The first item should match item1");
//        assertEquals(item2, availableItems.get(1), "The second item should match item2");
//    }

    @Test
    public void testGetItemById() {
        Item expectedItem = new Item();
        expectedItem.setItemId("item123");
        when(dynamoDBMapper.load(Item.class, "item123")).thenReturn(expectedItem);

        Item item = itemRepository.getItemById("item123");
        assertEquals(expectedItem, item, "Get item by ID is not working as expected");
    }

    @Test
    public void testAddItem() {
        Item item = new Item();
        item.setItemId("item123");

        itemRepository.addItem(item);
        verify(dynamoDBMapper, times(1)).save(item);
    }

    @Test
    public void testRemoveItem() {
        Item item = new Item();
        item.setItemId("item123");

        when(dynamoDBMapper.load(Item.class, "item123")).thenReturn(item);

        itemRepository.removeItem("item123");
        verify(dynamoDBMapper, times(1)).delete(item);
    }

    @Test
    public void testRemoveItem_ItemNotFound() {
        when(dynamoDBMapper.load(Item.class, "nonExistentId")).thenReturn(null);

        itemRepository.removeItem("nonExistentId");
        verify(dynamoDBMapper, never()).delete(any(Item.class));
    }

    @Test
    public void testReplenishStock() {
        Item item = new Item();
        item.setItemId("item123");
        item.setStockLevel(5);

        when(dynamoDBMapper.load(Item.class, "item123")).thenReturn(item);

        itemRepository.replenishStock("item123", 10);

        assertEquals(15, item.getStockLevel(), "Replenish stock is not updating stock level correctly");
        verify(dynamoDBMapper, times(1)).save(item);
    }

    @Test
    public void testReplenishStock_ItemNotFound() {
        when(dynamoDBMapper.load(Item.class, "nonExistentId")).thenReturn(null);

        itemRepository.replenishStock("nonExistentId", 10);
        verify(dynamoDBMapper, never()).save(any(Item.class));
    }

    @Test
    public void testSetThreshold() {
        Item item = new Item();
        item.setItemId("item123");

        when(dynamoDBMapper.load(Item.class, "item123")).thenReturn(item);

        itemRepository.setThreshold("item123", 5);

        assertEquals(5, item.getThreshold(), "Set threshold is not updating threshold value correctly");
        verify(dynamoDBMapper, times(1)).save(item);
    }

    @Test
    public void testSetThreshold_ItemNotFound() {
        when(dynamoDBMapper.load(Item.class, "nonExistentId")).thenReturn(null);

        itemRepository.setThreshold("nonExistentId", 5);
        verify(dynamoDBMapper, never()).save(any(Item.class));
    }

}
