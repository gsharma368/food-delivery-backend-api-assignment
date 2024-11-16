package com.polyglot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DishReqRes {

    private int statusCode;
    private String error;
    private String message;

    private Integer dishId;
    private String dishName;
    private String dishPrice;
    private Integer restaurantId;
    private String dishType;
    private String dishDescription;
    private String dishCuisine;

    private Integer userId;
    private String response;
    private String attemptDateTime;
    private Integer marks;

}
