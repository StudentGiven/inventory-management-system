package com.eightbit.inventorymanagement.resources;


import com.eightbit.inventorymanagement.model.Item;
import com.eightbit.inventorymanagement.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetAvailableItems() throws Exception {
        Item item1 = new Item();
        item1.setItemId("item1");
        item1.setItemName("Item 1");

        Item item2 = new Item();
        item2.setItemId("item2");
        item2.setItemName("Item 2");

        when(itemService.getAvailableItems()).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].itemId").value("item1"))
                .andExpect(jsonPath("$[1].itemId").value("item2"));
    }

    @Test
    public void testGetItemByIdFound() throws Exception {
        Item item = new Item();
        item.setItemId("item1");
        item.setItemName("Item 1");

        when(itemService.getItemById("item1")).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/item1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value("item1"))
                .andExpect(jsonPath("$.itemName").value("Item 1"));
    }

    @Test
    public void testGetItemByIdNotFound() throws Exception {
        when(itemService.getItemById("nonExistentId")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/nonExistentId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddItem() throws Exception {
        Item item = new Item();
        item.setItemId("item1");
        item.setItemName("New Item");

        when(itemService.addItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"item1\", \"itemName\":\"New Item\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value("item1"))
                .andExpect(jsonPath("$.itemName").value("New Item"));
    }

    @Test
    public void testRemoveItemFound() throws Exception {
        when(itemService.removeItem("item1")).thenReturn(true);

        mockMvc.perform(delete("/api/items/item1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveItemNotFound() throws Exception {
        when(itemService.removeItem("nonExistentId")).thenReturn(false);

        mockMvc.perform(delete("/api/items/nonExistentId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReplenishStockSuccess() throws Exception {
        when(itemService.replenishStock("item1", 10)).thenReturn(true);

        mockMvc.perform(put("/api/items/item1/replenish")
                        .param("quantity", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReplenishStockNotFound() throws Exception {
        when(itemService.replenishStock("nonExistentId", 10)).thenReturn(false);

        mockMvc.perform(put("/api/items/nonExistentId/replenish")
                        .param("quantity", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSetThresholdSuccess() throws Exception {
        when(itemService.setThreshold("item1", 5)).thenReturn(true);

        mockMvc.perform(put("/api/items/item1/threshold")
                        .param("threshold", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSetThresholdNotFound() throws Exception {
        when(itemService.setThreshold("nonExistentId", 5)).thenReturn(false);

        mockMvc.perform(put("/api/items/nonExistentId/threshold")
                        .param("threshold", "5"))
                .andExpect(status().isNotFound());
    }

}
