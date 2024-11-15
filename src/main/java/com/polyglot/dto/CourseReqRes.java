package com.polyglot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.polyglot.entity.Restaurant;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseReqRes {
    private String title;
    private String description;
    private String content;
    private Restaurant restaurant;
    private int statusCode;
    private String error;
    private String message;

}
