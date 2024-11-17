package com.polyglot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.polyglot.entity.CartItem;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartReqRes {

    private int statusCode;
    private String error;
    private String message;

    private Integer dishId;
    private Integer userId;
    private Integer cartId;
    private Integer quantity;
    private Integer cartPrice;
    private List<CartItem> cartItems;
    private String response;
}
