package com.example.hotel_thymeleaf_security.service;


import java.util.UUID;

/**
 * @param <T> type of entity
 * @param <R> type of request
 */

public interface BaseService<T, R> {

    T create(R r);

    T getById(UUID id);

    T update(R r, UUID id);

    void deleteById(UUID id);

}
