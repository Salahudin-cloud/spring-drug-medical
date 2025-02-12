package com.example.drugmed.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebResponse <T>{
    private String message;

    private Integer status;

    private T data;

    private List<String> errors;
}
