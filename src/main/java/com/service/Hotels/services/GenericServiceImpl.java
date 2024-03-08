package com.service.Hotels.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ConflictException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.GenericService;
import com.service.Hotels.repositories.GenericRepository;

@Service
public class GenericServiceImpl<T> implements GenericService<T>{

    @Autowired
    private GenericRepository<T, Long> repository;

    @Override
    public List<T> getAll() {
        List<T> entities = repository.findAll();
        if (entities.isEmpty()) {
            throw new NotFoundException("No data found");
        }
        return entities;
    }

    @Override
    public T getEntityById(Long id) {
        if (id == null) {
            new BadRequestException("Ehe ID cannot be null");
        }
        return repository.findById(id).orElseThrow(() -> new NotFoundException("there is no entity with ID " + id));
    }

    @Override
    public T addEntity(T entity) {
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
             // si hay violacion en la clave única o intentadno insertar un intedad ya
            throw new ConflictException("Error adding entity: Data integrity violation");
        } catch (Exception e) {
            // Si ocurre cualquier otra excepción no esperada
            throw new ServerErrorException("Error adding entity: " + e.getMessage(), e);
        }
    }

    @Override
    public T updateEntity(T entity) {
         try {
            T entityUpdated = repository.save(entity);
            return entityUpdated;
        } 
        catch(DataIntegrityViolationException e) {
            throw new ConflictException("Error adding entity: Data integrity violation");
        }
        catch (Exception e) {
            throw new BadRequestException("Error adding new entity : " + entity.getClass().getSimpleName());
        }
    }

    @Override
    public void removeEntity(Long id) {
        if (id == null) {
            throw new BadRequestException("Ehe ID cannot be null");
        }
        if (repository.existsById(id)) {
            try {
                repository.deleteById(id);
            } catch (IllegalArgumentException ex) {
                throw new ConflictException("Error moving entity: Data integrity violation");
            }
        }
    }

    @Override
    public List<T> getAll(Long id) {
        List<T> entities = repository.getAllRoomsByHotelId(id);
        if (entities.isEmpty()) {
            throw new NotFoundException("No data found");
        }
        return entities;
    }

}
