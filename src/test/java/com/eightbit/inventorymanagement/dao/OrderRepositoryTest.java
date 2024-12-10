package com.eightbit.inventorymanagement.dao;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.eightbit.inventorymanagement.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderRepositoryTest {


    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setOrderId("order123");

        orderRepository.createOrder(order);

        verify(dynamoDBMapper, times(1)).save(order);
    }

//    @Test
//    public void testGetOrderById() {
//        Order expectedOrder = new Order();
//        expectedOrder.setOrderId("order123");
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(expectedOrder);
//
//        Order result = orderRepository.getOrderById("order123");
//
//        assertEquals(expectedOrder, result, "getOrderById should return the expected order");
//    }

//    @Test
//    public void testGetOrdersByCustomerId() {
//        String customerId = "customer123";
//        Order order1 = new Order();
//        order1.setCustomerId(customerId);
//        Order order2 = new Order();
//        order2.setCustomerId(customerId);
//
//        List<Order> expectedOrders = List.of(order1, order2);
//
//        when(dynamoDBMapper.query(eq(Order.class), any(DynamoDBQueryExpression.class))).thenReturn(expectedOrders);
//
//        List<Order> result = orderRepository.getOrdersByCustomerId(customerId);
//
//        assertEquals(expectedOrders, result, "getOrdersByCustomerId should return the expected list of orders");
//    }

//    @Test
//    public void testUpdateOrderStatus() {
//        Order order = new Order();
//        order.setOrderId("order123");
//        order.setStatus(OrderStatus.HOLD);
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(order);
//
//        orderRepository.updateOrderStatus("order123", OrderStatus.EXECUTED);
//
//        assertEquals(OrderStatus.EXECUTED, order.getStatus(), "Order status should be updated to EXECUTED");
//        verify(dynamoDBMapper, times(1)).save(order);
//    }

//    @Test
//    public void testUpdateOrderStatus_OrderNotFound() {
//        when(dynamoDBMapper.load(Order.class, "nonExistentId")).thenReturn(null);
//
//        orderRepository.updateOrderStatus("nonExistentId", OrderStatus.EXECUTED);
//
//        verify(dynamoDBMapper, never()).save(any(Order.class));
//    }

//    @Test
//    public void testIsOrderExpired() {
//        Order order = new Order();
//        order.setOrderId("order123");
//        order.setHoldExpiryTime(Instant.now().minusSeconds(3600).toString());
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(order);
//
//        assertTrue(orderRepository.isOrderExpired("order123"), "Order should be expired");
//    }

//    @Test
//    public void testIsOrderExpired_NotExpired() {
//        Order order = new Order();
//        order.setOrderId("order123");
//        order.setHoldExpiryTime(Instant.now().plusSeconds(3600).toString());
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(order);
//
//        assertFalse(orderRepository.isOrderExpired("order123"), "Order should not be expired");
//    }

//    @Test
//    public void testIsOrderExpired_OrderNotFound() {
//        when(dynamoDBMapper.load(Order.class, "nonExistentId")).thenReturn(null);
//
//        assertFalse(orderRepository.isOrderExpired("nonExistentId"), "Non-existent order should not be expired");
//    }

//    @Test
//    public void testUpdateOrderItems() {
//        Order order = new Order();
//        order.setOrderId("order123");
//
////        List<Item> items = List.of(
////                Map.of("itemId", "item1", "quantity", 2),
////                Map.of("itemId", "item2", "quantity", 1)
////        );
//        List<OrderItem> items = new ArrayList<>();
//        OrderItem item = new OrderItem();
//        item.setItemId("item123");
//        item.setQuantity(10);
//
//        OrderItem item2 = new OrderItem();
//        item.setItemId("item456");
//        item.setQuantity(5);
//
//        items.add(item);
//        items.add(item2);
//
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(order);
//
//        orderRepository.updateOrderItems("order123", items);
//
//        assertEquals(items, order.getItems(), "Order items should be updated");
//        verify(dynamoDBMapper, times(1)).save(order);
//    }
//
//    @Test
//    public void testUpdateOrderItems_OrderNotFound() {
//        when(dynamoDBMapper.load(Order.class, "nonExistentId")).thenReturn(null);
//
//        List<OrderItem> items = new ArrayList<>();
//        OrderItem item = new OrderItem();
//        item.setItemId("item123");
//        item.setQuantity(10);
//
//        OrderItem item2 = new OrderItem();
//        item.setItemId("item456");
//        item.setQuantity(5);
//
//        items.add(item);
//        items.add(item2);
//
//        items.add(item);
//        items.add(item2);
//
//        orderRepository.updateOrderItems("nonExistentId", items);
//
//        verify(dynamoDBMapper, never()).save(any(Order.class));
//    }
//
//    @Test
//    public void testDeleteOrder() {
//        Order order = new Order();
//        order.setOrderId("order123");
//
//        when(dynamoDBMapper.load(Order.class, "order123")).thenReturn(order);
//
//        orderRepository.deleteOrder("order123");
//
//        verify(dynamoDBMapper, times(1)).delete(order);
//    }
//
//    @Test
//    public void testDeleteOrder_OrderNotFound() {
//        when(dynamoDBMapper.load(Order.class, "nonExistentId")).thenReturn(null);
//
//        orderRepository.deleteOrder("nonExistentId");
//
//        verify(dynamoDBMapper, never()).delete(any(Order.class));
//    }
}
