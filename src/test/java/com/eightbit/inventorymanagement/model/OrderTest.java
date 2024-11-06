package com.eightbit.inventorymanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class OrderTest {
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    @Test
    public void testCustomerId() {
        order.setCustomerId("customer123");
        assertEquals("customer123", order.getCustomerId());
    }

    @Test
    public void testOrderTime() {
        Instant now = Instant.now();
        order.setOrderTime(now);
        assertEquals(now.toString(), order.getOrderTime());
        assertEquals(now, order.getOrderTimeAsInstant());
    }

    @Test
    public void testOrderId() {
        order.setOrderId("order123");
        assertEquals("order123", order.getOrderId());
    }

    @Test
    public void testItems() {
        List<Map<String, Object>> items = List.of(
                Map.of("itemId", "item1", "quantity", 2),
                Map.of("itemId", "item2", "quantity", 1)
        );
        order.setItems(items);
        assertEquals(items, order.getItems());
    }

    @Test
    public void testHoldStartTime() {
        Instant now = Instant.now();
        order.setHoldStartTime(now);
        assertEquals(now.toString(), order.getHoldStartTime());
    }

    @Test
    public void testHoldExpiryTime() {
        Instant now = Instant.now();
        order.setHoldExpiryTime(now);
        assertEquals(now.toString(), order.getHoldExpiryTime());
    }

    @Test
    public void testStatus() {
        order.setStatus(OrderStatus.HOLD);
        assertEquals(OrderStatus.HOLD, order.getStatus());
    }

    @Test
    public void testOrderTimeAsInstant() {
        Instant now = Instant.now();
        order.setOrderTime(now);
        Instant parsedInstant = order.getOrderTimeAsInstant();
        assertEquals(now, parsedInstant);
    }
}
