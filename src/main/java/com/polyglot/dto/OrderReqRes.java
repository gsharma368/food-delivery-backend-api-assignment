package com.polyglot.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.polyglot.entity.CartItem;
import com.polyglot.entity.Order;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderReqRes {

    private int statusCode;
    private String error;
    private String message;


    private Integer userId;
    private Integer cartId;
    private Integer orderId;

    private boolean isPaymentDone;


    private Integer quantity;
    private Integer orderPrice;
    private List<CartItem> orderItems;

    private List<Order> orders;
    private String response;
}
