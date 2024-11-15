package com.polyglot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuesAnsReqRes {

    private int statusCode;
    private String error;
    private String message;

    private Integer questionId;
    private String question;
    private String answer;
    private Integer courseId;
    private String questionType;

    private Integer userId;
    private String response;
    private String attemptDateTime;
    private Integer marks;

}
