package com.eightbit.inventorymanagement.resources;

import com.eightbit.inventorymanagement.model.OrderStatus;
import com.eightbit.inventorymanagement.service.OrderService;
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

public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

//    @Test
//    public void testCreateOrder() throws Exception {
//        Order order = new Order();
//        order.setOrderId("order1");
//        order.setCustomerId("customer1");
//        order.setStatus(OrderStatus.HOLD);
//        order.setItems(List.of(Map.of("itemId", "item1", "quantity", 2))); // Mock order items if required
//
//        when(orderService.createOrder(any(Order.class))).thenReturn(order);
//
//        // JSON payload that matches the Order structure, including items and status
//        String orderJson = """
//            {
//                "orderId": "order1",
//                "customerId": "customer1",
//                "status": "HOLD",
//                "items": [
//                    {
//                        "itemId": "item1",
//                        "quantity": 2
//                    }
//                ]
//            }
//        """;
//
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(orderJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.orderId").value("order1"))
//                .andExpect(jsonPath("$.customerId").value("customer1"))
//                .andExpect(jsonPath("$.status").value("HOLD"))
//                .andExpect(jsonPath("$.items[0].itemId").value("item1"))
//                .andExpect(jsonPath("$.items[0].quantity").value(2));
//    }

//    @Test
//    public void testGetOrderByIdFound() throws Exception {
//        Order order = new Order();
//        order.setOrderId("order1");
//        order.setCustomerId("customer1");
//
//        when(orderService.getOrderById("order1")).thenReturn(Optional.of(order));
//
//        mockMvc.perform(get("/api/orders/order1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.orderId").value("order1"))
//                .andExpect(jsonPath("$.customerId").value("customer1"));
//    }

    @Test
    public void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById("nonExistentOrder")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/nonExistentOrder"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void testGetOrdersByCustomerId() throws Exception {
//        Order order1 = new Order();
//        order1.setOrderId("order1");
//        order1.setCustomerId("customer1");
//
//        Order order2 = new Order();
//        order2.setOrderId("order2");
//        order2.setCustomerId("customer1");
//
//        when(orderService.getOrdersByCustomerId("customer1")).thenReturn(List.of(order1, order2));
//
//        mockMvc.perform(get("/api/orders/customer/customer1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].orderId").value("order1"))
//                .andExpect(jsonPath("$[1].orderId").value("order2"));
//    }

    @Test
    public void testUpdateOrderStatusSuccess() throws Exception {
        when(orderService.updateOrderStatus("order1", OrderStatus.EXECUTED)).thenReturn(true);

        mockMvc.perform(put("/api/orders/order1/status")
                        .param("status", "EXECUTED"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateOrderStatusNotFound() throws Exception {
        when(orderService.updateOrderStatus("nonExistentOrder", OrderStatus.EXECUTED)).thenReturn(false);

        mockMvc.perform(put("/api/orders/nonExistentOrder/status")
                        .param("status", "EXECUTED"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testIsOrderExpired() throws Exception {
        when(orderService.isOrderExpired("order1")).thenReturn(true);

        mockMvc.perform(get("/api/orders/order1/expired"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testUpdateOrderItemsSuccess() throws Exception {
        when(orderService.updateOrderItems(eq("order1"), any(List.class))).thenReturn(true);

        String itemsJson = """
            [
                {
                    "itemId": "item1",
                    "quantity": 2
                }
            ]
        """;

        mockMvc.perform(put("/api/orders/order1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateOrderItemsNotFound() throws Exception {
        when(orderService.updateOrderItems(eq("nonExistentOrder"), any(List.class))).thenReturn(false);

        String itemsJson = """
            [
                {
                    "itemId": "item1",
                    "quantity": 2
                }
            ]
        """;

        mockMvc.perform(put("/api/orders/nonExistentOrder/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrderSuccess() throws Exception {
        when(orderService.deleteOrder("order1")).thenReturn(true);

        mockMvc.perform(delete("/api/orders/order1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOrderNotFound() throws Exception {
        when(orderService.deleteOrder("nonExistentOrder")).thenReturn(false);

        mockMvc.perform(delete("/api/orders/nonExistentOrder"))
                .andExpect(status().isNotFound());
    }

}
