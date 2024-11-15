package com.polyglot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.polyglot.entity.Restaurant;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgessReqRes {
    private Integer attemptedQuestions;
    private Integer unAttemptedQuestions;
    private Integer totalQuestionsInTheCourse;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer totalMarks;
    private Restaurant restaurant;
    private int statusCode;
    private String error;
    private String message;
}
