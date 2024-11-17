package com.polyglot.controller;

import com.polyglot.dto.OrderReqRes;
import com.polyglot.entity.*;
import com.polyglot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create an order from a cart
    @PostMapping("/checkout")
    public ResponseEntity<OrderReqRes> checkout(@RequestBody OrderReqRes orderReqRes) {
        OrderReqRes order = orderService.checkoutCartAndCreateOrder(orderReqRes);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // Get a specific order by ID
    @GetMapping("/all-orders-for-user")
    public ResponseEntity<OrderReqRes> getOrder(@RequestBody OrderReqRes orderReqRes) {
        OrderReqRes order = orderService.getAllOrdersForUser(orderReqRes);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/order-by-order-id")
    public ResponseEntity<OrderReqRes> getOrderByOrderId(@RequestBody OrderReqRes orderReqRes) {
        OrderReqRes order = orderService.getOrderByOrderId(orderReqRes);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


//    // Get all orders (optional)
//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    // Update order status (for example: 'PENDING', 'COMPLETED', 'CANCELLED')
//    @PutMapping("/{orderId}")
//    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
//        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
//        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
//    }
//
//    // Delete an order (optional, depending on your business logic)
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
//        orderService.deleteOrder(orderId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
