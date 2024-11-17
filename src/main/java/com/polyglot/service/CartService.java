package com.polyglot.service;

import com.polyglot.dto.CartReqRes;
import com.polyglot.dto.RestaurantReqRes;
import com.polyglot.entity.*;
import com.polyglot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

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

    // Create a new cart
    public CartReqRes addDishToCart(CartReqRes cartReqRes) {
        CartReqRes resp = new CartReqRes();
        try {
            Optional<Dish> dishOptional = dishRepo.findById(cartReqRes.getDishId());
            if(!dishOptional.isPresent()){
                resp.setStatusCode(404);
                resp.setMessage("Dish not found");
                return resp;
            }

            Optional<OurUsers> userOptional = usersRepo.findById(cartReqRes.getUserId());
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
                userCart = new Cart();
            }
            List<Integer> userCartItems = userCart.getCartItems();

            List<CartItem> cartItemsFinal = new ArrayList<>();
            Integer totalPrice = userCart.getTotalPrice();
            Integer priceToBeAddedForDishSelection = dishOptional.get().getPrice() * cartReqRes.getQuantity();
            userCart.setTotalPrice(totalPrice + priceToBeAddedForDishSelection);
            resp.setCartPrice(totalPrice + priceToBeAddedForDishSelection);
            boolean isDishNotInCartItem = false;
            for(Integer cartItemId : userCartItems){
                Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
                if(cartItemOptional.isPresent()){
                    if(Objects.equals(cartItemOptional.get().getDishId(), cartReqRes.getDishId())){
                        isDishNotInCartItem = true;

                        Integer quantity = cartItemOptional.get().getQuantity();
                        Integer price = cartItemOptional.get().getTotalPrice();
                        quantity += cartReqRes.getQuantity();

                        price += priceToBeAddedForDishSelection;
                        cartItemOptional.get().setQuantity(quantity);
                        cartItemOptional.get().setTotalPrice(price);
                        CartItem cartItem = cartItemRepo.save(cartItemOptional.get());
                    }
                    cartItemsFinal.add(cartItemOptional.get());
                }
            }
            if(!isDishNotInCartItem){
                CartItem newCartItem = new CartItem();
                newCartItem.setDishId(cartReqRes.getDishId());
                newCartItem.setName(dishOptional.get().getName());
                newCartItem.setDishType(dishOptional.get().getDishType());
                newCartItem.setDescription(dishOptional.get().getDescription());
                newCartItem.setPrice(dishOptional.get().getPrice());
                newCartItem.setCuisine(dishOptional.get().getCuisine());

                newCartItem.setQuantity(cartReqRes.getQuantity());
                newCartItem.setTotalPrice(priceToBeAddedForDishSelection);
                totalPrice += priceToBeAddedForDishSelection;
                newCartItem.setCartId(userCart.getId());
                newCartItem = cartItemRepo.save(newCartItem);
                cartItemsFinal.add(newCartItem);
                List<Integer> userCartItemsIds = userCart.getCartItems();
                userCartItemsIds.add(newCartItem.getId());
                userCart.setCartItems(userCartItemsIds);
                userCart = cartRepo.save(userCart);
                newCartItem.setCartId(userCart.getId());
                newCartItem = cartItemRepo.save(newCartItem);
                userOptional.get().setCartId(newCartItem.getCartId());
                OurUsers userUpdated = usersRepo.save(userOptional.get());
            }

            resp.setUserId(cartReqRes.getUserId());
            resp.setStatusCode(200);
            resp.setCartId(userCart.getId());
            resp.setCartItems(cartItemsFinal);


        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public CartReqRes getCartById(CartReqRes cartReqRes) {
        CartReqRes resp = new CartReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(cartReqRes.getUserId());
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
                userCart = new Cart();
            }
            List<Integer> userCartItems = userCart.getCartItems();

            List<CartItem> cartItemsFinal = new ArrayList<>();
            Integer totalPrice = userCart.getTotalPrice();
            Integer priceToBeAddedForDishSelection = dishOptional.get().getPrice() * cartReqRes.getQuantity();
            userCart.setTotalPrice(totalPrice + priceToBeAddedForDishSelection);
            resp.setCartPrice(totalPrice + priceToBeAddedForDishSelection);
            boolean isDishNotInCartItem = false;
            for(Integer cartItemId : userCartItems){
                Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
                if(cartItemOptional.isPresent()){
                    if(Objects.equals(cartItemOptional.get().getDishId(), cartReqRes.getDishId())){
                        isDishNotInCartItem = true;

                        Integer quantity = cartItemOptional.get().getQuantity();
                        Integer price = cartItemOptional.get().getTotalPrice();
                        quantity += cartReqRes.getQuantity();

                        price += priceToBeAddedForDishSelection;
                        cartItemOptional.get().setQuantity(quantity);
                        cartItemOptional.get().setTotalPrice(price);
                        CartItem cartItem = cartItemRepo.save(cartItemOptional.get());
                    }
                    cartItemsFinal.add(cartItemOptional.get());
                }
            }
            if(!isDishNotInCartItem){
                CartItem newCartItem = new CartItem();
                newCartItem.setDishId(cartReqRes.getDishId());
                newCartItem.setName(dishOptional.get().getName());
                newCartItem.setDishType(dishOptional.get().getDishType());
                newCartItem.setDescription(dishOptional.get().getDescription());
                newCartItem.setPrice(dishOptional.get().getPrice());
                newCartItem.setCuisine(dishOptional.get().getCuisine());

                newCartItem.setQuantity(cartReqRes.getQuantity());
                newCartItem.setTotalPrice(priceToBeAddedForDishSelection);
                totalPrice += priceToBeAddedForDishSelection;
                newCartItem.setCartId(userCart.getId());
                newCartItem = cartItemRepo.save(newCartItem);
                cartItemsFinal.add(newCartItem);
                List<Integer> userCartItemsIds = userCart.getCartItems();
                userCartItemsIds.add(newCartItem.getId());
                userCart.setCartItems(userCartItemsIds);
                userCart = cartRepo.save(userCart);
                newCartItem.setCartId(userCart.getId());
                newCartItem = cartItemRepo.save(newCartItem);
                userOptional.get().setCartId(newCartItem.getCartId());
                OurUsers userUpdated = usersRepo.save(userOptional.get());
            }

            resp.setUserId(cartReqRes.getUserId());
            resp.setStatusCode(200);
            resp.setCartId(userCart.getId());
            resp.setCartItems(cartItemsFinal);


        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}

