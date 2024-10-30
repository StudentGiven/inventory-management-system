package com.eightbit.inventorymanagement.resources;

import com.eightbit.inventorymanagement.model.Order;
import com.eightbit.inventorymanagement.model.OrderStatus;
import com.eightbit.inventorymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // Retrieve a specific order by orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Retrieve all orders for a specific customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    // Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable String orderId, @RequestParam OrderStatus status) {
        boolean isUpdated = orderService.updateOrderStatus(orderId, status);
        return isUpdated ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Check if an order is expired
    @GetMapping("/{orderId}/expired")
    public ResponseEntity<Boolean> isOrderExpired(@PathVariable String orderId) {
        boolean isExpired = orderService.isOrderExpired(orderId);
        return ResponseEntity.ok(isExpired);
    }

    // Update items in an existing order
    @PutMapping("/{orderId}/items")
    public ResponseEntity<Void> updateOrderItems(@PathVariable String orderId, @RequestBody List<Map<String, Object>> items) {
        boolean isUpdated = orderService.updateOrderItems(orderId, items);
        return isUpdated ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // Delete an order by orderId
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        boolean isDeleted = orderService.deleteOrder(orderId);
        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
