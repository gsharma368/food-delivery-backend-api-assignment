package com.polyglot.service;

import com.polyglot.dto.CartReqRes;
import com.polyglot.dto.OrderReqRes;
import com.polyglot.entity.*;
import com.polyglot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private DishRepo dishRepo;

    @Autowired
    private OrderRepo orderRepo;

    // Create an order from a cart
    public OrderReqRes checkoutCartAndCreateOrder(OrderReqRes orderReqRes) {

        OrderReqRes resp = new OrderReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(orderReqRes.getUserId());
            if(!userOptional.isPresent()){
                resp.setStatusCode(404);
                resp.setMessage("User not found");
                return resp;
            }
            Cart userCart;
            if(userOptional.get().getCartId() != null &&  userOptional.get().getCartId() > 0){
                Optional<Cart> cartOptional = cartRepo.findById(userOptional.get().getCartId());
                userCart = cartOptional.get();
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Empty Cart for the User");
                return resp;
            }

            Order addOrder = new Order();
            addOrder.setStatus("PENDING");

            addOrder.setPaymentDone(orderReqRes.isPaymentDone());
            List<Integer> userCartItems = userCart.getCartItems();

            List<CartItem> cartItemsFinal = new ArrayList<>();
            Integer totalPrice = userCart.getTotalPrice();
            resp.setOrderPrice(totalPrice);
            addOrder.setTotalPrice(totalPrice);

            List<CartItem> orderItemsFinal = new ArrayList<>();
            addOrder.setTotalPrice(totalPrice);
            List<Integer> orderItemsIdsList = new ArrayList<>();

            for(Integer cartItemId : userCartItems){
                Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
                if(cartItemOptional.isPresent()){

                    orderItemsIdsList.add(cartItemOptional.get().getId());
                    CartItem cartItem = cartItemRepo.save(cartItemOptional.get());

                    orderItemsFinal.add(cartItemOptional.get());
                }
            }
            addOrder.setOrderItems(orderItemsIdsList);
            addOrder.setUserId(userOptional.get().getId());
            addOrder = orderRepo.save(addOrder);

            userOptional.get().setCartId(0);
            OurUsers userUpdated = usersRepo.save(userOptional.get());
            resp.setOrderItems(orderItemsFinal);
            resp.setOrderId(addOrder.getId());
            resp.setUserId(orderReqRes.getUserId());
            resp.setStatusCode(200);
            resp.setCartId(userCart.getId());

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    // Get an order by ID
    public OrderReqRes getAllOrdersForUser(OrderReqRes orderReqRes) {
        OrderReqRes resp = new OrderReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(orderReqRes.getUserId());
            if(!userOptional.isPresent()){
                resp.setStatusCode(404);
                resp.setMessage("User not found");
                return resp;
            }
            List<Order> allOrders = orderRepo.findAll();
            List<Order> userOrders = new ArrayList<>();
            for(Order order : allOrders){
                if(order.getUserId() == orderReqRes.getUserId()){
                    userOrders.add(order);
                }
            }
            resp.setOrders(userOrders);
            resp.setUserId(orderReqRes.getUserId());
            resp.setStatusCode(200);

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public OrderReqRes getOrderByOrderId(OrderReqRes orderReqRes) {
        OrderReqRes resp = new OrderReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(orderReqRes.getUserId());
            if(!userOptional.isPresent()){
                resp.setStatusCode(404);
                resp.setMessage("User not found");
                return resp;
            }
            Order userOrder;
            if(orderReqRes.getOrderId() != null &&  orderReqRes.getOrderId() > 0){
                Optional<Order> orderOptional = orderRepo.findById(orderReqRes.getOrderId());
                userOrder = orderOptional.get();
                if(userOrder.getUserId() != orderReqRes.getUserId()){
                    resp.setStatusCode(404);
                    resp.setMessage("This order Id does not belong to this user.");
                    return resp;
                }
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Valid OrderId not given in payload.");
                return resp;
            }

            List<Integer> userCartItems = userOrder.getOrderItems();

            List<CartItem> cartItemsFinal = new ArrayList<>();
            resp.setOrderPrice(userOrder.getTotalPrice());

            List<CartItem> orderItemsFinal = new ArrayList<>();
            List<Integer> orderItemsIdsList = new ArrayList<>();

            for(Integer cartItemId : userCartItems){
                Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
                if(cartItemOptional.isPresent()){

                    orderItemsIdsList.add(cartItemOptional.get().getId());
                    CartItem cartItem = cartItemRepo.save(cartItemOptional.get());

                    orderItemsFinal.add(cartItemOptional.get());
                }
            }

            resp.setOrderItems(orderItemsFinal);
            resp.setOrderId(orderReqRes.getOrderId());
            resp.setUserId(orderReqRes.getUserId());
            resp.setStatusCode(200);

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

}
