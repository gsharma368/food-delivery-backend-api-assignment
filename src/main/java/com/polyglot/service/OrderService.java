package com.polyglot.service;

import com.polyglot.entity.*;
import com.polyglot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private OrderRepo orderRepository;

    // Create an order from a cart
//    public Order checkoutCart(Long cartId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//        Order order = new Order(cart);
//        return orderRepository.save(order);
//    }
//
//    // Get an order by ID
//    public Order getOrderById(Long orderId) {
//        return orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//    }
//
//    // Get all orders
//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    // Update the order status
//    public Order updateOrderStatus(Long orderId, String status) {
//        Order order = getOrderById(orderId);
//        order.setStatus(status);
//        return orderRepository.save(order);
//    }
//
//    // Delete an order
//    public void deleteOrder(Long orderId) {
//        orderRepository.deleteById(orderId);
//    }
}
