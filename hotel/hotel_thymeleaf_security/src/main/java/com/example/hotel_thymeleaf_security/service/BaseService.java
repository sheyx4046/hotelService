package com.example.hotel_thymeleaf_security.service;


import java.util.UUID;

/**
 * @param <T> type of entity
 * @param <R> type of request
 */

public interface BaseService<T, R> {

    T create(R r);

    T getById(Long id);

    T update(R r, Long id);

    void deleteById(Long id);

}
