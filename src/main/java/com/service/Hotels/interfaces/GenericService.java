package com.service.Hotels.interfaces;


import java.util.*;

public interface GenericService<T> {
    List<T> getAll();
    List<T> getAll(Long id);
    T getEntityById(Long id);
    T addEntity(T entity);
    T updateEntity(T entity);
    void removeEntity(Long id);
}
