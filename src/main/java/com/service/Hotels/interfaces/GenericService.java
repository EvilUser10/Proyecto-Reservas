package com.service.Hotels.interfaces;


import java.util.*;

public interface GenericService<T> {
    List<T> getAll();
    T getEntityById(Long id);
    T addEntity(T entity);
    T updateEntity(T entity);
    void removeEntity(Long id);
}
