package com.example.hotel_thymeleaf_security.entity.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BaseResponse<T> {
    private String message;
    private Integer status;
    private T data;
    private boolean success;

}
