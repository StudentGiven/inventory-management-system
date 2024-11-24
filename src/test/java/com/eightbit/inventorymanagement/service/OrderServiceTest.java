package com.eightbit.inventorymanagement.service;

import com.eightbit.inventorymanagement.dao.ItemRepository;
import com.eightbit.inventorymanagement.dao.OrderRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testCreateOrderWithValidData() {
//        Order order = new Order();
//        order.setCustomerId("customer123");
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
//        order.setItems(items);
//        order.setOrderTime(Instant.parse("2023-11-05T10:00:00Z"));
//
//        Order result = orderService.createOrder(order);
//
//        assertEquals(order, result, "The created order should be returned");
//        verify(orderRepository, times(1)).createOrder(order);
//    }

    @Test
    public void testCreateOrderWithNullOrder() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(null);
        });

        assertEquals("Order must not be null.", exception.getMessage());
        verify(orderRepository, never()).createOrder(any(Order.class));
    }

    @Test
    public void testCreateOrderWithMissingCustomerId() {
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId("item123");
        item.setQuantity(10);

        OrderItem item2 = new OrderItem();
        item.setItemId("item456");
        item.setQuantity(5);

        items.add(item);
        items.add(item2);
        order.setItems(items);
        order.setOrderTime(Instant.parse("2023-11-05T10:00:00Z"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(order);
        });

        assertEquals("Customer ID must be provided for the order.", exception.getMessage());
        verify(orderRepository, never()).createOrder(any(Order.class));
    }

    @Test
    public void testGetOrderByIdWithValidId() {
        Order order = new Order();
        order.setOrderId("order123");

        when(orderRepository.getOrderById("order123")).thenReturn(order);

        Optional<Order> result = orderService.getOrderById("order123");

        assertTrue(result.isPresent(), "Order should be found");
        assertEquals(order, result.get(), "The retrieved order should match the expected order");
    }

    @Test
    public void testGetOrderByIdWithMissingId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrderById(null);
        });

        assertEquals("Order ID must be provided.", exception.getMessage());
        verify(orderRepository, never()).getOrderById(anyString());
    }

    @Test
    public void testUpdateOrderStatusWithValidData() {
        Order order = new Order();
        order.setOrderId("order123");

        when(orderRepository.getOrderById("order123")).thenReturn(order);

        boolean result = orderService.updateOrderStatus("order123", OrderStatus.EXECUTED);

        assertTrue(result, "Order status should be updated successfully");
        verify(orderRepository, times(1)).updateOrderStatus("order123", OrderStatus.EXECUTED);
    }

    @Test
    public void testUpdateOrderStatusWithMissingStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus("order123", null);
        });

        assertEquals("Order status must be provided for update.", exception.getMessage());
        verify(orderRepository, never()).updateOrderStatus(anyString(), any(OrderStatus.class));
    }

    @Test
    public void testUpdateOrderStatusWithNonExistentOrder() {
        when(orderRepository.getOrderById("nonExistentId")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus("nonExistentId", OrderStatus.EXECUTED);
        });

        assertEquals("Order with ID nonExistentId does not exist.", exception.getMessage());
        verify(orderRepository, never()).updateOrderStatus(anyString(), any(OrderStatus.class));
    }

    @Test
    public void testIsOrderExpiredWithValidId() {
        when(orderRepository.isOrderExpired("order123")).thenReturn(true);

        boolean result = orderService.isOrderExpired("order123");

        assertTrue(result, "The order should be expired");
    }

    @Test
    public void testIsOrderExpiredWithMissingId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.isOrderExpired(null);
        });

        assertEquals("Order ID must be provided.", exception.getMessage());
    }

    @Test
    public void testUpdateOrderItemsWithValidData() {
        Order order = new Order();
        order.setOrderId("order123");

        when(orderRepository.getOrderById("order123")).thenReturn(order);

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId("item123");
        item.setQuantity(10);

        OrderItem item2 = new OrderItem();
        item.setItemId("item456");
        item.setQuantity(5);

        items.add(item);
        items.add(item2);
        boolean result = orderService.updateOrderItems("order123", items);

        assertTrue(result, "Order items should be updated successfully");
        verify(orderRepository, times(1)).updateOrderItems("order123", items);
    }

    @Test
    public void testUpdateOrderItemsWithEmptyItems() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderItems("order123", null);
        });

        assertEquals("Order items must be provided for update.", exception.getMessage());
        verify(orderRepository, never()).updateOrderItems(anyString(), any());
    }

    @Test
    public void testDeleteOrderWithValidId() {
        Order order = new Order();
        order.setOrderId("order123");

        when(orderRepository.getOrderById("order123")).thenReturn(order);

        boolean result = orderService.deleteOrder("order123");

        assertTrue(result, "Order should be deleted successfully");
        verify(orderRepository, times(1)).deleteOrder("order123");
    }

    @Test
    public void testDeleteOrderWithNonExistentId() {
        when(orderRepository.getOrderById("nonExistentId")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrder("nonExistentId");
        });

        assertEquals("Order with ID nonExistentId does not exist.", exception.getMessage());
        verify(orderRepository, never()).deleteOrder(anyString());
    }

}
